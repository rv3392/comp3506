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

    private CountableElement<T>[] hashTable;

    private int numElements;
    private int numDistinctElements;
    private int capacity;

    @SuppressWarnings({"unchecked"})
    public LinkedMultiHashSet(int initialCapacity) {
        this.capacity = initialCapacity;
        this.numElements = 0;
        this.numDistinctElements = 0;

        this.hashTable = (CountableElement<T>[]) new CountableElement[initialCapacity];
    }

    @Override
    public void add(T element) {
        if (this.numElements >= this.capacity) {
            this.resize();
        }

        if (!this.contains(element)) {
            int insertIndex = this.linearProbing(this.hash(element.hashCode()));
            this.hashTable[insertIndex] = new CountableElement<T>(element);
            this.numDistinctElements++;
        } else {
            int index = this.getElementIndexInTable(element);
            this.hashTable[index].addToCount(1);
        }

        this.numElements++;
    }

    @Override
    public void add(T element, int count) {
        if (this.numElements >= this.capacity) {
            this.resize();
        }

        if (!this.contains(element)) {
            int insertIndex = this.linearProbing(this.hash(element.hashCode()));
            this.hashTable[insertIndex] = new CountableElement<T>(element);
            this.hashTable[insertIndex].addToCount(count - 1);
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
        return null;
    }

    private void resize() {
        //TODO: Implement this
    }

    private int hash(int key) {
        return key % this.capacity;
    }

    private int linearProbing(int start) {
        int i = start;
        while (this.hashTable[i] != null) {
            i++;
        }

        return i;
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

    private class CountableElement<T> {
        T value;
        int count;

        CountableElement (T value) {
            this.value = value;
            this.count = 1;
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


    }
}