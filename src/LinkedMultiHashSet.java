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

    private LinkedCountableElement<T>[] hashTable;
    private LinkedCountableElement<T> iteratorListHead;
    private LinkedCountableElement<T> iteratorListTail;

    private int numElements;
    private int numDistinctElements;
    private int capacity;

    @SuppressWarnings({"unchecked"})
    public LinkedMultiHashSet(int initialCapacity) {
        this.capacity = initialCapacity;
        this.numElements = 0;
        this.numDistinctElements = 0;

        this.hashTable = (LinkedCountableElement<T>[]) new LinkedCountableElement[initialCapacity];
        this.iteratorListTail = null;
    }

    @Override
    public void add(T element) {
        if (this.numElements + 1 > this.capacity) {
            this.resize();
        }

        if (!this.contains(element)) {
            int insertIndex = this.linearProbing(this.hash(element.hashCode()));
            // TODO: This is a bit messy
            LinkedCountableElement<T> toAdd = new LinkedCountableElement<T>(element);
            this.hashTable[insertIndex] = toAdd;
            this.addToIteratorList(toAdd);
            this.numDistinctElements++;
        } else {
            int index = this.getElementIndexInTable(element);
            this.hashTable[index].addToCount(1);
        }

        this.numElements++;
    }

    @Override
    public void add(T element, int count) {
        while (this.numElements + count > this.capacity) {
            this.resize();
        }

        if (!this.contains(element)) {
            int insertIndex = this.linearProbing(this.hash(element.hashCode()));
            LinkedCountableElement<T> toAdd = new LinkedCountableElement<T>(element);
            toAdd.addToCount(count - 1);
            this.hashTable[insertIndex] = toAdd;
            this.addToIteratorList(toAdd);
            this.numDistinctElements++;
        } else {
            int index = this.getElementIndexInTable(element);
            this.hashTable[index].addToCount(count);
        }

        this.numElements += count;
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
        int elementIndex = this.getElementIndexInTable(element);
        if (elementIndex == -1) {
            throw new NoSuchElementException();
        }

        this.hashTable[elementIndex].addToCount(-1);
        if (this.hashTable[elementIndex].getCount() == 0) {
            this.hashTable[elementIndex] = null;
            this.numDistinctElements--;
        }

        this.numElements--;
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

    @SuppressWarnings({"unchecked"})
    private void resize() {
        this.capacity *= 2;
        LinkedCountableElement<T>[] newHashTable = (LinkedCountableElement<T>[]) new LinkedCountableElement[this.capacity];

        for (int i = 0; i < hashTable.length; i++) {
            if (this.hashTable[i] != null) {
                int elementIndex = this.linearProbing(this.hash(this.hashTable[i].value.hashCode()), newHashTable);
                newHashTable[elementIndex] = this.hashTable[i];
            }
        }

        this.hashTable = newHashTable;
    }

    private int hash(int key) {
        return key % this.capacity;
    }

    private int linearProbing(int start) {
        return this.linearProbing(start, this.hashTable);
    }

    private int linearProbing(int start, LinkedCountableElement<T>[] toProbe) {
        int i = start;
        while (toProbe[i] != null) {
            i++;
        }

        return i;
    }

    private void addToIteratorList(LinkedCountableElement<T> toAdd) {
        if (this.iteratorListHead == null) {
            this.iteratorListHead = toAdd;
            this.iteratorListTail = toAdd;
        } else if (this.iteratorListTail != null) {
            this.iteratorListTail.setNext(toAdd);
        }

        this.iteratorListTail = toAdd;
    }

    private int getElementIndexInTable(T element) {
        int i = this.hash(element.hashCode());
        if (this.hashTable[i] == null) {
            return -1;
        }

        while (!this.hashTable[i].getValue().equals(element)) {
            i++;
            if (this.hashTable[i] == null) {
                return -1;
            }
            if (i > this.numElements - 1) {
                return -1;
            }
        }

        return i;
    }

    private class LinkedCountableElement<T> {
        private T value;
        private int count;

        private LinkedCountableElement<T> next;

        LinkedCountableElement(T value) {
            this.value = value;
            this.count = 1;

            this.next = null;
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
    }
}