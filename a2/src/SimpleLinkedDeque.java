import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The SimpleLinkedDeque has a memory complexity of O(n) where n is the number
 * of items within the deque. This is because each item within the deque requires
 * a new LinkedListNode in the linked list used to to store each item.
 */
public class SimpleLinkedDeque<T> implements SimpleDeque<T> {
    /* The head and tail of the linked list used to store the data of SimpleLinkedDeque. */
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;

    /* Maximum number of items that can be stored in the deque. */
    private int capacity;
    /* Current number of items in the deque */
    private int numItems;
    /* A deque with this capacity has an unlimited capacity */
    private final int UNLIMITED_CAPACITY = -1;

    /**
     * Constructs a new linked list based deque with unlimited capacity.
     *
     * This constructor has an O(1) time complexity as 4 assignment operations
     * are always performed. It also has O(1) memory complexity as there is
     * always a constant amount of memory used.
     */
    public SimpleLinkedDeque() {
        this.head = null;
        this.tail = null;
        this.capacity = UNLIMITED_CAPACITY;
        this.numItems = 0;
    }

    /**
     * Constructs a new linked list based deque with limited capacity.
     *
     * This constructor has an O(1) time complexity as 4 assignment operations
     * are always performed. It also has O(1) memory complexity as there is
     * always a constant amount of memory used.
     *
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0
     */
    public SimpleLinkedDeque(int capacity) throws IllegalArgumentException {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        this.head = null;
        this.tail = null;
        this.capacity = capacity;
        this.numItems = 0;
    }

    /**
     * Constructs a new linked list based deque with unlimited capacity, and initially 
     * populates the deque with the elements of another SimpleDeque.
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @requires otherDeque != null
     */
    public SimpleLinkedDeque(SimpleDeque<? extends T> otherDeque) {
        this.capacity = UNLIMITED_CAPACITY;
        this.numItems = 0;

        Iterator<? extends T> otherIterator = otherDeque.iterator();
        LinkedListNode<T> current = null;
        LinkedListNode<T> previous = null;
        while (otherIterator.hasNext()) {
            current = new LinkedListNode<>(otherIterator.next());
            if (previous != null) {
                previous.setNext(current);
                current.setPrevious(previous);
            }

            if (this.head == null) {
                this.head = current;
            }

            previous = current;
            this.numItems++;
        }

        this.tail = current;
        if (this.tail == null) {
            this.head = null;
        }
    }
    
