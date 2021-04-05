import java.util.*;

public class FactChecker {

    /* A suffix to add to the end of a name to represent that a particular node is the leaving node of a person. */
    private static final String LEAVE_SUFFIX = "-L";
    /* A suffix to add to the end of a name to represent that a particular node is the arriving node of a person. */
    private static final String ARRIVE_SUFFIX = "-A";

    /**
     * Checks if a list of facts is internally consistent. 
     * That is, can they all hold true at the same time?
     * Or are two (or potentially more) facts logically incompatible?
     * 
     * @param facts list of facts to check consistency of
     * @return true if all the facts are internally consistent, otherwise false.
     */
    public static boolean areFactsConsistent(List<Fact> facts) {
        Map<String, PersonEventNode> people = new HashMap<>();

        // Construct a directed graph using the given facts.
        // Each node in the graph represents either a person arriving or a person leaving so each person is represented
        // by two nodes that are connected with a directed edge such that A_arriving -> A_leaving. Given a fact of
        // type one, the leaving node of the first person is connected to the arriving node of the second person. That
        // is, given Fact1(A, B): A_leaving -> A_arriving. Given a fact of type two, the arriving node of either person
        // is connected to the leaving node of the other and so given Fact2(A, B): A_arriving -> B_leaving and
        // B_arriving -> A_leaving.
        for (Fact fact : facts) {
            switch (fact.getType()) {
                case TYPE_ONE:
                    addNewTypeOneFact(fact, people);
                    break;
                case TYPE_TWO:
                    addNewTypeTwoFact(fact, people);
                    break;
            }
        }

        Set<String> toCheck = new HashSet<>(people.keySet());
        Set<String> currentTraversalFrontier = new HashSet<>();
        // Used to account for situations where the graph may be disconnected, allows for the DFS to be started again
        // from another node.
        while (toCheck.size() > 0) {
            String start = toCheck.iterator().next();

            if (graphContainsCycles(start, people, currentTraversalFrontier, toCheck)) {
                // If there is a cycle in the graph then there must be an inconcistency in the facts
                return false;
            }
        }

        return true;
    }

    /**
     * Conducts a DFS within the given "people" graph. Continues until either a node is found that has already been
     * checked or the node "person" doesn't have any neighbours to go to. Essentially, this finds whether a back
     * edge exists between a node and one of its predecessors which is the only way for a cycle to exist.
     *
     * When the neighbours of the "person" node are expanded each neighbour is added to the checked set and removed
     * from the toCheck set.
     *
     * @param person the name of the person to start at
     * @param people the graph representation of the facts
     * @param currentTraversalFrontier the nodes that have been discovered but not completed
     * @param toCheck the nodes that have not been discovered yet
     * @return true if a cycle exists in the graph and false otherwise
     */
    private static boolean graphContainsCycles(String person, Map<String, PersonEventNode> people,
            Set<String> currentTraversalFrontier, Set<String> toCheck) {

        if (currentTraversalFrontier.contains(person)) {
            return true;
        }

        if (!toCheck.contains(person)) {
            return false;
        }

        currentTraversalFrontier.add(person);
        toCheck.remove(person);

        for (BeforeEdge edge : people.get(person).startEdges.values()) {
            if (graphContainsCycles(edge.getEndingPerson().name, people, currentTraversalFrontier, toCheck)) {
                return true;
            }
        }

        currentTraversalFrontier.remove(person);
        return false;
    }

    /**
     * Adds a new type one fact to the given graph "people" as described in areFactsConsistent().
     *
     * @param fact the fact to add
     * @param people the graph to add the fact to
     */
    private static void addNewTypeOneFact(Fact fact, Map<String, PersonEventNode> people) {
        PersonEventNode personAArrive = addNewPerson(fact.getPersonA() + ARRIVE_SUFFIX, people);
        PersonEventNode personALeave = addNewPerson(fact.getPersonA() + LEAVE_SUFFIX, people);
        PersonEventNode personBArrive = addNewPerson(fact.getPersonB() + ARRIVE_SUFFIX, people);
        PersonEventNode personBLeave = addNewPerson(fact.getPersonB() + LEAVE_SUFFIX, people);

        addNewEdgeToPeople(personAArrive, personALeave); // A arrives before they leave
        addNewEdgeToPeople(personBArrive, personBLeave); // B arrives before they leave
        addNewEdgeToPeople(personALeave, personBArrive); // A leaves before B arrives since this is a type one fact
    }

