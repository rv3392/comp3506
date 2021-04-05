import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Has an O(n) memory complexity where n is the capacity of the SimpleArrayDeque.
 * This is because the array to store items in the deque is always proportional
 * to the capacity of the deque and this array dominates all other memory usages
 * which are mostly O(1).
 */
public class SimpleArrayDeque<T> implements SimpleDeque<T> {
    /* An array of Objects to store the data for the SimpleDeque. Should be casted to T when retrieved. */
    private Object[] deque;
    /* The positions to add and remove elements when adding/removing from the left and right. */
    private int leftIndex;
    private int rightIndex;
    /* The maximum number of items that can be stored in the deque. */
    private int capacity;
    /* The current number of items in the deque. */
    private int numItems;

    /**
     * Constructs a new array based deque with limited capacity.
     *
     * This constructor has an O(n) time complexity where n is the capacity
     * of the deque. It also has O(n) memory complexity as the array allocated
     * to store the deque has n elements.
     *
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0
     */
    public SimpleArrayDeque(int capacity) throws IllegalArgumentException {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        // These values are used to make sure the first element pushed from either side is pushed
        // to the same element in the deque array.
        this.leftIndex = capacity/2;
        this.rightIndex = capacity/2 - 1;

        this.capacity = capacity;
        this.numItems = 0;
        this.deque = new Object[capacity];
    }

    /**
     * Constructs a new array based deque with limited capacity, and initially populates the deque
     * with the elements of another SimpleDeque.
     *
     * Similar to the previous constructor, an array must be created of length capacity which takes
     * O(n) time where n is the capacity. As well as this, copying the other deque to the new deque
     * also takes O(N) where N is the number of items in the other deque and N <= n. Hence, the
     * time complexity of this method must be O(n).
     *
     * The memory complexity is O(n) where n is the capacity of the deque. As an array must be
     * initialised for the deque data.
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0 or size of otherDeque is > capacity
     */
    public SimpleArrayDeque(int capacity, SimpleDeque<? extends T> otherDeque) 
            throws IllegalArgumentException {
        if (capacity <= 0 || otherDeque.size() > capacity) {
            throw new IllegalArgumentException();
        }

        // These values are used to make sure the first element pushed from either side is pushed
        // to the same element in the deque array.
        this.leftIndex = capacity/2;
        this.rightIndex = capacity/2 - 1;

        this.capacity = capacity;
        this.numItems = 0;
        this.deque = new Object[capacity];

        Iterator<T> otherIterator = (Iterator<T>) otherDeque.iterator();
        while (otherIterator.hasNext()) {
            // Move rightIndex to the start of the array if there is no space left on the rhs
            // of the array. Otherwise just move one more space to the right.
            if (this.rightIndex == capacity - 1) {
                this.rightIndex = 0;
            } else {
                this.rightIndex++;
            }
            this.deque[rightIndex] = otherIterator.next();
            this.numItems++;
        }
    }

    /**
     * This method has a time complexity of O(1) as the length of the deque has no
     * impact on this method's performance. Memory complexity is also O(1).
     */
    @Override
    public boolean isEmpty() {
        if (this.numItems == 0) {
            return true;
        }

        return false;
    }

    /**
     * This method has a time complexity of O(1) as it's comparing values that are
     * stored as members of the class. Memory complexity is also O(1).
     */
    @Override
    public boolean isFull() {
        if (this.numItems >= this.capacity) {
            return true;
        }

        return false;
    }

    /**
     * This method has a time complexity of O(1) as it's returning a class member.
     * Memory complexity is also O(1).
     */
    @Override
    public int size() {
        return this.numItems;
    }

    /**
     * This method has a time complexity of O(1) as it always takes the same number
     * of operations to perform a push. This is because the array has a constant size
     * and accessing/setting an array element occurs in constant time. All other
     * operations such as incrementing the leftIndex and numItems always take
     * the same number of operations.
     *
     * Memory complexity is also O(1) as no new variables are created.
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }

        if (this.leftIndex == 0) {
            this.leftIndex = this.capacity - 1;
        } else {
            this.leftIndex--;
        }

        this.deque[leftIndex] = e;
        this.numItems++;
    }

    /**
     * This method has a time complexity of O(1). The reason is the same as pushLeft
     * as it essentially performs the same task.
     *
     * Memory complexity is also O(1) as no new variables are created.
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        if (this.isFull()) {
            throw new RuntimeException();
        }

        if (this.rightIndex == this.capacity - 1) {
            this.rightIndex = 0;
        } else {
            this.rightIndex++;
        }

        this.deque[rightIndex] = e;
        this.numItems++;
    }

    /**
     * This method has a time complexity of O(1).
     * Memory complexity is also O(1) as no new variables are created.
     */
    @Override
    public T peekLeft() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        return (T) this.deque[this.leftIndex];
    }

    /**
     * This method has a time complexity of O(1).
     * Memory complexity is also O(1) as no new variables are created.
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        return (T) this.deque[this.rightIndex];
    }

    /**
     * This method has a time complexity of O(1).
     * Memory complexity is also O(1) even though a new T instance is created
     * as the memory usage is not proportional to the size of the deque or
     * number of items and references to object instances are always the
     * same size.
     */
    @Override
    public T popLeft() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        T poppedElement = (T) this.deque[this.leftIndex];
        if (this.leftIndex == capacity - 1) {
            this.leftIndex = 0;
        } else {
            this.leftIndex++;
        }

        this.numItems--;
        return poppedElement;
    }

    /**
     * This method has a time complexity of O(1).
     * Memory complexity is also O(1) even though a new T instance is created
     * as the memory usage is not proportional to the size of the deque or
     * number of items and references to object instances are always the
     * same size.
     */
    @Override
    public T popRight() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        T poppedElement = (T) this.deque[this.rightIndex];
        if (this.rightIndex == 0) {
            this.rightIndex = capacity - 1;
        } else {
            this.rightIndex--;
        }

        this.numItems--;
        return poppedElement;
    }

    /**
     * This method has both time and memory complexity O(1) as
     * initiating an object of the same type always takes the
     * same amount of memory.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = leftIndex;

            @Override
            public boolean hasNext() {
                if (numItems == 0) {
                    return false;
                }

                if (leftIndex < rightIndex) {
                    if (currentIndex <= rightIndex && currentIndex >= leftIndex) {
                        return true;
                    }
                }

                if (rightIndex < leftIndex) {
                    if (currentIndex <= rightIndex || currentIndex >= leftIndex) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T next = (T) deque[currentIndex];
                    
                if (currentIndex >= capacity - 1) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                return next;
            }
        };
    }

    /**
     * This method has both time and memory complexity O(1) as
     * initiating an object of the same type always takes the
     * same amount of memory.
     */
    @Override
    public Iterator<T> reverseIterator() {
        return new Iterator<T>() {
            private int currentIndex = rightIndex;

            @Override
            public boolean hasNext() {
                if (numItems == 0) {
                    return false;
                }

                if (leftIndex < rightIndex) {
                    if (currentIndex <= rightIndex && currentIndex >= leftIndex) {
                        return true;
                    }
                }

                if (rightIndex < leftIndex) {
                    if (currentIndex <= rightIndex || currentIndex >= leftIndex) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T next = (T) deque[currentIndex];

                if (currentIndex <= 0) {
                    currentIndex = capacity - 1;
                } else {
                    currentIndex--;
                }
                return next;
            }
        };
    }
}
