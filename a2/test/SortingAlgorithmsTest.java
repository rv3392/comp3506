import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class SortingAlgorithmsTest {
    private Integer[] unsorted;
    private Integer[] sorted;
    private String[] sortedAscending;

    @Before
    public void setUp() {
        unsorted = new Integer[]{5, 10, 9, 2, 1, 4, 0};
        sorted = Arrays.copyOf(unsorted, unsorted.length);
        Arrays.sort(sorted);

        sortedAscending = new String[]{"apple", "banana", "camel", "dog",
                "duck", "elephant", "fun", "giraffe", "gorilla", "hello",
                "hey", "hi", "world"};
    }

    /* --------------------------- SELECTION SORT --------------------------- */

    @Test
    public void testSelectionSortUnsorted() {
        SortingAlgorithms.selectionSort(unsorted, false);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void testSelectionSortUnsortedReverse() {
        SortingAlgorithms.selectionSort(unsorted, true);
        int j = 0;
        for (int i = unsorted.length - 1; i >= 0; i--) {
            if (!(sorted[i].equals(unsorted[j++]))) {
                fail();
            }
        }
    }

    @Test
    public void testSelectionSortSortedAscending() {
        String[] toSort = Arrays.copyOf(sortedAscending, sortedAscending.length);
        SortingAlgorithms.selectionSort(toSort, false);

        assertArrayEquals(toSort, sortedAscending);
    }

    /* --------------------------- INSERTION SORT -------------------------- */

    @Test
    public void testInsertionSortUnsorted() {
        SortingAlgorithms.insertionSort(unsorted, false);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void testInsertionSortUnsortedReverse() {
        SortingAlgorithms.insertionSort(unsorted, true);
        int j = 0;
        for (int i = unsorted.length - 1; i >= 0; i--) {
            if (!(sorted[i].equals(unsorted[j++]))) {
                fail();
            }
        }
    }

    @Test
    public void testInsertionSortSortedAscending() {
        String[] toSort = Arrays.copyOf(sortedAscending, sortedAscending.length);
        SortingAlgorithms.insertionSort(toSort, false);

        assertArrayEquals(toSort, sortedAscending);
    }

    /* -------------------------------- MERGE SORT -------------------------- */

    @Test
    public void testMergeSortUnsorted() {
        SortingAlgorithms.mergeSort(unsorted, false);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void testMergeSortUnsortedReverse() {
        SortingAlgorithms.mergeSort(unsorted, true);
        int j = 0;
        for (int i = unsorted.length - 1; i >= 0; i--) {
            if (!(sorted[i].equals(unsorted[j++]))) {
                fail();
            }
        }
    }

    @Test
    public void testMergeSortSortedAscending() {
        String[] toSort = Arrays.copyOf(sortedAscending, sortedAscending.length);
        SortingAlgorithms.mergeSort(toSort, false);

        assertArrayEquals(toSort, sortedAscending);
    }

    /* -------------------------------- QUICK SORT -------------------------- */

    @Test
    public void testQuickSortUnsorted() {
        SortingAlgorithms.quickSort(unsorted, false);
        assertArrayEquals(unsorted, sorted);
    }

    @Test
    public void testQuickSortUnsortedReverse() {
        SortingAlgorithms.quickSort(unsorted, true);
        int j = 0;
        for (int i = unsorted.length - 1; i >= 0; i--) {
            if (!(sorted[i].equals(unsorted[j++]))) {
                fail();
            }
        }
    }

    @Test
    public void testQuickSortSortedAscending() {
        String[] toSort = Arrays.copyOf(sortedAscending, sortedAscending.length);
        SortingAlgorithms.quickSort(toSort, false);

        assertArrayEquals(toSort, sortedAscending);
    }

    @Test public void insertionSortGivenNotReversed() {
        Integer[] input = {9, 2, 2, 8, 5, 7, 3, 4, 5, 1, 102};
        Integer[] sortedInput = {1, 2, 2, 3, 4, 5, 5, 7, 8, 9, 102};
        SortingAlgorithms.insertionSort(input, false);
        Assert.assertArrayEquals(input, sortedInput);
    }

    @Test public void insertionSortGivenReversed() {

    }

    @Test public void selectionSortGivenNotReversed() {
        Integer[] input = {9, 2, 2, 8, 5, 7, 3, 4, 5, 1, 102};
        Integer[] sortedInput = {1, 2, 2, 3, 4, 5, 5, 7, 8, 9, 102};
        SortingAlgorithms.selectionSort(input, false);
        Assert.assertArrayEquals(input, sortedInput);
    }

    @Test public void selectionSortGivenReversed() {

    }

    @Test
    public void mergeSortGivenNotReversed() {
        Integer[] input = {9, 2, 2, 8, 5, 7, 3, 4, 5, 1, 102};
        Integer[] sortedInput = {1, 2, 2, 3, 4, 5, 5, 7, 8, 9, 102};
        SortingAlgorithms.mergeSort(input, false);
        Assert.assertArrayEquals(input, sortedInput);
    }

    @Test
    public void mergeSortGivenReversed() {

    }

    @Test
    public void quickSortGivenNotReversed() {
        Integer[] input = {9, 2, 2, 8, 5, 7, 3, 4, 5, 1, 102};
        Integer[] sortedInput = {1, 2, 2, 3, 4, 5, 5, 7, 8, 9, 102};
        SortingAlgorithms.quickSort(input, false);
        System.out.println(Arrays.toString(input));
        Assert.assertArrayEquals(input, sortedInput);
    }

    @Test
    public void quickSortGivenReversed() {

    }
}
