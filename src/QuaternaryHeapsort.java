import java.lang.Math;

public class QuaternaryHeapsort {

    /**
     * Sorts the input array, in-place, using a quaternary heap sort.
     *
     * @param input to be sorted (modified in place)
     */
    public static <T extends Comparable<T>> void quaternaryHeapsort(T[] input) {
        // TODO: implement question 1 here
    }

    /**
     * Performs a downheap from the element in the given position on the given max heap array.
     *
     * A downheap should restore the heap order by swapping downwards as necessary.
     * The array should be modified in place.
     *
     * You should only consider elements in the input array from index 0 to index (size - 1)
     * as part of the heap (i.e. pretend the input array stops after the inputted size).
     *
     * @param input array representing a quaternary max heap.
     * @param start position in the array to start the downheap from.
     * @param size the size of the heap in the input array, starting from index 0
     */
    public static <T extends Comparable<T>> void quaternaryDownheap(T[] input, int start, int size) {
        int depth = getDepth(start);
        int i = start;
        int largestChildIndex = getLargestChildIndex(input, getFirstChildIndex(i, depth++), size);
        while (i < size && largestChildIndex != -1 && input[largestChildIndex].compareTo(input[i]) > 0) {
            swap(input, i, largestChildIndex);
            i = largestChildIndex;
            largestChildIndex = getLargestChildIndex(input, getFirstChildIndex(i, depth++), size);
        }
    }

    /**
     * Use the general formula to calculate the depth of a tree to calculate
     * the depth of a particular index within the tree.
     *
     * @param index Node to calculate the depth of
     * @return Depth of the given index in the quaternary heap
     */
    private static int getDepth(int index) {
        return (int) Math.floor(Math.log10(3 * index + 1) / Math.log10(4));
    }

    /**
     * Get the index of the first child of the node at the provided index and
     * depth in the quaternary heap.
     *
     * @param index Index of the node for which to find the first child
     * @param depth Depth at which the node is located
     * @return The index of the first child of the given node
     */
    private static int getFirstChildIndex(int index, int depth) {
        int nodesInCurrentDepth = (int) Math.pow(4, depth);
        int nodesUptoDepth = (nodesInCurrentDepth - 1) / 3;
        int indexInDepth = index - nodesUptoDepth;
        return index + nodesInCurrentDepth + 3 * (indexInDepth);
    }

    /**
     * Scans the children of a node in input and returns the index of the
     * largest.
     *
     * The scanning starts at the given firstChild and continues until all
     * of the children of the node have been compared. This may be before
     * 4 comparisons are made as a node could have less than 4 children.
     *
     * @param input Array representing the quaternary heap
     * @param firstChild The index of the first child of the node
     *
     * @return The index of the largest child of the firstChild's parent
     */
    private static <T extends Comparable<T>> int getLargestChildIndex(T[] input, int firstChild, int size) {
        if (firstChild > size - 1) {
            return -1;
        }

        int maxIndex = size - firstChild;
        if (maxIndex > 4) {
            maxIndex = 4;
        }

        T largestChild = input[firstChild];
        int largestChildIndex = firstChild;
        for (int i = 1; i < maxIndex; i++) {
            if (input[i + firstChild].compareTo(largestChild) > 0) {
                largestChild = input[i + firstChild];
                largestChildIndex = i + firstChild;
            }
        }

        return largestChildIndex;
    }

    /**
     * Swap the element in index1 with the element in index2 within the given
     * array input.
     *
     * @param input Array to perform the swap on
     * @param index1 First index to swap
     * @param index2 Second index to swap
     */
    private static <T extends Comparable<T>> void swap(T[] input, int index1, int index2) {
        T temp = input[index1];
        input[index1] = input[index2];
        input[index2] = temp;
    }


}
