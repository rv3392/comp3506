import java.util.*;

public class ErdosNumbers {
    /**
     * String representing Paul Erdos's name to check against
     */
    public static final String ERDOS = "Paul Erdös";

    private HashMap<String, Author> authors = new HashMap<String, Author>();

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

    private void parsePaper(String paper) {
        String[] details = paper.split(":");
        String paperName = details[0];
        String[] authorNames = details[1].split("\\|");

        for (int i = 0; i < authorNames.length; i++) {
            String authorName = authorNames[i];
            this.parseAuthor(paperName, authorName);
            for (int j = i + 1; j < authorNames.length; j++) {
                String otherAuthorName = authorNames[j];
                this.parseAuthor(paperName, otherAuthorName);
                this.parseCollaboration(paperName, authorName, otherAuthorName);
            }

        }
    }

    private void parseAuthor(String paperName, String authorName) {
        this.authors.putIfAbsent(authorName, new Author(authorName));
        this.authors.get(authorName).addPaper(paperName);
    }

    private void parseCollaboration(String paperName, String authorName, String otherAuthorName) {
        Author first = this.authors.get(authorName);
        Author second = this.authors.get(otherAuthorName);

        if (!first.hasCollaboratedWith(second)) {
            Collaboration newAuthorCollaboration = new Collaboration(first, second);
            first.addCollaboration(second, newAuthorCollaboration);
            second.addCollaboration(first, newAuthorCollaboration);
        }

        first.getCollaboration(second).incrementPaperCount();
    }

    private class Author {
        private String name;
        private Map<String, Collaboration> collaborations;
        private Set<String> papers;

        private Author(String name) {
            this.name = name;
            this.collaborations = new HashMap<String, Collaboration>();
            this.papers = new HashSet<String>();
        }

        private String getName() {
            return this.name;
        }

        private void addCollaboration(Author collaborator, Collaboration newCollaboration) {
            this.collaborations.put(collaborator.getName(), newCollaboration);
        }

        private boolean hasCollaboratedWith(Author collaborater) {
            if (collaborations.containsKey(collaborater.getName())) {
                return true;
            }

            return false;
        }

        private Collaboration getCollaboration(Author collaborator) {
            return collaborations.get(collaborator.getName());
        }

        private void addPaper(String paper) {
            this.papers.add(paper);
        }

        private Set<String> getPapers() {
            return this.papers;
        }

        private Set<String> getCollaborators() {
            return this.collaborations.keySet();
        }
    }

    private class Collaboration {
        private Author author1;
        private Author author2;
        private int numPapersTogether;

        private Collaboration(Author author1, Author author2) {
            this.numPapersTogether = 0;
            this.author1 = author1;
            this.author2 = author2;
        }

        private void incrementPaperCount() {
            this.numPapersTogether++;
        }

        private int getCollaborationCount() {
            return this.numPapersTogether;
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
        Set<String> visited = new HashSet<String>();

        // A map containing the current lowest known Erdos number of all explored authors
        Map<String, Integer> erdosToNode = new HashMap<>();
        erdosToNode.put(ERDOS, 0);

        // LinkedList used with addFirst and removeLast to act as a Queue
        LinkedList<String> frontier = new LinkedList<>();
        frontier.addFirst(ERDOS);

        // Performs a BFS to find the author. Each of the author's collaborators are expanded and added to the frontier
        // at each iteration.
        while (frontier.size() > 0) {
            String currentAuthor = frontier.removeLast();

            int currentAuthorErdos = erdosToNode.get(currentAuthor);
            for (String collaborator : this.getCollaborators(currentAuthor)) {
                // The collaborator has never been seen before or there's a new smaller Erdos number for this author
                if (!visited.contains(collaborator)
                        || (erdosToNode.containsKey(collaborator)
                        && currentAuthorErdos + 1 < erdosToNode.get(collaborator))) {
                    erdosToNode.put(collaborator, currentAuthorErdos + 1);
                    frontier.addFirst(collaborator);
                    visited.add(collaborator);
                }
            }

            if (currentAuthor.equals(author)) {
                return erdosToNode.get(author);
            }
        }
        
        return 0;
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
        // TODO: implement this
        
        return 0;
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
        // TODO: implement this

        return 0;
    }
}
