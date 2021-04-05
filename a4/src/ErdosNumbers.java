import java.util.*;

public class ErdosNumbers {
    /**
     * String representing Paul Erdos's name to check against
     */
    public static final String ERDOS = "Paul Erdös";

    /**
     * A mapping of author names to their respective Author instance.
     */
    private Map<String, Author> authors = new HashMap<>();
    /**
     * A mapping of paper names to a list of their authors' names.
     */
    private Map<String, List<String>> papers = new HashMap<>();

    /**
     * Initialises the class with a list of papers and authors.
     *
     * Each element in 'papers' corresponds to a String of the form:
     * 
     * [paper name]:[author1][|author2[|...]]]
     *
     * Note that for this constructor and the below methods, authors and papers
     * are unique (i.e. there can't be multiple authors or papers with the exact same name or title).
     * 
     * @param papers List of papers and their authors
     */
    public ErdosNumbers(List<String> papers) {
        for (String paper : papers) {
            this.parsePaper(paper);
        }
    }
    
    /**
     * Gets all the unique papers the author has written (either solely or
     * as a co-author).
     * 
     * @param author to get the papers for.
     * @return the unique set of papers this author has written.
     */
    public Set<String> getPapers(String author) {
        return this.authors.get(author).getPapers();
    }

    /**
     * Gets all the unique co-authors the author has written a paper with.
     *
     * @param author to get collaborators for
     * @return the unique co-authors the author has written with.
     */
    public Set<String> getCollaborators(String author) {
        return this.authors.get(author).getCollaborators();
    }

    /**
     * Checks if Erdos is connected to all other author's given as input to
     * the class constructor.
     * 
     * In other words, does every author in the dataset have an Erdos number?
     * 
     * @return the connectivity of Erdos to all other authors.
     */
    public boolean isErdosConnectedToAll() {
        Set<String> visited = new HashSet<String>();

        // Use a DFS-style traversal to iterate over all of the nodes connected to Erdos
        // There is no search condition and so the DFS continues until all connected nodes have been traversed.
        Stack<String> frontier = new Stack<String>();
        frontier.add(ERDOS);

        while (frontier.size() > 0) {
            String nextAuthor = frontier.pop();
            if (!visited.contains(nextAuthor)) {
                frontier.addAll(this.getCollaborators(nextAuthor));
                visited.add(nextAuthor);
            }
        }

        // This means the number of nodes visited is the same as the number of authors and thus all authors are
        // connected to Erdos.
        if (visited.size() == authors.size()) {
            return true;
        }
        
        return false;
    }

    /**
     * Calculate the Erdos number of an author. 
     * 
     * This is defined as the length of the shortest path on a graph of paper 
     * collaborations (as explained in the assignment specification).
     * 
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * a defined Erdos number), returns Integer.MAX_VALUE.
     * 
     * Note: Erdos himself has an Erdos number of 0.
     * 
     * @param author to calculate the Erdos number of
     * @return authors' Erdos number or otherwise Integer.MAX_VALUE
     */
    public int calculateErdosNumber(String author) {
        // Use the djikstra's shortest path method as a standard BFS by using a Queue instead of a PriorityQueue and
        // giving each edge a weight of 1.
        double erdosNumber = djikstraShortestPathLength(new LinkedList<>(), ERDOS, author, true);

        if (erdosNumber == Double.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) erdosNumber;
    }

    /**
     * Gets the average Erdos number of all the authors on a paper.
     * If a paper has just a single author, this is just the author's Erdos number.
     *
     * Note: Erdos himself has an Erdos number of 0.
     *
     * @param paper to calculate it for
     * @return average Erdos number of paper's authors
     */
    public double averageErdosNumber(String paper) {
        int sumErdosNumber = 0;
        for (String author : this.papers.get(paper)) {
            sumErdosNumber += this.calculateErdosNumber(author);
        }
        
        return (double) sumErdosNumber / (double) this.papers.get(paper).size();
    }

