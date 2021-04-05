import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class SortingAlgorithmTimer {
    @FunctionalInterface
    private interface Sorter {
        <T extends Comparable> void sort(T[] input, boolean reversed);
    }

    public static void main(String args[]) {
        int[] sizes = {5, 10, 50, 100, 500, 1000, 10000};
        Sorter[] algorithms = {SortingAlgorithms::selectionSort, SortingAlgorithms::insertionSort,
                    SortingAlgorithms::mergeSort, SortingAlgorithms::quickSort};
        String[] algorithmNames = {"Selection", "Insertion", "Merge", "Quick"};

        for (Integer size : sizes) {
            for (int i = 0; i < algorithms.length; i++) {
                Integer[] unsorted = generateArray(size);
                long timeTaken = timeAlgorithm(unsorted, algorithms[i]);
                System.out.printf("Random (%s, %d): %dms\n", algorithmNames[i], size, timeTaken);

                Integer[] ascending = generateAscendingArray(size);
                timeTaken = timeAlgorithm(ascending, algorithms[i]);
                System.out.printf("Ascending (%s, %d): %dms\n", algorithmNames[i], size, timeTaken);

                Integer[] descending = generateDescendingArray(size);
                timeTaken = timeAlgorithm(descending, algorithms[i]);
                System.out.printf("Descending (%s, %d): %dms\n", algorithmNames[i], size, timeTaken);
                
                System.out.println();
            }
        }
    }

    private static <T extends Comparable> long timeAlgorithm(Integer[] toSort, Sorter sortingAlgorithm) {
        long startTime = System.currentTimeMillis();
        sortingAlgorithm.sort(toSort, false);
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }

    private static Integer[] generateArray(int size) {
        Random randomGenerator = new Random();
        Integer[] generatedArray = new Integer[size];
        for (int i = 0; i < size; i++) {
            generatedArray[i] = randomGenerator.nextInt();
        }
        return generatedArray;
    }

    private static Integer[] generateAscendingArray(int size) {
        Integer[] generatedArray = generateArray(size);
        Arrays.sort(generatedArray);
        return generatedArray;
    }

    private static Integer[] generateDescendingArray(int size) {
        Integer[] generatedArray = generateArray(size);
        Arrays.sort(generatedArray);
        Collections.reverse(Arrays.asList(generatedArray));
        return generatedArray;
    }
}
