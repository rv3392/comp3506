import java.util.Comparator;

/**
 * A comparator for Binary Trees.
 */
public class BinaryTreeComparator<E extends Comparable<E>> implements Comparator<BinaryTree<E>> {

    /**
     * Compares two binary trees with the given root nodes.
     *
     * Two nodes are compared by their left childs, their values, then their right childs,
     * in that order. A null is less than a non-null, and equal to another null.
     *
     * The worst case time complexity for this method is O(n) where n is the size of the
     * larger of the two trees. This is because in the worst case every element in both
     * trees must be compared. The worst case memory complexity is O(1) as no extra memory
     * is required to run this method.
     *
     * @param tree1 root of the first binary tree, may be null.
     * @param tree2 root of the second binary tree, may be null.
     * @return -1, 0, +1 if tree1 is less than, equal to, or greater than tree2, respectively.
     */
    @Override
    public int compare(BinaryTree<E> tree1, BinaryTree<E> tree2) {
        // If either of the nodes given is null then the leaf of the tree has been reached
        if (tree1 == null && tree2 == null) {
            return 0;
        } else if (tree1 == null) {
            return -1;
        } else if (tree2 == null) {
            return 1;
        }

        // If all three comparisons return 0 (i.e. the two values are equal) then the subtree beginning from tree1
        // and tree2 are both equal.

        // Compare left subtree
        int leftCompare = compare(tree1.getLeft(), tree2.getLeft());
        if (leftCompare != 0) {
            return leftCompare;
        }
        // Compare values
        int valCompare = (tree1.getValue()).compareTo(tree2.getValue());
        if (valCompare != 0) {
            return valCompare > 0 ? 1 : -1;
        }
        // Compare right subtree and return the result since left and value were both equal
        int rightCompare = compare(tree1.getRight(), tree2.getRight());
        return rightCompare;
    }
}
