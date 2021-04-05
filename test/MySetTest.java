import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MySetTest {
    private class BadInteger {
        int value;
        int hash;

        private BadInteger(int value, int hash) {
            this.value = value;
            this.hash = hash;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            } else if (other.getClass() != this.getClass()) {
                return false;
            }

            BadInteger i = (BadInteger) other;

            return i.value == this.value;
        }

        @Override
        public String toString() {
            return String.format("%d", value);
        }
    }

    @Test
    public void testExceptions() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);
        BadInteger i1 = new BadInteger(100, 6);
        BadInteger i2 = new BadInteger(200, 7);
        BadInteger i3 = new BadInteger(300, 8);
        BadInteger i4 = new BadInteger(400, 9);
        BadInteger i5 = new BadInteger(500, 10);
        BadInteger i6 = new BadInteger(500, 10);

        s.add(i1);
        try {
            s.remove(i1, 2);
            fail();
        } catch (NoSuchElementException e) {}
        s.remove(i1);


    }

    @Test
    public void insane() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(2);
        BadInteger i1 = new BadInteger(100, 1);
        BadInteger i2 = new BadInteger(200, 2);
        BadInteger i3 = new BadInteger(300, 3);
        BadInteger i4 = new BadInteger(400, 3);
        BadInteger i5 = new BadInteger(500, 2);
        BadInteger i6 = new BadInteger(600, 1);
        BadInteger i7 = new BadInteger(700, 3);
        BadInteger i8 = new BadInteger(800, 8);
        BadInteger i9 = new BadInteger(900, 8);

        s.add(i1);
        s.add(i2);
        s.add(i9);
        s.add(i8);
        s.add(i3);
        s.add(i2);
        s.add(i5);
        s.add(i3);
        s.add(i4);
        s.add(i6);
        s.add(i7);
        s.add(i9);
        s.add(i9);
        s.add(i8);
        s.add(i7);
        s.add(i7);
        s.add(i4);
        s.add(i9);
        s.add(i1);
        s.add(i3);
        s.add(i4);
        s.add(i4);
        s.add(i2);
        s.add(i4);
        s.add(i8);
        s.add(i9);
        s.add(i9);

        assertEquals(2, s.count(i1));
        assertEquals(3, s.count(i2));
        assertEquals(3, s.count(i3));
        assertEquals(5, s.count(i4));
        assertEquals(1, s.count(i5));
        assertEquals(1, s.count(i6));
        assertEquals(3, s.count(i7));
        assertEquals(3, s.count(i8));
        assertEquals(6, s.count(i9));

    }

    @Test
    public void test1() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);

        assertEquals(5, s.internalCapacity());

        BadInteger i1 = new BadInteger(100, 6);
        BadInteger i2 = new BadInteger(200, 7);
        BadInteger i3 = new BadInteger(300, 8);
        BadInteger i4 = new BadInteger(400, 9);
        BadInteger i5 = new BadInteger(500, 10);
        BadInteger i6 = new BadInteger(500, 10);

        s.add(i1);
        s.add(i2);
        s.add(i3);
        s.add(i4);
        s.add(i5);
        s.add(i6);

        assertEquals(6, s.size());
        assertEquals(5, s.distinctCount());

        assertEquals(2, s.count(i5));
        assertEquals(2, s.count(i6));

        assertEquals(10, s.internalCapacity());

        /* Collision */
        BadInteger i7 = new BadInteger(600, 10);

        s.add(i7);
        assertEquals(7, s.size());
        assertEquals(6, s.distinctCount());

        assertEquals(10, s.internalCapacity());
    }

    @Test
    public void edgeIndices() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);

        assertEquals(5, s.internalCapacity());

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger n1 = new BadInteger(1000, 7);


        s.add(i1);
        s.add(i1);
        s.add(i2);
        s.add(i3);
        s.add(i4);
        s.add(i5);
        s.add(i1);

        assertEquals(5, s.distinctCount());
        assertEquals(7, s.size());

        assertEquals(3, s.count(i1));
        assertEquals(1, s.count(i2));
        assertEquals(1, s.count(i3));
        assertEquals(1, s.count(i4));
        assertEquals(1, s.count(i5));

        try {
            s.remove(n1);
            fail();
        } catch (NoSuchElementException e) {}

        try {
            s.remove(i1, 4);
            fail();
        } catch (NoSuchElementException e) {}

        try {
            s.remove(i2, 2);
            fail();
        } catch (NoSuchElementException e) {}

        s.remove(i1, 2);

        assertEquals(5, s.size());
        assertEquals(5, s.distinctCount());

        s.remove(i2);

        assertEquals(4, s.size());
        assertEquals(4, s.distinctCount());

    }

    @Test
    public void edgeIndicesOneCap() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(1);

        assertEquals(1, s.internalCapacity());

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger n1 = new BadInteger(1000, 7);


        s.add(i1);
        s.add(i1);
        s.add(i2);
        s.add(i3);
        s.add(i4);
        s.add(i5);
        s.add(i1);

        assertEquals(5, s.distinctCount());
        assertEquals(7, s.size());

        assertEquals(3, s.count(i1));
        assertEquals(1, s.count(i2));
        assertEquals(1, s.count(i3));
        assertEquals(1, s.count(i4));
        assertEquals(1, s.count(i5));

        try {
            s.remove(n1);
            fail();
        } catch (NoSuchElementException e) {}

        try {
            s.remove(i1, 4);
            fail();
        } catch (NoSuchElementException e) {}

        try {
            s.remove(i2, 2);
            fail();
        } catch (NoSuchElementException e) {}

        s.remove(i1, 2);

        assertEquals(5, s.size());
        assertEquals(5, s.distinctCount());

        s.remove(i2);

        assertEquals(4, s.size());
        assertEquals(4, s.distinctCount());

    }

    @Test
    public void removeTest() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);
        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 3);

        s.add(i1);
        s.add(i2);
        s.add(i1);
        s.add(i2);

        s.remove(i1);
        assertEquals(1, s.count(i1));
        assertEquals(2, s.count(i2));
        s.remove(i2, 2);
        assertEquals(0, s.count(i2));


    }

    @Test
    public void iterTest() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger i6 = new BadInteger(600, 3);


        s.add(i3);
        s.add(i5);
        s.add(i6);
        s.add(i4);
        s.add(i5);
        s.add(i2);
        s.add(i1);

        Iterator<BadInteger> x = s.iterator();

        assertEquals(i3, x.next());
        assertEquals(i5, x.next());
        assertEquals(i5, x.next());
        assertEquals(i6, x.next());
        assertEquals(i4, x.next());
        assertEquals(i2, x.next());
        assertEquals(i1, x.next());

        assertFalse(x.hasNext());


    }

    @Test
    public void iterTestRemove() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(5);

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger i6 = new BadInteger(600, 3);


        s.add(i3);
        s.add(i5);
        s.add(i6);
        s.add(i4);
        s.add(i5);
        s.add(i2);
        s.add(i1);
        s.add(i3);
        s.add(i4, 5);
        s.add(i3);

        s.remove(i3);
        s.remove(i5, 2);

        s.add(i3);
        s.add(i5);

        Iterator<BadInteger> x = s.iterator();

        assertEquals(i3, x.next());
        assertEquals(i3, x.next());
        assertEquals(i3, x.next());
        assertEquals(i6, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i2, x.next());
        assertEquals(i1, x.next());
        assertEquals(i5, x.next());

        assertFalse(x.hasNext());

    }

    @Test
    public void iterTestOneCap() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(1);

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger i6 = new BadInteger(600, 3);


        s.add(i3);
        s.add(i5);
        s.add(i6);
        s.add(i4);
        s.add(i5);
        s.add(i2);
        s.add(i1);
        s.add(i3);
        s.add(i4, 5);
        s.add(i3);

        s.remove(i3);
        s.remove(i5, 2);

        s.add(i3);
        s.add(i5);

        Iterator<BadInteger> x = s.iterator();

        assertEquals(i3, x.next());
        assertEquals(i3, x.next());
        assertEquals(i3, x.next());
        assertEquals(i6, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i4, x.next());
        assertEquals(i2, x.next());
        assertEquals(i1, x.next());
        assertEquals(i5, x.next());

        assertFalse(x.hasNext());

    }

    @Test
    public void reAdd() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(3);

        BadInteger i1 = new BadInteger(100, 3);
        BadInteger i2 = new BadInteger(200, 4);
        BadInteger i3 = new BadInteger(300, 0);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 3);
        BadInteger i6 = new BadInteger(600, 3);

        assertEquals(3, s.internalCapacity());
        assertEquals(0, s.distinctCount());
        assertEquals(0, s.size());

        s.add(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(1, s.size());

        s.add(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.add(i1, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(5, s.size());

        s.remove(i1, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.remove(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(2, s.size());

        s.remove(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(0, s.distinctCount());
        assertEquals(0, s.size());

        s.add(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(1, s.size());

        s.add(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.add(i3);

        assertEquals(6, s.internalCapacity());
        assertEquals(3, s.distinctCount());
        assertEquals(4, s.size());

    }
    @Test
    public void reAddButEverythingHasTheSameHash() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(3);

        BadInteger i1 = new BadInteger(100, 1);
        BadInteger i2 = new BadInteger(200, 1);
        BadInteger i3 = new BadInteger(300, 1);
        BadInteger i4 = new BadInteger(400, 1);
        BadInteger i5 = new BadInteger(500, 1);
        BadInteger i6 = new BadInteger(600, 1);

        assertEquals(3, s.internalCapacity());
        assertEquals(0, s.distinctCount());
        assertEquals(0, s.size());

        s.add(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(1, s.size());

        s.add(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.add(i1, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(5, s.size());

        s.remove(i1, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.remove(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(2, s.size());

        s.remove(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(0, s.distinctCount());
        assertEquals(0, s.size());

        s.add(i1);

        assertEquals(3, s.internalCapacity());
        assertEquals(1, s.distinctCount());
        assertEquals(1, s.size());

        s.add(i2, 2);

        assertEquals(3, s.internalCapacity());
        assertEquals(2, s.distinctCount());
        assertEquals(3, s.size());

        s.add(i3);

        assertEquals(6, s.internalCapacity());
        assertEquals(3, s.distinctCount());
        assertEquals(4, s.size());
    }

    private void verify(LinkedMultiHashSet s, int internalcap, int distinct,
            int size) {
        assertEquals(internalcap, s.internalCapacity());
        assertEquals(distinct, s.distinctCount());
        assertEquals(size, s.size());
    }
    @Test
    public void weird() {
        LinkedMultiHashSet<BadInteger> s = new LinkedMultiHashSet<>(4);

        BadInteger i1 = new BadInteger(100, 1);
        BadInteger i2 = new BadInteger(200, 2);
        BadInteger i3 = new BadInteger(300, 1);
        BadInteger i4 = new BadInteger(400, 5);
        BadInteger i5 = new BadInteger(500, 5);
        BadInteger i6 = new BadInteger(100, 1);
        BadInteger i7 = new BadInteger(10000, 3);

        assertEquals(0, s.count(i7)); // XD
        verify(s, 4, 0, 0);

        s.add(i1, 4);
        s.add(i3);
        s.add(i2);
        s.add(i3);
        s.add(i1);
        s.add(i3);
        s.remove(i1, 3);
        s.add(i2);
        s.remove(i2, 2);
        try {
            s.remove(i2);
            fail();
        } catch (NoSuchElementException e) {}

        assertEquals(0, s.count(i7)); // XD
        try {
            s.remove(i1, 3);
            fail();
        } catch (NoSuchElementException e) {}

        s.remove(i1, 2);
        assertFalse(s.contains(i1));

        verify(s, 4, 1, 3);
        assertEquals(0, s.count(i7)); // XD

        Iterator it = s.iterator();

        for (int i = 0; i < 3; i++)
            assertEquals(i3, it.next());

        assertFalse(it.hasNext());

        s.add(i4, 3);
        s.add(i6, 4);
        s.add(i4);

        verify(s, 4, 3, 11);
        assertEquals(0, s.count(i7)); // XD

        it = s.iterator();
        assertEquals(0, s.count(i7)); // XD

        for (int i = 0; i < 3; i++)
            assertEquals(i3, it.next());
        for (int i = 0; i < 4; i++)
            assertEquals(i4, it.next());
        for (int i = 0; i < 4; i++)
            assertEquals(i6, it.next());

        s.add(i5);
        s.add(i6);

        verify(s, 8, 4, 13);

        s.remove(i4, 4);

        verify(s, 8, 3, 9);
        assertEquals(0, s.count(i7)); // XD

        it = s.iterator();
        for (int i = 0; i < 3; i++)
            assertEquals(i3, it.next());
        for (int i = 0; i < 5; i++)
            assertEquals(i6, it.next());
        assertEquals(i5, it.next());

        s.add(i4, 2);
        assertEquals(0, s.count(i7)); // XD

        it = s.iterator();

        for (int i = 0; i < 3; i++)
            assertEquals(i3, it.next());
        for (int i = 0; i < 5; i++)
            assertEquals(i6, it.next());
        assertEquals(i5, it.next());
        for (int i = 0; i < 2; i++)
            assertEquals(i4, it.next());

        verify(s, 8, 4, 11);
        assertEquals(0, s.count(i7)); // XD

        s.remove(i4, 2);
        s.remove(i3, 3);
        s.remove(i5);
        s.remove(i6, 5);

        verify(s, 8, 0, 0);

        s.add(i6, 2);
        s.add(i5);
        s.add(i6);
        s.add(i5);
        s.add(i4);
        s.remove(i5, 2);
        s.add(i3);
        s.add(i5);

        verify(s, 8, 4, 6);
        assertEquals(0, s.count(i7)); // XD

        it = s.iterator();

        for (int i = 0; i < 3; i++)
            assertEquals(i6, it.next());
        assertEquals(i4, it.next());
        assertEquals(i3, it.next());
        assertEquals(i5, it.next());

        assertEquals(0, s.count(i7)); // XD

    }

}


















