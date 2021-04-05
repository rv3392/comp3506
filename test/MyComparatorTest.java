import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyComparatorTest {
    private <E> BinaryTree<E> tree(BinaryTree<E> left, E value, BinaryTree<E> right) {
        return new BinaryTree<>(value, left, right);
    }

    private <E> BinaryTree<E> left(BinaryTree<E> left, E value) {
        return new BinaryTree<>(value, left, null);
    }

    private <E> BinaryTree<E> right(E value, BinaryTree<E> right) {
        return new BinaryTree<>(value, null, right);
    }

    private <E> BinaryTree<E> leaf(E value) {
        return new BinaryTree<>(value, null, null);
    }

    private <E> void expand(BinaryTree<E> tree, E left, E right) {
        tree.setLeft(new BinaryTree<>(left));
        tree.setRight(new BinaryTree<>(right));
    }

    private <E> void expandLeft(BinaryTree<E> tree, E left) {
        tree.setLeft(new BinaryTree<>(left));
    }

    private <E> void expandRight(BinaryTree<E> tree, E right) {
        tree.setRight(new BinaryTree<>(right));
    }

    public <E> BinaryTree<E> makeTree(E[] arr, BinaryTree<E> root, int i) {
        // Base case for recursion
        if (i < arr.length && arr[i] != null) {
            root = new BinaryTree<E>(arr[i]);

            root.setLeft(makeTree(arr, root.getRight(), 2 * i + 1));

            root.setRight(makeTree(arr, root.getRight(), 2 * i + 2));

        }
        return root;
    }

    private BinaryTreeComparator<Integer> c;

    @Before
    public void setup() {
        c = new BinaryTreeComparator<>();
    }

    @Test
    public void given() {
        BinaryTree<Integer> b1 = leaf(4);
        BinaryTree<Integer> b2 = leaf(4);

        expand(b1, 5, 7);
        expand(b2, 10, 7);

        assertEquals(-1, c.compare(b1, b2));

        b1 = leaf(10);
        b2 = leaf(4);
        expand(b1, 5, 3);
        expand(b2, 5, 7);

        assertEquals(1, c.compare(b1, b2));

        b1 = leaf(10);
        b2 = leaf(1);

        expandRight(b1, 4);
        expand(b2, 2, 3);

        assertEquals(-1, c.compare(b1, b2));

    }
    @Test
    public void test() {
        BinaryTree<Integer> b1 = leaf(100);
        BinaryTree<Integer> b2 = leaf(100);

        assertEquals(0, c.compare(b1, b2));

        expandRight(b1, 50);
        expandLeft(b2, 1);

        assertEquals(-1, c.compare(b1, b2));

        expandLeft(b1, 3);

        assertEquals(1, c.compare(b1, b2));

        expandLeft(b1, -2);

        assertEquals(-1, c.compare(b1, b2));
    }

    @Test
    public void blackbox() {
        Integer[] reference = new Integer[] {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

        BinaryTree tree1 = leaf(0);
        BinaryTree tree2 = leaf(0);

        tree1 = makeTree(reference, tree1, 0);
        tree2 = makeTree(reference, tree2, 0);

        assertEquals(0, c.compare(tree1, tree2));

        Integer[] test1 = new Integer[] {
                1, 2, 3, 4, 5, 6, 7, 7, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test1, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        Integer[] test2 = new Integer[] {
                1, 2, 5, 4, 5, 6, 7, 7, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test2, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        Integer[] test3 = new Integer[] {
                1, 2, 3, 4, 6, 6, 7, 7, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test3, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        Integer[] test10 = new Integer[] {
                1, 2, 3, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test10, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));


        Integer[] test11 = new Integer[] {
                1, 2, 3, 4, 5, 7, 7, 8, 9, 10, 11, 12, 12, 14, 15, 16
        };

        tree2 = makeTree(test11, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

        Integer[] test12 = new Integer[] {
                1, 2, 3, 4, 5, 7, 7, 8, 9, 10, 11, 11, 12, 14, 15, 16
        };

        tree2 = makeTree(test12, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        /* Nulls */
        Integer[] test13 = new Integer[] {
                1, 2, 3, 4, 5, 6, 7, null, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test13, tree2, 0);

        assertEquals(-1, c.compare(tree2,tree1));

        assertEquals(0, c.compare(tree2, tree2));

        Integer[] test14 = new Integer[] {
                1, 3, null, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
        };

        tree2 = makeTree(test14, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

        test14 = new Integer[] {
                1, 2, null, 4, null, null, null, 9, 9, 10, 11, 12, 13, 14, 15
                , 16
        };

        tree2 = makeTree(test14, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

        test14 = new Integer[] {
                1, 2, null, 4, null, null, null, 9, 9, 10, 11, 12, 13, 14, 15
                , 16
        };

        tree2 = makeTree(test14, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

        /* Small cases */
        Integer[] ref = new Integer[] {1, 2, 3};
        Integer[] comp = new Integer[] {1, 2, 3};

        tree1 = makeTree(ref, tree1, 0);
        tree2 = makeTree(comp, tree2, 0);

        assertEquals(0, c.compare(tree2, tree1));

        comp = new Integer[] {1, 10, 0};

        tree2 = makeTree(comp, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

        comp = new Integer[] {0, 2, 5};

        tree2 = makeTree(comp, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        comp = new Integer[] {1, null, 10};

        tree2 = makeTree(comp, tree2, 0);

        assertEquals(-1, c.compare(tree2, tree1));

        comp = new Integer[] {1, 3, null};

        tree2 = makeTree(comp, tree2, 0);

        assertEquals(1, c.compare(tree2, tree1));

    }

}
