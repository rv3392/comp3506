import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedMultiHashSet is an implementation of a (@see MultiSet), using a hashtable as the internal
 * data structure, and with predictable iteration order based on the insertion order
 * of elements.
 * 
 * Its iterator orders elements according to when the first occurrence of the element 
 * was added. When the multiset contains multiple instances of an element, those instances 
 * are consecutive in the iteration order. If all occurrences of an element are removed, 
 * after which that element is added to the multiset, the element will appear at the end of the 
 * iteration.
 * 
 * The internal hashtable array should be doubled in size after an add that would cause it to be
 * at full capacity. The internal capacity should never decrease.
 * 
 * Collision handling for elements with the same hashcode (i.e. with hashCode()) should be done
 * using linear probing, as described in lectures.
 * 
 * @param <T> type of elements in the set
 */
public class LinkedMultiHashSet<T> implements MultiSet<T>, Iterable<T> {

    /* An array storing all of the elements of the underlying hash table */
    private LinkedCountableElement<T>[] hashTable;

    /* The linked list used to store LinkedCountableElements for the iterator
       iteratorListHead is the start of this linked list and used as the start of the iterator
       iteratorListTail is the end of the linked list and where new elements are added
     */
    private LinkedCountableElement<T> iteratorListHead;
    private LinkedCountableElement<T> iteratorListTail;

    /* Number of total elements - including duplicates */
    private int numElements;
    /* Number of distinct elements - not including duplicates */
    private int numDistinctElements;
    /* Maximum number of total elements that can be stored - this counts duplicates */
    private int capacity;

    @SuppressWarnings({"unchecked"})
    public LinkedMultiHashSet(int initialCapacity) {
        this.capacity = initialCapacity;
        this.numElements = 0;
        this.numDistinctElements = 0;

        this.hashTable = (LinkedCountableElement<T>[]) new LinkedCountableElement[initialCapacity];
        this.iteratorListTail = null;
        this.iteratorListHead = null;
    }

    @Override
    public void add(T element) {
        this.add(element, 1);
    }

    @Override
    public void add(T element, int count) {
        if (!this.contains(element)) {
            this.addNewElement(element, count);
        } else {
            int index = this.getElementIndexInTable(element);
            this.hashTable[index].addToCount(count);
        }

        this.numElements += count;
        if (this.numDistinctElements >= this.capacity) {
            this.resize();
        }
    }

    private void addNewElement(T element, int count) {
        int insertIndex = this.linearProbing(this.compress(element.hashCode()));

        LinkedCountableElement<T> toAdd = new LinkedCountableElement<T>(element);
        toAdd.addToCount(count - 1);
        this.hashTable[insertIndex] = toAdd;

        this.addToIteratorList(toAdd);

        this.numDistinctElements++;
    }

    @Override
    public boolean contains(T element) {
        if (this.getElementIndexInTable(element) == -1) {
            return false;
        }

        return true;
    }

    @Override
    public int count(T element) {
        int elementIndex = this.getElementIndexInTable(element);
        if (elementIndex == -1) {
            return 0;
        }

        return this.hashTable[elementIndex].getCount();
    }

    @Override
    public void remove(T element) throws NoSuchElementException {
        this.remove(element, 1);
    }

    @Override
    public void remove(T element, int count) throws NoSuchElementException {
        int elementIndex = this.getElementIndexInTable(element);
        if (elementIndex == -1) {
            throw new NoSuchElementException();
        }

        if (count > this.hashTable[elementIndex].getCount()) {
            throw new NoSuchElementException();
        }

        this.hashTable[elementIndex].addToCount(-count);
        if (this.hashTable[elementIndex].getCount() == 0) {
            this.removeFromIteratorList(this.hashTable[elementIndex]);
            this.hashTable[elementIndex] = null;
            this.numDistinctElements--;
        }

        this.numElements -= count;
    }

    @Override
    public int size() {
        return this.numElements;
    }

    @Override
    public int internalCapacity() {
        return this.capacity;
    }

