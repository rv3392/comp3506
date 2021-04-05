import java.util.Comparator;

public class SortingAlgorithms {
    /**
     * Sorts the given array using the selection sort algorithm.
     * This should modify the array in-place.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void selectionSort(T[] input, boolean reversed) {
        Comparator<T> sortingComparator = getComparator(reversed);

        // Iterate through each value in the array
        for (int i = 0; i < input.length; i++) {
            int minimumValueIndex = i;
            // Find the smallest input in the array after the current "i"
            for (int j = i + 1; j < input.length; j++) {
                if (sortingComparator.compare(input[j], input[minimumValueIndex]) < 0) {
                    minimumValueIndex = j;
                }
            }

            // Swap the next smallest value in the array with the currently
            // selected.
            T temp = input[i];
            input[i] = input[minimumValueIndex];
            input[minimumValueIndex] = temp;
        }
    }

    /**
     * Sorts the given array using the insertion sort algorithm.
     * This should modify the array in-place.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void insertionSort(T[] input, boolean reversed) {
        Comparator<T> sortingComparator = getComparator(reversed);

        // Iterate over each element in the array
        for (int i = 1; i < input.length; i++) {
            T selection = input[i];
            // Take the current element and move it back towards the start of
            // the array until an element that is greater than the current element
            // is found.
            for (int j = i - 1; j >= 0; j--) {
                input[j + 1] = input[j];
                if (sortingComparator.compare(selection, input[j]) >= 0) {
                    input[j + 1] = selection;
                    break;
                }

                if (j == 0) {
                    input[j] = selection;
                    break;
                }
            }

        }
    }
    
    /**
     * Sorts the given array using the merge sort algorithm.
     * This should modify the array in-place.
     * 
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void mergeSort(T[] input, boolean reversed) {
        recursiveMergeSort(input, 0, input.length, reversed);
    }

    /**
     * Recursive helper method for Merge Sort.
     *
     * Sorts the given input array between the provided leftBound and rightBound.
     *
     * @param input Array to sort
     * @param leftBound Index to start sorting from
     * @param rightBound Index to sort until
     * @param reversed reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     */
    private static <T extends Comparable> void recursiveMergeSort(
            T[] input, int leftBound, int rightBound, boolean reversed) {
        if (rightBound - leftBound == 1) {
            return;
        }
        recursiveMergeSort(input, leftBound, (leftBound + rightBound)/2, reversed);
        recursiveMergeSort(input, (leftBound + rightBound)/2, rightBound, reversed);
        merge(input, leftBound, (leftBound + rightBound)/2, rightBound, reversed);
    }

    /**
     * Merges the two halves of the array between leftBound and rightBound. After
     * merging the merged array is placed into the origin input array between
     * the leftBound and rightBound.
     *
     * @param input The array elements to be merged on
     * @param leftBound The start of the first half to merge on
     * @param middleBound The start of the second half to merge on
     * @param rightBound The end of the second half
     * @param reversed reversed If false, the array should be sorted ascending.
     *          Otherwise, it should be sorted descending.
     */
    private static <T extends Comparable> void merge(
            T[] input, int leftBound, int middleBound, int rightBound, boolean reversed) {
        int mergedLength = rightBound - leftBound;
        Object[] merged = new Object[mergedLength];

        int firstHalfCursor = leftBound;
        int secondHalfCursor = middleBound;
        int mergedCursor = 0;

        Comparator<T> sortingComparator = getComparator(reversed);
        while (firstHalfCursor < middleBound && secondHalfCursor < rightBound) {
            if (sortingComparator.compare(input[firstHalfCursor], input[secondHalfCursor]) <= 0) {
                merged[mergedCursor] = input[firstHalfCursor];
                firstHalfCursor++;
            } else {
                merged[mergedCursor] = input[secondHalfCursor];
                secondHalfCursor++;
            }

            mergedCursor++;
        }

        while (firstHalfCursor < middleBound) {
            merged[mergedCursor] = input[firstHalfCursor];
            mergedCursor++;
            firstHalfCursor++;
        }

        while (secondHalfCursor < rightBound) {
            merged[mergedCursor] = input[secondHalfCursor];
            mergedCursor++;
            secondHalfCursor++;
        }

        System.arraycopy(merged, 0, input, leftBound, mergedLength);
    }
    