    /**
     * Adds a new type two fact to the given graph "people" as described in areFactsConsistent().
     *
     * @param fact the fact to add
     * @param people the graph to add the fact to
     */
    private static void addNewTypeTwoFact(Fact fact, Map<String, PersonEventNode> people) {
        PersonEventNode personAArrive = addNewPerson(fact.getPersonA() + ARRIVE_SUFFIX, people);
        PersonEventNode personALeave = addNewPerson(fact.getPersonA() + LEAVE_SUFFIX, people);
        PersonEventNode personBArrive = addNewPerson(fact.getPersonB() + ARRIVE_SUFFIX, people);
        PersonEventNode personBLeave = addNewPerson(fact.getPersonB() + LEAVE_SUFFIX, people);

        addNewEdgeToPeople(personAArrive, personALeave); // A arrives before they leave
        addNewEdgeToPeople(personBArrive, personBLeave); // B arrives before they leave
        addNewEdgeToPeople(personAArrive, personBLeave); // A arrives before B leaves since this is a type two fact
        addNewEdgeToPeople(personBArrive, personALeave); // B arrives before A leaves since this is a type two fact
    }

    /**
     * Creates a new BeforeEdge and adds it to both person1 and person2.
     *
     * A BeforeEdge denotes a hierarchy where person1 "happens" before person2. That is, person1 could leave before
     * person2 or person1 could arrive before person2 depending on the types of the given PersonEventNodes. This method
     * doesn't check for semantic accuracy so it is possible to have a person1 and person2 where they are both of the
     * ARRIVE_SUFFIX type or some other non-semantic version of this.
     *
     * @param person1
     * @param person2
     */
    private static void addNewEdgeToPeople(PersonEventNode person1, PersonEventNode person2) {
        BeforeEdge relation = new BeforeEdge(person1, person2);
        person1.addEdge(relation);
        person2.addEdge(relation);
    }

    /**
     * Pretty much the same as putIfAbsent for a Map but only creates the instance if it isn't mapped to already,
     * whereas putIfAbsent needs a new instance passed in regardless.
     *
     * @param personName name of the person with either a LEAVE_SUFFIX or ARRIVE_SUFFIX
     * @param people the Map to add this person to
     * @return the instance pointed to by the given personName, this could be one that is newly created or one that
     *      already existed in the given people Map
     */
    private static PersonEventNode addNewPerson(String personName, Map<String, PersonEventNode> people) {
        PersonEventNode person;
        if (!people.containsKey(personName)) {
            person = new PersonEventNode(personName);
            people.put(personName, person);
        } else {
            person = people.get(personName);
        }

        return person;
    }

    /**
     * A PersonEventNode represents an event occurring. An event can either be of the arriving or leaving type and
     * this should be reflected by adding ARRIVE_SUFFIX or LEAVE_SUFFIX to the end of each person's name. Each
     * PersonEventNode with an ARRIVE_SUFFIX should be connected to a PersonEventNode with a LEAVE_SUFFIX using a
     * BeforeEdge in order to reflect the relation that a particular person arrives before they leave.
     */
    private static class PersonEventNode {
        /* Name of the person including one of ARRIVE_SUFFIX or LEAVE_SUFFIX */
        private String name;
        /* Stores all edges that start from this node */
        private Map<PersonEventNode, BeforeEdge> startEdges;
        /* Stores all edges that end at this node */
        private Map<PersonEventNode, BeforeEdge> endEdges;

        /**
         * Constructor for PersonEventNode.
         * @param name the name of the person including either ARRIVE_SUFFIX or LEAVE_SUFFIX
         */
        private PersonEventNode(String name) {
            this.name = name;
            this.startEdges = new HashMap<>();
            this.endEdges = new HashMap<>();
        }

        /**
         * Adds a the given edge to this node if it has not been added already.
         * @param edge the edge to add
         */
        private void addEdge(BeforeEdge edge) {
            PersonEventNode other;
            // Checks whether the edge starts at this node or ends at this node and adds it to the respective Map.
            if (edge.getStartingPerson().equals(this)) {
                other = edge.getEndingPerson();
                startEdges.putIfAbsent(other, edge);
            } else {
                other = edge.getStartingPerson();
                endEdges.putIfAbsent(other, edge);
            }
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        /**
         * Compares the "this" instance to the "other" instance and returns if they are equal or not.
         *
         * @param other the instance to compare to
         * @throws ClassCastException if "other" is not a PersonEventNode
         * @return true if the "name" of "this" is the same as "other"
         */
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof PersonEventNode)) {
                throw new ClassCastException();
            }

            return this.name.equals(((PersonEventNode) other).name);
        }
    }

    /**
     * A BeforeEdge provides a directed edge between two PersonEventNodes showing that the "start" node occurred
     * before the "end" node.
     */
    private static class BeforeEdge {
        /* Node the edge starts from */
        private PersonEventNode start;
        /* Node the edge ends at */
        private PersonEventNode end;

        /**
         * Constructor for BeforeEdge.
         * @param start node to set as the start of this edge
         * @param end node to set as the end of this edge
         */
        private BeforeEdge(PersonEventNode start, PersonEventNode end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Get the start node of this edge.
         * @return start node of this edge
         */
        private PersonEventNode getStartingPerson() {
            return this.start;
        }

        /**
         * Get the end node of this edge.
         * @return end node of this edge
         */
        private PersonEventNode getEndingPerson() {
            return this.end;
        }
    }
}