    @Override
    public int distinctCount() {
        return this.numDistinctElements;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            LinkedCountableElement<T> cursor = iteratorListHead;
            int numCursorIterated = 0;

            @Override
            public boolean hasNext() {
                return cursor.getNext() != null || numCursorIterated < cursor.getCount();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                if (numCursorIterated < cursor.getCount()) {
                    numCursorIterated++;
                } else {
                    cursor = cursor.getNext();
                    numCursorIterated = 1;
                }

                return cursor.getValue();
            }
        };
    }

    /**
     * Resizes the internal hash table of LinkedCountableElements doubling the capacity.
     */
    @SuppressWarnings({"unchecked"})
    private void resize() {
        // Double hash table size and create a new array with this new size.
        this.capacity *= 2;
        LinkedCountableElement<T>[] newHashTable = (LinkedCountableElement<T>[]) new LinkedCountableElement[this.capacity];

        // Copy over the elements of the hash table, but reapply the compression function.
        for (LinkedCountableElement<T> element : hashTable) {
            if (element != null) {
                int elementIndex = this.linearProbing(this.compress(element.value.hashCode()), newHashTable);
                newHashTable[elementIndex] = element;
            }
        }

        this.hashTable = newHashTable;
    }

    private int compress(int key) {
        return key % this.capacity;
    }

    /**
     * Applies linear probing to the stored internal representation of the hash
     * and gets the first empty position after "start".
     *
     * @param start index to start linear probing from
     * @return index of the first empty position after "start" in the hash table
     */
    private int linearProbing(int start) {
        return this.linearProbing(start, this.hashTable);
    }

    /**
     * Applies linear probing to the given hash table array and gets the first
     * empty position after "start".
     *
     * @param start index to start linear probing from
     * @param toProbe the array to linearly probe
     * @return index of the first empty position after "start" in the given array
     */
    private int linearProbing(int start, LinkedCountableElement<T>[] toProbe) {
        int i = start;
        while (toProbe[i] != null) {
            i++;
        }

        return i;
    }

    /**
     * Add a new element to the iterator list.
     *
     * This adds a new element to the end of the iterator list such that when
     * this.iterator() is called the element that has been added will appear
     * after all other elements already in the list. The element to add
     * should also be simultaneously added to the internal representation
     * of the set.
     *
     * @param toAdd element to be added to the linked list for the iterator
     */
    private void addToIteratorList(LinkedCountableElement<T> toAdd) {
        if (this.iteratorListHead == null) {
            this.iteratorListHead = toAdd;
            this.iteratorListTail = toAdd;
        } else if (this.iteratorListTail != null) {
            this.iteratorListTail.setNext(toAdd);
            toAdd.setPrevious(this.iteratorListTail);
        }

        this.iteratorListTail = toAdd;
    }

    /**
     * Removes the given element from the iterator linked list.
     *
     * This removes the given element from the list. The given element must be
     * in the list already and thus should have either a "next" or "previous"
     * element. In the case that it has neither then nothing really happens.
     *
     * @param toRemove element to be removed from the list for the iterator.
     */
    private void removeFromIteratorList(LinkedCountableElement<T> toRemove) {
        // TODO: Does this work with a single element?
        if (toRemove.previous != null) {
            toRemove.previous.setNext(toRemove.next);
        }

        if (toRemove.next != null) {
            toRemove.next.setPrevious(toRemove.previous);
        }
    }

    /**
     * Gets the index of the LinkedCountableElement container with the given
     * element. If the given element is not within any of the containers in
     * the hash table, then -1 is returned.
     *
     * @param element the element to search for
     * @return the index of the element if it is found, otherwise -1
     */
    private int getElementIndexInTable(T element) {
        int i = this.compress(element.hashCode());
        if (this.hashTable[i] == null) {
            return -1;
        }

        while (!this.hashTable[i].getValue().equals(element)) {
            i++;
            if (this.hashTable[i] == null) {
                return -1;
            }
            if (i >= this.capacity) {
                return -1;
            }
        }

        return i;
    }

    private class LinkedCountableElement<T> {
        private T value;
        private int count;

        private LinkedCountableElement<T> next;
        private LinkedCountableElement<T> previous;

        LinkedCountableElement(T value) {
            this.value = value;
            this.count = 1;

            this.next = null;
            this.previous = null;
        }

        void addToCount(int i) {
            this.count += i;
        }

        int getCount() {
            return this.count;
        }

        T getValue() {
            return this.value;
        }

        LinkedCountableElement<T> getNext() {
            return this.next;
        }

        void setNext(LinkedCountableElement<T> next) {
            this.next = next;
        }

        LinkedCountableElement<T> getPrevious() {
            return this.previous;
        }

        void setPrevious(LinkedCountableElement<T> previous) {
            this.previous = previous;
        }
    }
}