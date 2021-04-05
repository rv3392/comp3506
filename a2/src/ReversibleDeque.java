import java.util.Iterator;
import java.util.NoSuchElementException;

public class ReversibleDeque<T> implements SimpleDeque<T> {
    /**
     * The deque provided to be used as an internal representation of the data.
     * All implemented methods call methods of this deque.
     */
    private SimpleDeque<T> data;

    /**
     * The current direction the deque is facing. This controls which side should
     * be popped, pushed and peeked from. That is, peekLeft on a ReversibleDeque
     * with direction = BACKWARDS becomes a peekRight on the internal data deque.
     */
    private int direction;
    /* Represents when the deque faces forwards as an integer state */
    private final int FORWARDS = 0;
    /* Represents when the deque faces forwards as an integer state */
    private final int BACKWARDS = 1;

    /**
     * Constructs a new reversible deque, using the given data deque to store
     * elements.
     * The data deque must not be used externally once this ReversibleDeque
     * is created.
     *
     * This constructor has an O(1) time complexity and O(1) memory
     * complexity as two allocation operations always run and no
     * new memory is allocated.
     *
     * @param data a deque to store elements in.
     */
    public ReversibleDeque(SimpleDeque<T> data) {
        this.data = data;
        this.direction = FORWARDS; // Starting direction is forwards.
    }

    /**
     * This method is called to reverse the ReversibleDeque.
     *
     * Calling it once results in the deque becoming reversed. This means
     * that the leftmost element becomes the rightmost and the element
     * one away from the left end is now one away from the right end and
     * so on. Calling it again after the first time results in the deque
     * ending back in its initial orientation.
     *
     * This method has an O(1) time complexity and memory complexity
     * as the number of operations performed is only dependent on
     * the current orientation of the deque and no new memory is
     * ever allocated.
     */
    public void reverse() {
        if (this.direction == FORWARDS) {
            this.direction = BACKWARDS;
        } else if (this.direction == BACKWARDS) {
            this.direction = FORWARDS;
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its size() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public int size() {
        return this.data.size();
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its isEmpty() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its isFull() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public boolean isFull() {
        return this.data.isFull();
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its pushLeft() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public void pushLeft(T e) throws RuntimeException {
        if (this.direction == FORWARDS) {
            this.data.pushLeft(e);
        } else {
            this.data.pushRight(e);
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its pushRight() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public void pushRight(T e) throws RuntimeException {
        if (this.direction == FORWARDS) {
            this.data.pushRight(e);
        } else {
            this.data.pushLeft(e);
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its peekLeft() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public T peekLeft() throws NoSuchElementException {
        if (this.direction == FORWARDS) {
            return this.data.peekLeft();
        } else {
            return this.data.peekRight();
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its peekRight() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public T peekRight() throws NoSuchElementException {
        if (this.direction == FORWARDS) {
            return this.data.peekRight();
        } else {
            return this.data.peekLeft();
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its popLeft() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public T popLeft() throws NoSuchElementException {
        if (this.direction == FORWARDS) {
            return this.data.popLeft();
        } else {
            return this.data.popRight();
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its popRight() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public T popRight() throws NoSuchElementException {
        if (this.direction == FORWARDS) {
            return this.data.popRight();
        } else {
            return this.data.popLeft();
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its iterator() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public Iterator<T> iterator() {
        if (this.direction == FORWARDS) {
            return this.data.iterator();
        } else {
            return this.data.reverseIterator();
        }
    }

    /**
     * The time and memory complexity of this method is dependent on the
     * specific implementation of SimpleDeque used and its reverseIterator() Big-O bounds.
     * They are O(1) if the wrapped deque is SimpleArrayDeque or SimpleLinkedDeque.
     */
    @Override
    public Iterator<T> reverseIterator() {
        if (this.direction == FORWARDS) {
            return this.data.reverseIterator();
        } else {
            return this.data.iterator();
        }
    }
}
