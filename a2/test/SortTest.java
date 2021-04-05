import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class SortTest {
    Integer[] arr;

    @Before
    public void setup() {
        arr = new Integer[] {5, 23, 9, 10, 50, 15, 15, 8};
    }


    @Test
    public void arrtest() {
        Integer[] test1 = new Integer[]{5, 23, 9, 10, 50, 15, 15, 8};
        Integer[] test2 = new Integer[]{1, 1, 1, 2, 1, 1, 1};
        Integer[] test3 = new Integer[]{1, 2, 3, 3, 3, 2, 2, 6};
        Integer[] test4 = new Integer[]{9, 9, 8, 8, 8, 8, 8, 8};
        Integer[] test5 = new Integer[]{1, 1, 1, 1, 1, 2, 2};
        Integer[] test6 = new Integer[]{10, 23, 9, 5, 50, 15, 15, 8};
        Integer[] test7 = new Integer[]{10, 23, 9, 5, 50, 15, 15, 15};
        Integer[] test8 = new Integer[]{23, 9, 5, 5, 10, 8, 9, 7};
        Integer[] test9 = new Integer[]{3, 2, 1, 2, 3, 4, 5, 6, 7};
        Integer[] test10 = new Integer[]{50, 40, 60, 1, 2, 3, 100, 200,
                300, 45, 55};
        Integer[] test11 = new Integer[]{5, 4, 1, 1};
        Integer[] test12 = new Integer[]{1, 3, 5, 4, 3, 2, 2, 3, 5};
        testAll(test1);
        testAll(test2);
        testAll(test3);
        testAll(test4);
        testAll(test5);
        testAll(test6);
        testAll(test7);
        testAll(test8);
        testAll(test9);
        testAll(test10);
        testAll(test11);
        testAll(test12);

        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        for (Integer a1 : nums) {
        for (Integer a2 : nums) {
        for (Integer a3 : nums) {
        for (Integer a4 : nums) {
        for (Integer a5 : nums) {
        for (Integer a6 : nums) {
            Integer[] sample = new Integer[] {a1, a2, a3, a4, a5, a6};
            testAll(sample);
        }}}}}}
        nums = new Integer[]{1, 2, 3, 4, 5, 6};
        for (Integer a1 : nums) {
        for (Integer a2 : nums) {
        for (Integer a3 : nums) {
        for (Integer a4 : nums) {
        for (Integer a5 : nums) {
        for (Integer a6 : nums) {
            Integer[] sample = new Integer[] {a1, a2,
            a3, a4, a5, a6};
            testAll(sample);
        }}}}}}
    }

    private void testAll(Integer[] arr) {

        Integer[] comparison = Arrays.copyOf(arr, arr.length);
        Arrays.sort(comparison);

        Integer[] sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.selectionSort(sample, false);
        assertArrayEquals(comparison, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.insertionSort(sample, false);
        assertArrayEquals(comparison, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.mergeSort(sample, false);
        assertArrayEquals(comparison, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.quickSort(sample, false);
        assertArrayEquals(comparison, sample);

        Integer[] comparisonReversed = Arrays.copyOf(arr, arr.length);
        Arrays.sort(comparisonReversed, Comparator.reverseOrder());


        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.selectionSort(sample, true);
        assertArrayEquals(comparisonReversed, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.insertionSort(sample, true);
        assertArrayEquals(comparisonReversed, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.mergeSort(sample, true);
        assertArrayEquals(comparisonReversed, sample);

        sample = Arrays.copyOf(arr, arr.length);
        SortingAlgorithms.quickSort(sample, true);
        assertArrayEquals(comparisonReversed, sample);


    }

    @Test
    public void bruteForce() {
        for (int i = 0; i < 1000; i++) {
            Random random = new Random();
            int[] x = random.ints(i).toArray();
            Integer[] y = new Integer[x.length];
            for (int j = 0; j < y.length; j++) {
                y[j] = Integer.valueOf(x[j]);
            }
            Integer[] c = new Integer[y.length];
            for (int j = 0; j < y.length; j++) {
                c[j] = Integer.valueOf(x[j]);
            }
            Arrays.sort(c);

            SortingAlgorithms.selectionSort(y, false);
            assertArrayEquals(y, c);
        }
        for (int i = 1; i < 100; i++) {
            Random random = new Random();
            int[] x = random.ints(i).toArray();
            Integer[] y = new Integer[x.length];
            for (int j = 0; j < y.length; j++) {
                y[j] = Integer.valueOf(x[j]);
            }
            Integer[] c = new Integer[y.length];
            for (int j = 0; j < y.length; j++) {
                c[j] = y[j];
            }
            Arrays.sort(c);

            SortingAlgorithms.insertionSort(y, false);
            assertArrayEquals(y, c);
        }
        for (int i = 1; i < 100; i++) {
            Random random = new Random();
            int[] x = random.ints(i).toArray();
            Integer[] y = new Integer[x.length];
            for (int j = 0; j < y.length; j++) {
                y[j] = Integer.valueOf(x[j]);
            }
            Integer[] c = new Integer[y.length];
            for (int j = 0; j < y.length; j++) {
                c[j] = y[j];
            }
            Arrays.sort(c);

            SortingAlgorithms.mergeSort(y, false);
            assertArrayEquals(y, c);
        }
        for (int i = 1; i < 100; i++) {
            Random random = new Random();
            int[] x = random.ints(i).toArray();
            Integer[] y = new Integer[x.length];
            for (int j = 0; j < y.length; j++) {
                y[j] = Integer.valueOf(x[j]);
            }
            Integer[] c = new Integer[y.length];
            for (int j = 0; j < y.length; j++) {
                c[j] = y[j];
            }
            Arrays.sort(c);

            SortingAlgorithms.quickSort(y, false);
            assertArrayEquals(y, c);
        }
    }



}
