import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;

public class LinkedMultiHashSetTest {
    
    @Test
    public void testBasicUsage() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(5);
        
        assertEquals(5, set.internalCapacity());
        
        set.add(5);
        assertEquals(1, set.count(5));
        assertEquals(1, set.size());
        assertTrue(set.contains(5));
        assertEquals(5, set.internalCapacity());

        set.add(5);
        assertEquals(2, set.count(5));
        assertEquals(2, set.size());
        assertTrue(set.contains(5));
        assertEquals(5, set.internalCapacity());
        
        set.add(3);
        assertEquals(2, set.count(5));
        assertEquals(1, set.count(3));
        assertEquals(3, set.size());
        assertTrue(set.contains(5));
        assertTrue(set.contains(3));
        assertEquals(5, set.internalCapacity());
    }
    
    @Test
    public void testRemovals() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(5);

        assertEquals(5, set.internalCapacity());

        set.add(5);
        assertEquals(1, set.count(5));
        assertEquals(1, set.size());
        assertTrue(set.contains(5));
        
        set.remove(5);
        assertEquals(0, set.count(5));
        assertEquals(0, set.size());
        assertFalse(set.contains(5));
        
        try {
            set.remove(5);
            fail();
        } catch (NoSuchElementException e) {
            // all good :)
        }
        
        set.add(5, 2);
        assertEquals(2, set.count(5));
        assertEquals(2, set.size());
        
        try {
            set.remove(5, 3);
            fail();
        } catch (NoSuchElementException e) {
            // all good :)
        }
        
        assertEquals(2, set.count(5));
        assertEquals(2, set.size());
    }
    
    @Test
    public void testIteratorSimple() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(5);

        set.add(1);
        set.add(2);
        set.add(3);

        Iterator<Integer> it = set.iterator();
        
        assertTrue(it.hasNext());
        assertEquals(1, (int) it.next());

        assertTrue(it.hasNext());
        assertEquals(2, (int) it.next());

        assertTrue(it.hasNext());
        assertEquals(3, (int) it.next());
        
        assertFalse(it.hasNext());
    }


    @Test
    public void testIteratorDuplicates() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(5);

        set.add(4);
        set.add(1);
        set.add(2);
        set.remove(4);
        set.add(3);
        set.add(4);
        set.add(1);
        set.add(3);

        Iterator<Integer> it = set.iterator();
        
        // duplicates
        assertTrue(it.hasNext());
        assertEquals(1, (int) it.next());
        assertTrue(it.hasNext());
        assertEquals(1, (int) it.next());

        assertTrue(it.hasNext());
        assertEquals(2, (int) it.next());

        // duplicates
        assertTrue(it.hasNext());
        assertEquals(3, (int) it.next());
        assertTrue(it.hasNext());
        assertEquals(3, (int) it.next());

        assertTrue(it.hasNext());
        assertEquals(4, (int) it.next());

        assertFalse(it.hasNext());
    }
    
    @Test
    public void testResizingDistinctStartingAtOne() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(1);

        assertEquals(1, set.internalCapacity());
        assertEquals(0, set.size());
        assertEquals(0, set.count(2));

        set.add(2);
        assertEquals(1, set.internalCapacity());
        assertEquals(1, set.size());
        assertEquals(1, set.count(2));

        set.add(3);
        assertEquals(2, set.internalCapacity());
        assertEquals(2, set.size());
        assertEquals(1, set.count(2));
        assertEquals(1, set.count(3));

        set.add(4);
        assertEquals(4, set.internalCapacity());
        assertEquals(3, set.size());
        assertEquals(1, set.count(2));
        assertEquals(1, set.count(3));
        assertEquals(1, set.count(4));

        set.add(5);
        assertEquals(4, set.internalCapacity());
        assertEquals(4, set.size());
        assertEquals(1, set.count(2));
        assertEquals(1, set.count(3));
        assertEquals(1, set.count(4));
        assertEquals(1, set.count(5));

        set.add(6);
        assertEquals(8, set.internalCapacity());
        assertEquals(5, set.size());
        assertEquals(1, set.count(2));
        assertEquals(1, set.count(3));
        assertEquals(1, set.count(4));
        assertEquals(1, set.count(5));
        assertEquals(1, set.count(6));
    }

    @Test
    public void testResizingRepeatedStartingAtOne() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(1);

        assertEquals(1, set.internalCapacity());
        assertEquals(0, set.size());
        assertEquals(0, set.count(2));

        set.add(2);
        assertEquals(1, set.internalCapacity());
        assertEquals(1, set.size());
        assertEquals(1, set.count(2));

        set.add(2);
        assertEquals(2, set.internalCapacity());
        assertEquals(2, set.size());
        assertEquals(2, set.count(2));

        set.add(3);
        assertEquals(4, set.internalCapacity());
        assertEquals(3, set.size());
        assertEquals(2, set.count(2));
        assertEquals(1, set.count(3));

        set.add(5);
        assertEquals(4, set.internalCapacity());
        assertEquals(4, set.size());
        assertEquals(2, set.count(2));
        assertEquals(1, set.count(3));
        assertEquals(1, set.count(5));

        set.add(2);
        assertEquals(8, set.internalCapacity());
        assertEquals(5, set.size());
        assertEquals(3, set.count(2));
        assertEquals(1, set.count(3));
        assertEquals(1, set.count(5));
    }

    @Test
    public void testResizingMultiAddStartingAtOne() {
        LinkedMultiHashSet<Integer> set = new LinkedMultiHashSet<>(1);

        assertEquals(1, set.internalCapacity());
        assertEquals(0, set.size());
        assertEquals(0, set.count(2));

        set.add(2, 1);
        assertEquals(1, set.internalCapacity());
        assertEquals(1, set.size());
        assertEquals(1, set.count(2));

        set.add(2, 4);
        assertEquals(8, set.internalCapacity());
        assertEquals(5, set.size());
        assertEquals(5, set.count(2));

        set.add(3, 2);
        assertEquals(8, set.internalCapacity());
        assertEquals(7, set.size());
        assertEquals(5, set.count(2));
        assertEquals(2, set.count(3));

        set.add(5);
        assertEquals(8, set.internalCapacity());
        assertEquals(8, set.size());
        assertEquals(5, set.count(2));
        assertEquals(2, set.count(3));
        assertEquals(1, set.count(5));

        set.add(2);
        assertEquals(16, set.internalCapacity());
        assertEquals(9, set.size());
        assertEquals(6, set.count(2));
        assertEquals(2, set.count(3));
        assertEquals(1, set.count(5));
    }

}