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

    /**
     * See MultiSet.java for more details.
     *
     * This method has a worst case time complexity of O(n) if the
     * element is in the hash set, however has been placed at the end
     * of a "blob" associated with a particular hash code due to
     * a large amount of collisions. The same bound is reached if
     * the element is not in the hash set as linear probing also
     * takes O(n) in this worst case.
     *
     * The memory complexity is O(1) as there is new memory allocated,
     * however it is not proportional to the size of the input but is
     * instead constant.
     *
     * @param element to add
     */
    @Override
    public void add(T element) {
        this.add(element, 1);
    }

    /**
     * O(n) time complexity and O(1) space complexity as mentioned above.
     * @param element to add
     * @param count
     */
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

    /**
     * Add a new element to the hash set if it isn't in the set already.
     *
     * This method has O(n) worst case time complexity and O(1) worst
     * case space complexity.
     *
     * @param element
     * @param count
     */
    private void addNewElement(T element, int count) {
        int insertIndex = this.linearProbing(this.compress(element.hashCode()));

        LinkedCountableElement<T> toAdd = new LinkedCountableElement<T>(element);
        toAdd.addToCount(count - 1);
        this.hashTable[insertIndex] = toAdd;

        this.addToIteratorList(toAdd);

        this.numDistinctElements++;
    }

    /**
     * This method has O(n) worst case time complexity in the event that the
     * element being checked for is at the end of a large group of elements
     * with hash collisions in the hash table.
     *
     * It also has O(1) space complexity as no new memory needs to be
     * allocated.
     *
     * @param element to check
     * @return
     */
    @Override
    public boolean contains(T element) {
        if (this.getElementIndexInTable(element) == -1) {
            return false;
        }

        return true;
    }

    /**
     * This method has O(n) worst case time complexity for the same
     * reason as the contains method. It also has O(1) space complexity.
     *
     * @param element to check
     * @return number of element stored in this LinkedMultiHashSet
     */
    @Override
    public int count(T element) {
        int elementIndex = this.getElementIndexInTable(element);
        if (elementIndex == -1) {
            return 0;
        }

        return this.hashTable[elementIndex].getCount();
    }

    /**
     * The worst case time and space complexity of this method is the same as
     * the worst case time and space complexity of the overloaded method below.
     * @param element to remove
     * @throws NoSuchElementException
     */
    @Override
    public void remove(T element) throws NoSuchElementException {
        this.remove(element, 1);
    }

    /**
     * The worst case time complexity of this method is O(n) in the case
     * where the element to remove may be located at the end of a large
     * block of elements with the same compressed hash code. The worst
     * case space complexity is O(1) as new memory allocated is always
     * constant.
     *
     * @param element to remove
     * @param count the number of occurrences of element to remove
     * @throws NoSuchElementException
     */
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

    /**
     * The space and time complexity of this method is O(1).
     * @return the number of total elements in the LinkedMultiHashSet
     */
    @Override
    public int size() {
        return this.numElements;
    }

    /**
     * The space and time complexity of this method is O(1).
     * @return the number of elements that can be stored in this LinkedMultiHashSet
     */
    @Override
    public int internalCapacity() {
        return this.capacity;
    }

    /**
     * The space and time complexity of this method is O(1).
     * @return the number of distinct elements in this LinkedMultiHashSet
     */
    @Override
    public int distinctCount() {
        return this.numDistinctElements;
    }

    /**
     * The space and time complexity of this method is O(1) as creating
     * an iterators always takes the same number of operations and
     * memory.
     *
     * @return an iterator for this LinkedMultiHashSet
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            /* Current position of the iterator */
            LinkedCountableElement<T> cursor = iteratorListHead;
            /* Number of duplicates of a particular element that have been iterated */
            int numCursorIterated = 0;

            /**
             * The space and time complexity of this method is O(1).
             * @return true if there is another element left and false otherwise
             */
            @Override
            public boolean hasNext() {
                return cursor.getNext() != null || numCursorIterated < cursor.getCount();
            }

            /**
             * The space and time complexity of this method is O(1).
             * @return the next element in the iterator
             */
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
     *
     * This method has a worst case time complexity of O(mn) where n is the total capacity of
     * the hash table array and m is the number of distinct elements in the hash table. This is
     * because the new array needs to be iterated through and elements from the old array moved
     * to it.
     *
     * However, since future add operations won't require a resize to be called each time,
     * the amortised worst case time complexity is actually O(n).
     *
     * The space complexity is also O(n) as the method doubles the size of the hash table
     * array. The amortised space complexity is O(1) as the average memory used by each
     * add operation is reduced by the use of this single expensive resize.
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

    /**
     * Applies the compression function on the given hash key.
     *
     * This method has a worst case time and space complexity of O(1).
     *
     * @param key the hash code to compress
     * @return the compressed hash code such that the element can be added to the
     *      hash table using it
     */
    private int compress(int key) {
        return key % this.capacity;
    }

    /**
     * Applies linear probing to the stored internal representation of the hash
     * and gets the first empty position after "start".
     *
     * The time complexity of this method is O(n) as it calls the overloaded
     * linearProbing method. The space complexity is O(1).
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
     * The worst case time complexity of this method is O(n). This can occur if
     * there were many elements added such that they had a hash collision resulting
     * in a large "blob" of elements stored at a particular index. The method would
     * have to iterate through all of these and so it would take linear time.
     *
     * The space complexity is O(1) as there are no changes made to the memory allocated.
     *
     * @param start index to start linear probing from
     * @param toProbe the array to linearly probe
     * @return index of the first empty position after "start" in the given array
     */
    private int linearProbing(int start, LinkedCountableElement<T>[] toProbe) {
        int i = start;
        while (toProbe[i] != null) {
            i++;
            if (i >= this.capacity) {
                i = 0;
            } else if (i == start) {
                return -1;
            }
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
     * The worst case time and space complexity of this method is O(1).
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
     * The worst case time and space complexity of this method is O(1).
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
     * This method has a time complexity of O(n) in the worst case scenario as
     * a hash collision being resolved with linear probing means that the element
     * may be stored quite far from the index given by the compression function.
     *
     * The space complexity is O(1).
     *
     * @param element the element to search for
     * @return the index of the element if it is found, otherwise -1
     */
    private int getElementIndexInTable(T element) {
        int start = this.compress(element.hashCode());
        int i = start;
        if (this.hashTable[i] == null) {
            return -1;
        }

        while (!this.hashTable[i].getValue().equals(element)) {
            i++;
            if (i >= this.capacity) {
                i = 0; // Wrap around to the start of the hash table
            }
            if (i == start) {
                return -1;
            }
            if (this.hashTable[i] == null) {
                return -1;
            }
        }

        return i;
    }

    private class LinkedCountableElement<T> {
        /* The value encapsulated by this LinkedCountableElement */
        private T value;
        /* The amount of the encapsulated element */
        private int count;

        /* The next and previous LinkedCountableElement in terms of the order in which they were added to the set */
        private LinkedCountableElement<T> next;
        private LinkedCountableElement<T> previous;

        /**
         * Constructor for a LinkedCountableElement.
         * @param value the value to store in this LinkedCountableElement
         */
        LinkedCountableElement(T value) {
            this.value = value;
            this.count = 1;

            this.next = null;
            this.previous = null;
        }

        /**
         * Increment the count of the element stored in this instance by 'i'.
         *
         * This method is O(1) in terms of both time and space complexity.
         *
         * @param i Amount to increment by.
         */
        void addToCount(int i) {
            this.count += i;
        }

        /**
         * Get the count of this element.
         *
         * This method has both O(1) time and space complexity in the worst
         * case.
         *
         * @return the count of this element
         */
        int getCount() {
            return this.count;
        }

        /**
         * Get the value encapsulated by this LinkedCountableElement.
         *
         * This method has both O(1) time and space complexity.
         *
         * @return the value stored
         */
        T getValue() {
            return this.value;
        }

        /**
         * Get the next LinkedCountableElement that was added to the hash set.
         *
         * This method has both O(1) time and space complexity.
         *
         * @return the next element or null if this is the last one
         */
        LinkedCountableElement<T> getNext() {
            return this.next;
        }

        /**
         * Set the next LinkedCountableElement that was added to the hash set.
         *
         * This method has both O(1) time and space complexity.
         *
         * @param next the element to set as the next one
         */
        void setNext(LinkedCountableElement<T> next) {
            this.next = next;
        }

        /**
         * Get the previous LinkedCountableElement that was added to the hash set.
         *
         * This method has both O(1) time and space complexity.
         *
         * @return the previous element or null if this is the first one
         */
        LinkedCountableElement<T> getPrevious() {
            return this.previous;
        }

        /**
         * Set the previous LinkedCountableElement that was added to the hash set.
         *
         * This method has both O(1) time and space complexity.
         *
         * @param previous the element to set as the previous one
         */
        void setPrevious(LinkedCountableElement<T> previous) {
            this.previous = previous;
        }
    }
}