    /**
     * Calculates the "weighted Erdos number" of an author.
     * 
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * an Erdos number), returns Double.MAX_VALUE.
     *
     * Note: Erdos himself has a weighted Erdos number of 0.
     * 
     * @param author to calculate it for
     * @return author's weighted Erdos number
     */
    public double calculateWeightedErdosNumber(String author) {
        // Apply djikstra's algorithm with a PriorityQueue being used as the frontier.
        return djikstraShortestPathLength(new PriorityQueue<>(), ERDOS, author, false);
    }

    /**
     * Parses a paper and adds any new authors found to this.authors and the paper to this.papers. Each involved
     * author's collaboration Map is also updated to reflect the new collaborators.
     * @param paper the full string representing a particular paper including the authors
     */
    private void parsePaper(String paper) {
        String[] details = paper.split(":");
        String paperName = details[0];
        String[] authorNames = details[1].split("\\|");

        papers.put(paperName, Arrays.asList(authorNames));

        for (int i = 0; i < authorNames.length; i++) {
            String authorName = authorNames[i];
            this.parseAuthor(paperName, authorName);
            for (int j = i + 1; j < authorNames.length; j++) {
                String otherAuthorName = authorNames[j];
                this.parseAuthor(paperName, otherAuthorName);
                this.parseCollaboration(authorName, otherAuthorName);
            }

        }
    }

    /**
     * Parses a particular author for a particular paper and adds it to this.authors if they aren't already in it. In
     * both cases the name of the paper is added to the new or existing author.
     * @param paperName the paper the author worked on
     * @param authorName the name of the author
     */
    private void parseAuthor(String paperName, String authorName) {
        this.authors.putIfAbsent(authorName, new Author(authorName));
        this.authors.get(authorName).addPaper(paperName);
    }

    /**
     * Parses a collaboration between two authors and adds it to both authors.
     * @param authorName the name of the first author
     * @param otherAuthorName the name of the second author
     */
    private void parseCollaboration(String authorName, String otherAuthorName) {
        Author first = this.authors.get(authorName);
        Author second = this.authors.get(otherAuthorName);
        first.addCollaboration(second.getName());
        second.addCollaboration(first.getName());
    }

    /**
     * A class representing a particular Author. Since each Author has a unique name, this class uses name as a unique
     * identifier. It also stores all of the papers an author has worked on as well as the name of author's they
     * have collaborated with and the amount of collaborations.
     */
    private class Author {
        /* Instance of an Author that needs to be compared */
        private String name;
        /* A Map between collaborators' names and the number of times they have collaborated with this author */
        private Map<String, Integer> collaborations;
        /* A Set of all of the papers this author has worked on */
        private Set<String> papers;

        /**
         * Constructor for Author.
         * @param name the unique name of an author
         */
        private Author(String name) {
            this.name = name;
            this.collaborations = new HashMap<String, Integer>();
            this.papers = new HashSet<String>();
        }

        /**
         * Get the name of the author.
         * @return the name of the author
         */
        private String getName() {
            return this.name;
        }

        /**
         * Adds a collaboration with the given author name. The author name should be the same as is used elsewhere.
         * If a collaboration with this author has been added before, a counter of the two authors' collaborations
         * is kept.
         * @param collaborator the unique name of the collaborating author
         */
        private void addCollaboration(String collaborator) {
            this.collaborations.put(collaborator, this.collaborations.getOrDefault(collaborator, 0) + 1);
        }

        /**
         * Gets the number of this author has collaborated with the author with the given name.
         * @param collaborator
         * @return the number of times this author has collaborated with the author with the given name if they have
         *      collaborated before, otherwise a null
         */
        private int getCollaborationCount(String collaborator) {
            return collaborations.get(collaborator);
        }

        /**
         * Adds a new paper to this author's set of papers they have worked on.
         * @param paper the paper to add to the set
         */
        private void addPaper(String paper) {
            this.papers.add(paper);
        }

        /**
         * Gets a set of papers that this author has worked on in this past.
         * @return a set of strings representing the papers that this author has worked on
         */
        private Set<String> getPapers() {
            return this.papers;
        }