    /**
     * Sorts the given array using the quick sort algorithm.
     * This should modify the array in-place.
     * 
     * You should use the value at the middle of the input  array(i.e. floor(n/2)) 
     * as the pivot at each step.
     * 
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     *                 Otherwise, it should be sorted descending.
     * @requires input != null
     */
    static <T extends Comparable> void quickSort(T[] input, boolean reversed) {
        Comparable[] sortedArray = recursiveQuickSort(input, reversed);
        System.arraycopy(sortedArray, 0, input, 0, input.length);
    }

    /**
     * Recursive helper method for Quicksort.
     *
     * Sorts the given input array using Quicksort and returns a sorted array.
     *
     * @param input An array of comparable objects.
     * @param reversed If false, the array should be sorted ascending.
     * @return Sorted array of Comparables.
     *
     * @requires input != null
     */
    private static <T extends Comparable> Comparable[] recursiveQuickSort(Comparable[] input, boolean reversed) {
        if (input.length == 1) {
            return input;
        }

        Comparable[] sortedArray = new Comparable[input.length];

        Comparable[] lesserPartition = new Comparable[0];
        Comparable[] equalPartition = new Comparable[0];
        Comparable[] greaterPartition = new Comparable[0];
        Comparable pivot = input[input.length/2];

        // Iterate through all elements of input and move elements into the lesser, equal
        // or greater arrays depending on if the elements are less than, equal to or
        // greater than the pivot.
        for (int i = 0; i < input.length; i++) {
            if (input[i].compareTo(pivot) < 0) {
                lesserPartition = extendArray(lesserPartition, input[i]);
            } else if (input[i].compareTo(pivot) == 0) {
                equalPartition = extendArray(equalPartition, input[i]);
            } else if (input[i].compareTo(pivot) > 0) {
                greaterPartition = extendArray(greaterPartition, input[i]);
            }
        }

        // Quicksort the array with elements less than the pivot and the array with
        // elements greater than the pivot.
        if (lesserPartition.length > 0) {
            lesserPartition = recursiveQuickSort(lesserPartition, reversed);
        }
        if (greaterPartition.length > 0) {
            greaterPartition = recursiveQuickSort(greaterPartition, reversed);
        }

        // Copy the 3 sorted parts of the list into a sorted array in the order lesser, equal, greater for non-reversed
        // sort and greater, equal, lesser for reversed sort.
        if (!reversed) {
            System.arraycopy(lesserPartition, 0, sortedArray, 0, lesserPartition.length);
            System.arraycopy(equalPartition, 0, sortedArray, lesserPartition.length, equalPartition.length);
            System.arraycopy(greaterPartition, 0, sortedArray,
                    lesserPartition.length + equalPartition.length, greaterPartition.length);
        } else {
            System.arraycopy(greaterPartition, 0, sortedArray, 0, greaterPartition.length);
            System.arraycopy(equalPartition, 0, sortedArray, greaterPartition.length, equalPartition.length);
            System.arraycopy(lesserPartition, 0, sortedArray,
                    greaterPartition.length + equalPartition.length, lesserPartition.length);
        }

        return sortedArray;
    }

    /**
     * Extend the given array of Comparables by creating a new array with length one
     * greater than the input array. Insert the given value into this new array.
     * @param input Array to extend
     * @param insert Value to insert in new space
     * @return Extended array
     */
    private static Comparable[] extendArray(Comparable[] input, Comparable insert) {
        Comparable[] output = new Comparable[input.length + 1];
        System.arraycopy(input, 0, output, 0, input.length);
        output[input.length] = insert;
        return output;
    }

    /**
     * Returns the correct comparator based on the required order
     *
     * Natural order comparator results in compare(0, 1) -> <0
     * Reverse order comparator results in compare(0, 1) -> >0 hence the reverse order
     * comparator can be used to move the larger values to the beginning of the list
     * rather than the smaller values resulting in a reverse sorted array.
     *
     * If reversed is true then the natural order comparator is returned,
     * otherwise the reverse order comparator is returned.
     *
     * @param reversed
     * @return If reversed is true then the natural order comparator is returned,
     *      otherwise the reverse order comparator is returned.
     */
    private static Comparator getComparator(boolean reversed) {
        Comparator sortingComparator = Comparator.naturalOrder();
        if (reversed) {
            sortingComparator = Comparator.reverseOrder();
        }

        return sortingComparator;
    }
}
