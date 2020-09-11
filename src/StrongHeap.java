public class StrongHeap {
    private static final int DEFAULT_DEPTH = -1;
    private static final int DEFAULT_VALUE = -1;

    /**
     * Determines whether the binary tree with the given root node is
     * a "strong binary heap", as described in the assignment task sheet.
     *
     * A strong binary heap is a binary tree which is:
     *  - a complete binary tree, AND
     *  - its values satisfy the strong heap property.
     *
     * @param root root of a binary tree, cannot be null.
     * @return true if the tree is a strong heap, otherwise false.
     */
    public static boolean isStrongHeap(BinaryTree<Integer> root) {
        int[] previousDepth = {DEFAULT_DEPTH}; // ugly hack for a mutable int
        if (!isComplete(root, getMaxDepth(root), 0, previousDepth)) {
            return false;
        }

        return isSubtreeStrongHeap(root, 0, DEFAULT_VALUE, DEFAULT_VALUE);
    }

    /**
     * Checks if the subtree formed by the given root is a strong heap.
     *
     * @param subtreeRoot Root of the subtree being checked
     * @param currentDepth Depth from the root of the main tree of this subtree
     * @param parentValue Should be set to DEFAULT_VALUE when called externally
     * @param grandparentValue Should be set to DEFAULT_VALUE when called externally
     * @return True if the subtree formed by the given root is a strong heap or false otherwise.
     */
    private static boolean isSubtreeStrongHeap(BinaryTree<Integer> subtreeRoot, int currentDepth, int parentValue,
            int grandparentValue) {

        if (currentDepth == 0 && subtreeRoot.isLeaf()) {
            // If there is a single node in the binary tree then it is automatically a strong heap
            return true;
        }

        if (currentDepth >= 2 && subtreeRoot.getValue() + parentValue >= grandparentValue
                || currentDepth >= 1 && subtreeRoot.getValue() >= parentValue) {
            // Violates the definition of a strong heap
            return false;
        } else if (subtreeRoot.isLeaf()) {
            // Follows the definition of a strong heap and we've reached a leaf node
            return true;
        }

        // If a leaf node wasn't reached but the definition of a strong heap is followed for the current node then
        // check if the next node follows the strong heap definition.
        if (subtreeRoot.getRight() == null) {
            return isSubtreeStrongHeap(subtreeRoot.getLeft(), currentDepth + 1, subtreeRoot.getValue(),
                    parentValue);
        } else {
            return isSubtreeStrongHeap(subtreeRoot.getLeft(), currentDepth + 1, subtreeRoot.getValue(),
                    parentValue) && isSubtreeStrongHeap(subtreeRoot.getRight(), currentDepth + 1,
                    subtreeRoot.getValue(), parentValue);
        }

    }

    /**
     * Gets what should be the longest root to leaf path if the BinaryTree defined by the given root is complete. For
     * a complete binary tree this should be the path from the root to the left-most node.
     *
     * @param root Starting node (root) of the BinaryTree whose max depth is being found
     * @return Longest root to leaf path of the BinaryTree
     */
    private static int getMaxDepth(BinaryTree<Integer> root) {
        BinaryTree<Integer> currentNode = root;
        int maxDepth = 0;
        while (currentNode.getLeft() != null) {
            maxDepth++;
            currentNode = currentNode.getLeft();
        }

        return maxDepth;
    }

    /**
     * Checks whether a BinaryTree is complete or not.
     *
     * Given a starting node, it recursively calls itself until a leaf node is reached. Upon reaching the leaf node,
     * the method checks if the BinaryTree violates any of the requirements of a complete binary tree - that all
     * nodes are on the left and that all levels apart from the last is completely filled.
     *
     * @param node The node in the binary tree to start from
     * @param maxDepth The maximum depth of the binary tree - i.e. longest root to leaf path
     * @param currentDepth The depth of the current node
     * @param previousDepth The 1st element is the depth of the previous node with the previous node being the last node
     *                      in terms of pre-order traversal - this is a very ugly workaround for a mutable int xd
     * @return Whether the BinaryTree defined by the the root is complete or not
     */
    private static boolean isComplete(BinaryTree<Integer> node, int maxDepth, int currentDepth, int[] previousDepth) {
        if (node.isLeaf()) {
            if (currentDepth > maxDepth || currentDepth < maxDepth - 1
                    || (currentDepth > previousDepth[0] && previousDepth[0] != DEFAULT_DEPTH)) {
                // Set last depth to this node's depth.
                previousDepth[0] = currentDepth;
                return false;
            }

            previousDepth[0] = currentDepth;
            return true;
        } else {
            if (node.getLeft() != null && node.getRight() != null) {
                // If the current node isn't a leaf node then recursively call function to check if any child nodes
                // violate the requirements for completeness. If either sub-tree has such a violation then pass false
                // up the call chain.
                return isComplete(node.getLeft(), maxDepth, currentDepth + 1, previousDepth)
                        && isComplete(node.getRight(), maxDepth, currentDepth + 1, previousDepth);
            } else if (node.getLeft() == null) {
                // Left child should never be null if the right child isn't null
                return false;
            } else if (node.getRight() == null) {
                // Make sure any future nodes don't have a depth deeper than this node as a complete binary tree
                // should only have a single change of depths at the lowest level.
                previousDepth[0] = currentDepth;
                return true;
            } else {
                return true; // Should never actually happen - just here because the compiler doesn't like it otherwise.
            }

        }
    }
}