    /**
     * Constructs a new linked list based deque with limited capacity, and initially 
     * populates the deque with the elements of another SimpleDeque.
     *
     * @param otherDeque the other deque to copy elements from. otherDeque should be left intact.
     * @param capacity the capacity
     * @throws IllegalArgumentException if capacity <= 0 or size of otherDeque is > capacity
     */
    public SimpleLinkedDeque(int capacity, SimpleDeque<? extends T> otherDeque) 
            throws IllegalArgumentException {
        if (capacity <= 0 || otherDeque.size() > capacity) {
            throw new IllegalArgumentException();
        }

        this.capacity = capacity;
        this.numItems = 0;

        Iterator<? extends T> otherIterator = otherDeque.iterator();
        LinkedListNode<T> current = null;
        LinkedListNode<T> previous = null;
        while (otherIterator.hasNext()) {
            current = new LinkedListNode<>(otherIterator.next());
            if (previous != null) {
                previous.setNext(current);
                current.setPrevious(previous);
            }

            if (this.head == null) {
                this.head = current;
            }

            previous = current;
            this.numItems++;
        }

        this.tail = current;
        if (this.tail == null) {
            this.head = null;
        }
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public boolean isEmpty() {
        if (this.numItems == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public boolean isFull() {
        if (this.capacity > UNLIMITED_CAPACITY && this.numItems >= this.capacity) {
            return true;
        }
        return false;
    }

    /**
     * This method has O(1) time complexity and O(1) memory complexity as it is
     * only returning a value.
     */
    @Override
    public int size() {
        return this.numItems;
    }

    /**
     * This method has O(1) time complexity, as all of the operations within the
     * method are O(1) including the creation of a LinkedListNode. This method has
     * O(1) memory complexity as although a new LinkedListNode is created, this is
     * not dependent on the size of "e".
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        if (this.capacity > UNLIMITED_CAPACITY && this.numItems == this.capacity) {
            throw new RuntimeException();
        }

        LinkedListNode<T> insertNode = new LinkedListNode<T>(e);
        if (this.head == null) {
            this.head = insertNode;
            this.tail = this.head;
        } else {
            this.head.setPrevious(insertNode);
            insertNode.setNext(this.head);
            this.head = insertNode;
        }

        this.numItems++;
    }

    /**
     * This method has O(1) time complexity, as all of the operations within the
     * method are O(1) including the creation of a LinkedListNode. This method has
     * O(1) memory complexity as although a new LinkedListNode is created, this is
     * not dependent on the size of "e".
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        if (this.capacity > UNLIMITED_CAPACITY && this.numItems == this.capacity) {
            throw new RuntimeException();
        }

        LinkedListNode<T> insertNode = new LinkedListNode<T>(e);
        if (this.tail == null) {
            this.tail = insertNode;
            this.head = this.tail;
        } else {
            this.tail.setNext(insertNode);
            insertNode.setPrevious(this.tail);
            this.tail = insertNode;
        }

        this.numItems++;
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public T peekLeft() throws NoSuchElementException {
        if (this.numItems == 0) {
            throw new NoSuchElementException();
        }
        return this.head.getValue();
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        if (this.numItems == 0) {
            throw new NoSuchElementException();
        }
        return this.tail.getValue();
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public T popLeft() throws NoSuchElementException {
        if (this.numItems == 0) {
            throw new NoSuchElementException();
        }

        T leftValue = this.head.getValue();
        this.head = this.head.getNext();
        if (this.head != null) {
            this.head.setPrevious(null);
        } else {
            this.tail = null;
        }
        this.numItems--;
        return (T) leftValue;
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as no new memory needs to be allocated for new
     * variables.
     */
    @Override
    public T popRight() throws NoSuchElementException {
        if (this.numItems == 0) {
            throw new NoSuchElementException();
        }

        T rightValue = this.tail.getValue();
        this.tail = this.tail.getPrevious();
        if (this.tail != null) {
            this.tail.setNext(null);
        } else {
            this.head = null;
        }
        this.numItems--;
        return (T) rightValue;
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as although new memory is allocated for the Iterator
     * the data from the deque does not need to be copied.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private LinkedListNode<T> cursor = head;

            @Override
            public boolean hasNext() {
                if (cursor == null) {
                    return false;
                }

                return true;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T next = cursor.value;
                cursor = cursor.getNext();
                return next;
            }
        };
    }

    /**
     * This method has an O(1) time complexity as the number of operations taken
     * is the same no matter the number of items in the deque. This method also has
     * O(1) memory complexity as although new memory is allocated for the Iterator
     * the data from the deque does not need to be copied.
     */
    @Override
    public Iterator<T> reverseIterator() {
        return new Iterator<T>() {
            private LinkedListNode<T> cursor = tail;

            @Override
            public boolean hasNext() {
                if (cursor == null) {
                    return false;
                }

                return true;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T next = cursor.value;
                cursor = cursor.getPrevious();
                return next;
            }
        };
    }

    /**
     * An inner class used to represent a single node in a doubly-linked list.
     * By default, the next and previous elements are set to null and as such
     * setNext() and setPrevious() must be called on each LinkedListNode.
     * @param <T> The type of the object to be stored in this linked list
     */
    private class LinkedListNode<T> {
        private LinkedListNode<T> next;
        private LinkedListNode<T> previous;
        private T value;

        private LinkedListNode(T value) {
            this.next = null;
            this.previous = null;
            this.value = value;
        }

        private void setNext(LinkedListNode<T> next) {
            this.next = next;
        }

        private LinkedListNode<T> getNext() {
            return this.next;
        }

        private void setPrevious(LinkedListNode<T> previous) {
            this.previous = previous;
        }

        private LinkedListNode<T> getPrevious() {
            return this.previous;
        }

        private T getValue() {
            return this.value;
        }
    }
}
