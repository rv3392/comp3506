import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyStrongHeapTest {
    BinaryTree<Integer> b;

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

    @Test
    public void test() {

        b = right(10, new BinaryTree<>(5));
        assertFalse(StrongHeap.isStrongHeap(b));

        b = right(10, new BinaryTree<>(15));
        assertFalse(StrongHeap.isStrongHeap(b));

        b = left(new BinaryTree<>(5), 10);
        assertTrue(StrongHeap.isStrongHeap(b));

        b = left(new BinaryTree<>(15), 10);
        assertFalse(StrongHeap.isStrongHeap(b));
    }

    @Test
    public void strongness() {
        b = leaf(10);

        expand(b, 5, 3);
        expand(b.getLeft(), 1, 2);
        expand(b.getRight(), 6, 6);

        assertFalse(StrongHeap.isStrongHeap(b));

        b = leaf(10);

        expand(b, 5, 3);
        expand(b.getLeft(), 4, 4);
        expand(b.getRight(), 2, 1);

        assertTrue(StrongHeap.isStrongHeap(b));

        b = leaf(100);

        expand(b, 50, 60);
        expand(b.getLeft(), 40, 30);
        expand(b.getRight(), 20, 10);

        expand(b.getLeft().getLeft(), 9, 1);
        expand(b.getLeft().getRight(), 19, 19);

        expand(b.getRight().getLeft(), 9, 2);
        expand(b.getRight().getRight(), 1, 1);

        assertTrue(StrongHeap.isStrongHeap(b));
    }

    @Test
    public void badHeaps() {
        b = leaf(100);

        expand(b, 50, 60);
        expand(b.getLeft(), 40, 30);
        expand(b.getRight(), 20, 10);

        expand(b.getLeft().getLeft(), 9, 1);
        expand(b.getLeft().getRight(), 19, 19);

        expand(b.getRight().getLeft(), 30, 2);
        expand(b.getRight().getRight(), 1, 1);

        assertFalse(StrongHeap.isStrongHeap(b));

        b = leaf(100);

        expand(b, 50, 60);
        expand(b.getLeft(), 40, 30);
        expand(b.getRight(), 20, 10);

        expand(b.getLeft().getLeft(), 9, 1);
        expand(b.getLeft().getRight(), 20, 19);

        expand(b.getRight().getLeft(), 9, 2);
        expand(b.getRight().getRight(), 1, 1);

        assertFalse(StrongHeap.isStrongHeap(b));

        b = leaf(100);

        expand(b, 50, 60);
        expand(b.getLeft(), 40, 30);
        expand(b.getRight(), 20, 10);

        expand(b.getLeft().getLeft(), 9, 1);
        expand(b.getLeft().getRight(), 19, 19);

        expand(b.getRight().getLeft(), 9, 2);
        expand(b.getRight().getRight(), 1, 11);

        assertFalse(StrongHeap.isStrongHeap(b));

        b = leaf(100);

        expand(b, 50, 60);
        expand(b.getLeft(), 40, 30);
        expand(b.getRight(), 20, 10);

        expand(b.getLeft().getLeft(), 9, 1);
        expand(b.getLeft().getRight(), 19, 19);

        expand(b.getRight().getLeft(), 9, 2);
        expand(b.getRight().getRight(), 1, 1);

        assertTrue(StrongHeap.isStrongHeap(b));
    }

    @Test
    public void badTrees() {
        b = leaf(100);

        expandRight(b, 50);

        assertFalse(StrongHeap.isStrongHeap(b));

    }

    @Test
    public void newTests() {
        Integer[] x = new Integer[] {1, null, 3, 4, 5, 6, 7, 8};
        BinaryTree<Integer> b = new BinaryTree<>(1);

        b = makeTree(x, b, 0);

        assertFalse(StrongHeap.isStrongHeap(b));
    }

    @Test
    public void bigReferenceTree() {
        Integer[] x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 3, -10, -6, 197, 198,
                49, 20, 29, 29, 0, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        BinaryTree<Integer> b =  new BinaryTree<>(1000);

        b = makeTree(x, b, 0);

        assertTrue(StrongHeap.isStrongHeap(b));

        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 3, -10, -6, 197, 198,
                49, 20, 29, 29, 150, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertFalse(StrongHeap.isStrongHeap(b));

        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 3, -10, -6, 197, 198,
                49, 20, 29, 29, 0, 180, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertFalse(StrongHeap.isStrongHeap(b));

        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 250,
                50, 70, 199, 199, -10, -6, 197, 198,
                49, 20, 29, 29, 0, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertFalse(StrongHeap.isStrongHeap(b));

        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 199, -10, -6, 197, 198,
                null, 20, 29, 29, 0, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertFalse(StrongHeap.isStrongHeap(b));
        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 199, -10, -6, 197, 198,
                49, 2, 29
        };

        b = makeTree(x, b, 0);

        assertTrue(StrongHeap.isStrongHeap(b));
        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 199, -10, -6, 197, 198,
                49, 20, 29, 29, 0, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertTrue(StrongHeap.isStrongHeap(b));
        x = new Integer[]{
                1000,
                500, 800,
                400, 300, -5, 199,
                50, 70, 199, 199, -10, -6, 197, 198,
                49, 20, 29, 29, 0, 100, 2, 2, -20, -100, -8, -11, 1, 1, 0, 0
        };

        b = makeTree(x, b, 0);

        assertTrue(StrongHeap.isStrongHeap(b));

    }
}
