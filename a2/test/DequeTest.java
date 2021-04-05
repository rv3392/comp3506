import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DequeTest {

    @Test
    public void CapTest() {
        SimpleArrayDeque<Integer> x = new SimpleArrayDeque<>(4);
        SimpleLinkedDeque<Integer> y = new SimpleLinkedDeque<>(4);
        cap(x);
        cap(y);
    }

    private void cap(SimpleDeque d) {
        assertTrue(d.isEmpty());
        d.pushLeft(1);
        d.pushLeft(2);
        d.pushLeft(3);
        d.pushRight(4);
        assertTrue(d.isFull());
        try {
            d.pushLeft(10);
            fail();
        } catch (RuntimeException e) {}

        try {
            d.pushRight(10);
            fail();
        } catch (RuntimeException e) {}

        assertEquals(3, d.peekLeft());
        assertEquals(3, d.popLeft());
        assertFalse(d.isFull());
        assertFalse(d.isEmpty());

        assertEquals(4, d.popRight());
        assertEquals(1, d.popRight());
        assertEquals(2, d.popRight());
        assertTrue(d.isEmpty());
        try {
            d.popRight();
            fail();
        } catch (NoSuchElementException e) {}

        d.pushLeft(30);
        assertEquals(30, d.peekRight());
        assertEquals(30, d.popRight());

        try {
            d.popLeft();
            fail();
        } catch (NoSuchElementException e) {}

        d.pushLeft(10);
        d.pushRight(20);
        d.pushRight(30);
        d.pushLeft(40);
        try {
            d.pushLeft(50);
            fail();
        } catch (RuntimeException e) {}

        try {
            d.pushRight(50);
            fail();
        } catch (RuntimeException e) {}

        assertEquals(30, d.popRight());
        assertEquals(40, d.popLeft());
        assertEquals(10, d.popLeft());
        assertEquals(20, d.popLeft());

    }

    @Test
    public void DequeTest() {
        SimpleArrayDeque<Integer> x = new SimpleArrayDeque<>(20);
        SimpleLinkedDeque<Integer> y = new SimpleLinkedDeque<>();
        testDeque1(x);
        testDeque1(y);
        testDeque2(x);
        testDeque2(y);
        testDeque3(x);
        testDeque3(y);
    }

    private void testDeque1(SimpleDeque d) {
        d.pushLeft(1);
        d.pushLeft(2);
        d.pushLeft(3);
        d.pushLeft(4);
        assertEquals(4, d.peekLeft());
        assertEquals(4, d.popLeft());
        assertEquals(3, d.peekLeft());
        assertEquals(3, d.popLeft());
        assertEquals(2, d.peekLeft());
        assertEquals(2, d.popLeft());
        assertEquals(1, d.peekLeft());
        assertEquals(1, d.popLeft());
        try {
            d.peekLeft();
            d.popLeft(); fail();
        } catch (NoSuchElementException e) {}
    }

    private void testDeque2(SimpleDeque d) {
        d.pushLeft(1);
        d.pushLeft(2);
        d.pushRight(0);
        assertEquals(0, d.popRight());
        assertEquals(1, d.popRight());
        assertEquals(2, d.popRight());

    }

    private void testDeque3(SimpleDeque d) {
        d.pushLeft(0);
        d.pushRight(1);
        d.pushLeft(2);
        d.pushRight(3);
        d.pushLeft(4);
        d.pushRight(5);
        d.pushLeft(6);
        d.pushRight(7);
        d.pushLeft(8);
        d.pushRight(9);

        assertEquals(8, d.popLeft());
        assertEquals(6, d.popLeft());
        assertEquals(9, d.popRight());
        assertEquals(7, d.popRight());
        assertEquals(4, d.popLeft());
        assertEquals(2, d.popLeft());
        assertEquals(0, d.popLeft());
        assertEquals(1, d.popLeft());
        assertEquals(3, d.popLeft());
        assertEquals(5, d.popRight());
        assertTrue(d.isEmpty());

        d.pushLeft(10);
        d.pushRight(20);
        assertEquals(10, d.popLeft());
        assertEquals(20, d.peekLeft());
        assertEquals(20, d.popLeft());
    }

    @Test
    public void iteratorTest() {
        SimpleArrayDeque<Integer> x = new SimpleArrayDeque<>(20);
        SimpleLinkedDeque<Integer> y = new SimpleLinkedDeque<>();
        iter1(x);
        iter1(y);
    }

    private void iter1(SimpleDeque d) {
        d.pushLeft(10);
        d.pushRight(20);
        d.pushRight(30);
        d.pushLeft(40);
        d.pushRight(50);
        d.pushLeft(60);

        Iterator i = d.iterator();
        assertEquals(60, i.next());
        assertEquals(40, i.next());
        assertEquals(10, i.next());
        assertEquals(20, i.next());
        assertEquals(30, i.next());
        assertEquals(50, i.next());
        try {
            i.next();
            fail();
        } catch (NoSuchElementException e) {}

        Iterator i2 = d.reverseIterator();
        assertEquals(50, i2.next());
        assertEquals(30, i2.next());
        assertEquals(20, i2.next());
        assertEquals(10, i2.next());
        assertEquals(40, i2.next());
        assertEquals(60, i2.next());
        try {
            i.next();
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    public void reversibleTest() {
        SimpleArrayDeque<Integer> x = new SimpleArrayDeque<>(20);
        SimpleLinkedDeque<Integer> y = new SimpleLinkedDeque<>();

        reverse(x);
        reverse(y);
        reverseTwice(x);
        reverseTwice(y);
    }

    private void reverse(SimpleDeque e) {
        ReversibleDeque d = new ReversibleDeque(e);

        d.pushRight(1);
        d.pushRight(2);
        d.pushLeft(3);
        d.pushLeft(4);
        d.pushRight(5);
        d.pushLeft(6);

        assertEquals(5, d.popRight());
        assertEquals(2, d.popRight());
        assertEquals(1, d.popRight());
        assertEquals(3, d.popRight());
        assertEquals(6, d.popLeft());
        assertEquals(4, d.popLeft());

        assertTrue(d.isEmpty());

        d.pushRight(1);
        d.pushRight(2);
        d.pushLeft(3);
        d.pushLeft(4);
        d.pushRight(5);
        d.pushLeft(6);

        d.reverse();

        assertEquals(5, d.popLeft());
        assertEquals(2, d.popLeft());
        assertEquals(1, d.popLeft());
        assertEquals(3, d.popLeft());
        assertEquals(6, d.popRight());
        assertEquals(4, d.popRight());

        assertTrue(d.isEmpty());

    }

    private void reverseTwice(SimpleDeque e) {
        ReversibleDeque d = new ReversibleDeque(e);

        d.reverse();
        d.reverse();

        d.pushRight(1);
        d.pushRight(2);
        d.pushLeft(3);
        d.pushLeft(4);
        d.pushRight(5);
        d.pushLeft(6);

        assertEquals(5, d.popRight());
        assertEquals(2, d.popRight());
        assertEquals(1, d.popRight());
        assertEquals(3, d.popRight());
        assertEquals(6, d.popLeft());
        assertEquals(4, d.popLeft());

        assertTrue(d.isEmpty());

        d.pushRight(1);
        d.pushRight(2);
        d.pushLeft(3);
        d.pushLeft(4);
        d.pushRight(5);
        d.pushLeft(6);

        d.reverse();

        assertEquals(5, d.popLeft());
        assertEquals(2, d.popLeft());
        assertEquals(1, d.popLeft());
        assertEquals(3, d.popLeft());
        assertEquals(6, d.popRight());
        assertEquals(4, d.popRight());

        assertTrue(d.isEmpty());

    }
}
