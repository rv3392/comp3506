import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.*;

public class SimpleLinkedDequeTest {
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
    public void invalidConstructorNegLinkedDeque() {
        new SimpleLinkedDeque<Integer>(-2);
    }

    @Test
    public void validConstructorLinkedDequeSameType() {
        SimpleLinkedDeque<Integer> other =
                new SimpleLinkedDeque<>(linkedDeque.size(), linkedDeque);

        assertTrue(other.isFull());
    }

    @Test
    public void validConstructorLinkedDequeDifferentType() {
        SimpleLinkedDeque<Integer> other =
                new SimpleLinkedDeque<>(arrayDeque.size(), arrayDeque);

        assertTrue(other.isFull());
    }

    @Test
    public void validConstructorLinkedDequeNoCapacity() {
        SimpleLinkedDeque<Integer> other = new SimpleLinkedDeque<>(arrayDeque);
        assertEquals(2, other.size());
    }

    @Test
    public void validEmptyConstructorLinkedDeque() {
        SimpleLinkedDeque<Integer> other = new SimpleLinkedDeque<>();
        assertTrue(other.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidConstructorLinkedDequeOther() {
        new SimpleLinkedDeque<>(1, arrayDeque);
    }

    @Test
    public void sizeElementsLinkedDeque() {
        assertEquals(2, linkedDeque.size());
    }

    @Test
    public void isEmptyLinkedDeque() {
        SimpleDeque<Integer> deque = new SimpleLinkedDeque<>(20);
        assertTrue(deque.isEmpty());

        deque.pushRight(1);
        assertFalse(deque.isEmpty());
    }

    @Test
    public void isFullLinkedDeque() {
        SimpleDeque<String> deque = new SimpleLinkedDeque<>(1);
        deque.pushLeft("hello");
        assertTrue(deque.isFull());
    }

    @Test(expected = RuntimeException.class)
    public void pushLeftLinkedDequeFull() {
        SimpleDeque<String> deque = new SimpleLinkedDeque<>(1);
        deque.pushLeft("hello");
        deque.pushLeft("world");
    }

    @Test
    public void peekLeftLinkedDequeSingleElement() {
        SimpleDeque<String> deque = new SimpleLinkedDeque<>(1);
        deque.pushRight("hello");
        int priorSize = deque.size();
        assertEquals("hello", deque.peekLeft());
        assertEquals(priorSize, deque.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void peekLeftLinkedDequeEmpty() {
        SimpleLinkedDeque<Integer> deque = new SimpleLinkedDeque<>(emptyDeque);
        deque.peekLeft();
    }

    @Test(expected = NoSuchElementException.class)
    public void popLeftLinkedDequeEmpty() {
        SimpleLinkedDeque<Integer> deque = new SimpleLinkedDeque<>(emptyDeque);
        deque.popLeft();
    }

    @Test
    public void popLeftLinkedDequePushLeft() {
        SimpleLinkedDeque<String> deque = new SimpleLinkedDeque<>();
        deque.pushLeft("hello1");
        deque.pushLeft("hello2");
        assertEquals("hello2", deque.popLeft());
    }

    @Test
    public void popLeftLinkedDequePushRight() {
        SimpleLinkedDeque<String> deque = new SimpleLinkedDeque<>();
        deque.pushRight("hello1");
        deque.pushRight("hello2");
        assertEquals("hello1", deque.popLeft());
    }

    @Test
    public void popRightLinkedDequePushRight() {
        SimpleLinkedDeque<String> deque = new SimpleLinkedDeque<>();
        deque.pushRight("hello1");
        deque.pushRight("hello2");
        assertEquals("hello2", deque.popRight());
    }

    @Test
    public void popRightLinkedDequePushLeft() {
        SimpleLinkedDeque<String> deque = new SimpleLinkedDeque<>();
        deque.pushLeft("hello1");
        deque.pushLeft("hello2");
        assertEquals("hello1", deque.popRight());
    }

    @Test
    public void iteratorLinkedDeque() {
        Integer[] arr = new Integer[15];
        SimpleLinkedDeque<Integer> deque = new SimpleLinkedDeque<>();
        for (int i = 0; i < 15; i++) {
            Integer num = rand.nextInt(20);
            arr[i] = num;
            deque.pushRight(num);
        }

        Iterator<Integer> iter = deque.iterator();
        Integer[] actual = new Integer[arr.length];
        int i = 0;
        while (iter.hasNext()) {
            actual[i++] = iter.next();
        }

        assertArrayEquals(arr, actual);
    }

    @Test
    public void reverseIteratorLinkedDeque() {
        Integer[] arr = new Integer[15];
        SimpleLinkedDeque<Integer> deque = new SimpleLinkedDeque<>();
        for (int i = 0; i < 15; i++) {
            Integer num = rand.nextInt(20);
            arr[i] = num;
            deque.pushLeft(num);
        }

        Iterator<Integer> iter = deque.reverseIterator();
        Integer[] actual = new Integer[arr.length];
        int i = 0;
        while (iter.hasNext()) {
            actual[i++] = iter.next();
        }

        assertArrayEquals(arr, actual);
    }


}
