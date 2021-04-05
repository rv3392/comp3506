import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.*;

public class SimpleArrayDequeTest {
    private SimpleArrayDeque<Integer> arrayDeque;
    private SimpleArrayDeque<Integer> emptyDeque;
    private SimpleLinkedDeque<Integer> linkedDeque;
    private Random rand;

    @Before
    public void setUp() {
        arrayDeque = new SimpleArrayDeque<>(10);
        arrayDeque.pushLeft(5);
        arrayDeque.pushLeft(4);

        linkedDeque = new SimpleLinkedDeque<>(10);
        linkedDeque.pushRight(7);
        linkedDeque.pushRight(10);

        emptyDeque = new SimpleArrayDeque<>(5);
        rand = new Random();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidConstructorNegArrayDeque() {
        new SimpleArrayDeque<Integer>(-2);
    }

    @Test
    public void validConstructorArrayDequeSameType() {
        SimpleArrayDeque<Integer> other =
                new SimpleArrayDeque<>(arrayDeque.size(), arrayDeque);

        assertTrue(other.isFull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidConstructorArrayDequeOther() {
        new SimpleArrayDeque<>(1, arrayDeque);
    }

    @Test
    public void sizeElementsArrayDeque() {
        assertEquals(2, arrayDeque.size());
    }

    @Test
    public void isEmptyArrayDeque() {
        SimpleDeque<Integer> deque = new SimpleArrayDeque<>(20);
        assertTrue(deque.isEmpty());

        deque.pushRight(1);
        assertFalse(deque.isEmpty());
    }

    @Test
    public void isFullArrayDeque() {
        SimpleDeque<String> deque = new SimpleArrayDeque<>(1);
        deque.pushLeft("hello");
        assertTrue(deque.isFull());
    }

    @Test(expected = RuntimeException.class)
    public void pushLeftArrayDequeFull() {
        SimpleDeque<String> deque = new SimpleArrayDeque<>(1);
        deque.pushLeft("hello");
        deque.pushLeft("world");
    }

    @Test
    public void peekLeftArrayDequeSingleElement() {
        SimpleDeque<String> deque = new SimpleArrayDeque<>(1);
        deque.pushRight("hello");
        int priorSize = deque.size();
        assertEquals("hello", deque.peekLeft());
        assertEquals(priorSize, deque.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void peekLeftArrayDequeEmpty() {
        emptyDeque.peekLeft();
    }

    @Test(expected = NoSuchElementException.class)
    public void popLeftArrayDequeEmpty() {
        emptyDeque.popLeft();
    }

    @Test
    public void iteratorArrayDeque() {
        SimpleArrayDeque<Integer> deque = new SimpleArrayDeque<>(10);
        Integer[] arr = new Integer[8];
        for (int i = 0; i < 7; i++) {
            arr[i] = rand.nextInt(10);
            deque.pushRight(arr[i]);
        }

        Iterator<Integer> iter = deque.iterator();
        Integer[] arr2 = new Integer[8];
        int i = 0;
        while (iter.hasNext()) {
            arr2[i++] = iter.next();
        }

        assertArrayEquals(arr2, arr);
    }

    @Test
    public void reverseIteratorArrayDeque() {
        SimpleArrayDeque<Integer> deque = new SimpleArrayDeque<>(10);
        Integer[] arr = new Integer[8];
        for (int i = 0; i < 7; i++) {
            arr[i] = rand.nextInt(10);
            deque.pushLeft(arr[i]);
        }

        Iterator<Integer> iter = deque.reverseIterator();
        Integer[] arr2 = new Integer[8];
        int i = 0;
        while (iter.hasNext()) {
            arr2[i++] = iter.next();
        }

        assertArrayEquals(arr2, arr);
    }
}