        /**
         * Gets a set of collaborators (authors) that this author has worked with. This set contains the names of the
         * authors not an Author representation.
         * @return a set of strings representing the names of all the authors this author has collaborated with
         */
        private Set<String> getCollaborators() {
            return this.collaborations.keySet();
        }
    }

    /**
     * A comparable wrapper class for an Author instance. It should store the distance from Erdos within the graph
     * representation and use this as the basis of comparison.
     */
    private class ComparableWeightedAuthor implements Comparable {
        /* Instance of an Author that needs to be compared */
        private Author author;
        /* The weight given to this Author (distance from Erdos in this case) */
        private double priority;

        /**
         * Constructor for ComparableWeightedAuthor.
         * @param author an Author instance that should be wrapped
         * @param priority the priority given to the Author - used for comparing ComparableWeightedAuthors
         */
        private ComparableWeightedAuthor(Author author, double priority) {
            this.author = author;
            this.priority = priority;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof ComparableWeightedAuthor)) {
                throw new ClassCastException();
            }

            return Double.compare(this.priority, ((ComparableWeightedAuthor) o).priority);
        }
    }

    /**
     * Runs Djikstra's algorithm on the Authors graph stored in this.author and gives the shortest path length
     *
     * Begins at start and tries to reach end using the given Queue "frontier" as the discovery edge or frontier.
     * If "unweighted" is set to true then this essentially acts as a normal BFS and a LinkedList can be passed to
     * "frontier". To use in Djikstra mode, set "unweighted" to false and pass a PriorityQueue to "frontier".
     *
     * @param frontier the discovery edge data structure to use
     * @param start the name of the author to start from
     * @param end the name of the author to try and reach
     * @param unweighted whether weights should be used for the edges - i.e. if true then a BFS is run otherwise
     *                   Djikstra's algorithm is run
     * @return the length of the shortest path between start and end in the stored graph
     */
    private double djikstraShortestPathLength(Queue<ComparableWeightedAuthor> frontier, String start, String end,
             boolean unweighted) {
        Set<String> visited = new HashSet<>();

        // A map containing the current lowest known Erdos number of all explored authors
        Map<String, Double> erdosToNode = new HashMap<>();
        erdosToNode.put(start, 0.0d);

        // LinkedList used with addFirst and removeLast to act as a Queue

        frontier.add(new ComparableWeightedAuthor(this.authors.get(start), 0.0));

        // Performs a BFS to find the author. Each of the author's collaborators are expanded and added to the frontier
        // at each iteration.
        while (frontier.size() > 0) {
            ComparableWeightedAuthor currentAuthor = frontier.remove();

            double currentAuthorErdos = erdosToNode.get(currentAuthor.author.name);
            for (String collaborator : this.getCollaborators(currentAuthor.author.name)) {
                double weight = getEdgeWeight(currentAuthor.author.name, collaborator, unweighted);
                // The collaborator has never been seen before or there's a new smaller Erdos number for this author
                if (!visited.contains(collaborator)
                        || (erdosToNode.containsKey(collaborator)
                        && currentAuthorErdos + weight < erdosToNode.get(collaborator))) {
                    erdosToNode.put(collaborator, currentAuthorErdos + weight);
                    frontier.add(new ComparableWeightedAuthor(this.authors.get(collaborator),
                            currentAuthorErdos + weight));
                    visited.add(collaborator);
                }
            }

            if (currentAuthor.author.name.equals(end)) {
                return erdosToNode.get(end);
            }
        }

        return Double.MAX_VALUE;
    }

    /**
     * Gets the weight of a particular edge using the formula given in the tasksheet. If "unweighted" is passed as true
     * then this method always returns 1.0d.
     * @param start the name of the author to start from
     * @param end the name of the author to end at
     * @param unweighted whether a weighted or unweighted weight is required
     * @return the weight of a particular edge in the stored graph
     */
    private double getEdgeWeight(String start, String end, boolean unweighted) {
        if (unweighted) {
            return 1.0d;
        } else {
            return (double) 1 / (this.authors.get(start).getCollaborationCount(end));
        }
    }
}
