import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;

public class MyHeapSortTest {


    private void testSort(Integer[] heap) {
        Integer[] comparison = Arrays.copyOf(heap, heap.length);
        Arrays.sort(comparison);

        QuaternaryHeapsort.quaternaryHeapsort(heap);
        assertArrayEquals(heap, comparison);

    }

    @Test
    public void givendownheaps() {
        Integer[] x = new Integer[] { 0,10,20,30,40};
        QuaternaryHeapsort.quaternaryDownheap(x, 0, 5 );
        assertArrayEquals(new Integer[] {40,10,20,30,0}, x);

        x = new Integer[] {1,0,2,3,4,10,20,30,40};
        QuaternaryHeapsort.quaternaryDownheap(x, 1, 9 );
        assertArrayEquals(new Integer[] {1,40,2,3,4,10,20,30,0}, x);

        x = new Integer[] {10,20,1,2,3,11,12,13,14};
        QuaternaryHeapsort.quaternaryDownheap(x, 0, 9 );
        assertArrayEquals(new Integer[] {20,14,1,2,3,11,12,13,10}, x);

        x = new Integer[] {10,20,1,2,3,11,12,13,14};
        QuaternaryHeapsort.quaternaryDownheap(x, 0, 5 );
        assertArrayEquals(new Integer[] {20,10,1,2,3,11,12,13,14}, x);
    }

    @Test
    public void specific() {
        Integer[] nums = new Integer[] {3, 1, 3, 4, 7, 8, 9};

        testSort(nums);
    }

    @Test
    public void sort() {

        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        for (Integer a1 : nums) {
            for (Integer a2 : nums) {
                for (Integer a3 : nums) {
                    for (Integer a4 : nums) {
                        for (Integer a5 : nums) {
                            for (Integer a6 : nums) {
                                for (Integer a7 : nums) {
                                    Integer[] sample = new Integer[]{a1, a2, a3, a4, a5, a6, a7};
                                    testSort(sample);
                                }
                            }
                        }
                    }
                }
            }
        }

        nums = new Integer[]{1, 2, 3, 4, 5, 6};
        for (Integer a1 : nums) {
            for (Integer a2 : nums) {
                for (Integer a3 : nums) {
                    for (Integer a4 : nums) {
                        for (Integer a5 : nums) {
                            for (Integer a6 : nums) {
                                Integer[] sample = new Integer[]{a1, a2, a3, a4, a5, a6};
                                testSort(sample);
                            }
                        }
                    }
                }
            }
        }
    }

//    @Test
//    public void bruteSmallMany() {
//        Random random = new Random();
//        for (int i = 0; i < 3000; i++) {
//            Integer[] ints =
//                    random.ints(i, 0, 600).boxed().toArray(Integer[]::new);
//            testSort(ints);
//            System.out.println(i);
//        }
//
//    }
    @Test
    public void brute() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Integer[] ints = random.ints(i).boxed().toArray(Integer[]::new);
            testSort(ints);
        }
    }

    @Test
    public void bruteSmall() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Integer[] ints =
                    random.ints(i, 0, 100).boxed().toArray(Integer[]::new);
            testSort(ints);
        }

    }

    //@Test
//    public void stupid() {
//        Integer[] nums = {1, 2, 3, 4, 5, 6, 7, 8};
//        for (Integer a1 : nums) {
//    for (Integer a2 : nums) {
//    for (Integer a3 : nums) {
//    for (Integer a4 : nums) {
//    for (Integer a5 : nums) {
//    for (Integer a6 : nums) {
//    for (Integer a7 : nums) {
//    for (Integer a8 : nums) {
//    Integer[] sample = new Integer[] {a1,a2,a3,a4,a5,a6,a7,a8};
//    testSort(sample);
//}}}}}}}}}
}

