package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Resizable collection of objects backed with an internal linked list.
 *
 * @param <T> type of object to store in linked list
 * @Author Danijel Barišić
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    /**
     * Number of elements stored in the collection.
     */
    private int size;

    /**
     * Reference to the first node of the internal linked list.
     */
    private ListNode<T> first;

    /**
     * Reference to the last node of the internal linked list.
     */
    private ListNode<T> last;

    /**
     * Counts the number of times the collection has been modified,
     * which means elements were added/removed/changed/moved.
     */
    private long modificationCount;

    /**
     * Class that represents a node in the internal linked list,
     * which contains references to the previous and next neighbouring nodes,
     * as well as a reference to an element/value.
     *
     * @Author Danijel Barišić
     */
    private static class ListNode<T> {

        /**
         * Reference to the previous node in the linked list.
         */
        ListNode<T> previous;

        /**
         * Reference to the next node in the linked list.
         */
        ListNode<T> next;

        /**
         * Reference to an element to be contained in this node.
         */
        T value;
    }

    /**
     * ElementsGetter implementation for ArrayIndexedCollection.
     *
     * @Author Danijel Barišić
     */
    private static class LinkedListElementsGetter<T> implements ElementsGetter<T> {

        /**
         * Node containing the current element.
         */
        private ListNode<T> currNode;

        /**
         * Collection from which the ElementsGetter will get elements.
         */
        private final LinkedListIndexedCollection<T> col;

        /**
         * Modification count at creation time,
         * which shouldn't differ from concurrent modification count
         * if this ElementsGetter instance will be used again.
         */
        private final long savedModificationCount;

        /**
         * @param col Collection to get elements from
         */
        public LinkedListElementsGetter(LinkedListIndexedCollection<T> col) {
            this.col = col;
            this.currNode = col.first;
            this.savedModificationCount = col.modificationCount;
        }

        /**
         * Checks if collection has remaining elements that haven't been fetched by this ElementsGetter yet.
         * <p>
         * This does not advance the index/node.
         *
         * @return true if there are remaining elements, false otherwise.
         * @throws ConcurrentModificationException when collection was modified after this ElementsGetter was created
         */
        public boolean hasNextElement() {

            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the meantime.");
            }

            if (currNode == null) {
                return false;
            }

            return true;
        }

        /**
         * Fetches next element in the collection and advances the index/node.
         *
         * @return next element in the collection
         * @throws ConcurrentModificationException when collection was modified after this ElementsGetter was created
         * @throws NoSuchElementException          when end of the collection is reached so there is no more elements to get
         */
        public T getNextElement() {

            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the meantime.");
            }

            if (currNode == null) {
                throw new NoSuchElementException("There is no more elements to get.");
            }

            ListNode<T> nextNode = currNode;
            currNode = currNode.next;

            return nextNode.value;
        }
    }

    /**
     * Sets the references to the first and last node equal to null.
     */
    public LinkedListIndexedCollection() {
        first = last = null;
    }

    /**
     * Copies elements from the specified collection (shallow copy).
     *
     * @param other collection to copy elements from
     * @throws NullPointerException when reference to the other collection is null
     */
    public LinkedListIndexedCollection(Collection<T> other) {

        if (other == null) {
            throw new NullPointerException("Reference to the other collection cannot be null");
        }

        addAll(other);
    }

    /**
     * Adds/appends a new element to the collection.
     *
     * @param value element to add
     * @throws NullPointerException when the value parameter is null
     */
    public void add(T value) {

        if (value == null) {
            throw new NullPointerException("Value of a new element cannot be null");
        }

        ListNode<T> newNode = new ListNode<>();
        newNode.value = value;

        size++;

        if (size == 1) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.previous = last;
        }
        last = newNode;

        modificationCount++;
    }

    /**
     * Removes all elements from the collection,
     * i.e. deletes references to the first and last elements of the internal linked list,
     * thereby "forgetting" the internal linked list.
     */
    public void clear() {

        first = last = null;
        size = 0;

        modificationCount++;
    }

    /**
     * @return number of elements in the collection
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the collection contains the specified element.
     *
     * @param value object for which to check existence in the collection
     * @return true if collection contains the specified object value, false otherwise
     */
    @Override
    public boolean contains(Object value) {

        ListNode<T> currNode = first;

        while (currNode != null) {
            if (currNode.value.equals(value)) {
                return true;
            }
            currNode = currNode.next;
        }

        return false;
    }

    /**
     * Converts the collection to plain Java array.
     *
     * @return array of elements stored in the collection
     */
    @Override
    public Object[] toArray() {

        Object[] resultArray = new Object[size];

        ListNode<T> currNode = first;

        for (int i = 0; i < size; i++) {
            resultArray[i] = currNode.value;
            currNode = currNode.next;
        }

        return resultArray;
    }

    /**
     * Returns the element that is stored in collection at the provided index.
     * <p>
     * Valid indexes are 0 to size-1.
     *
     * @param index index of the element to be returned
     * @return element at the provided index in the collection
     * @throws IndexOutOfBoundsException when the provided index is outside of the [0, size-1] interval
     */
    public T get(int index) {

        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("Index cannot be a number outside [0, size-1] interval");
        }

        ListNode<T> currNode;

        if (index < size / 2) {
            currNode = first;

            for (int i = 0; i != index && i < size / 2; i++) {
                currNode = currNode.next;
            }
        } else {
            currNode = last;

            for (int i = size - 1; i != index && i >= size / 2; i--) {
                currNode = currNode.previous;
            }
        }

        return currNode.value;
    }

    /**
     * Removes an element at a specified index, shifting the elements on the right of the removed element
     * toward left.
     * <p>
     * Valid indexes are 0 to size-1.
     *
     * @param index index of the element to remove
     * @throws IndexOutOfBoundsException when the provided index is outside of the [0, size-1] interval
     */
    public void remove(int index) {

        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be a number outside [0, size-1] interval");
        }

        ListNode<T> currNode;

        if (index < size / 2) {
            currNode = first;

            for (int i = 0; i != index && i < size / 2; i++) {
                currNode = currNode.next;
            }
        } else {
            currNode = last;

            for (int i = size - 1; i != index && i >= size / 2; i--) {
                currNode = currNode.previous;
            }
        }

        currNode.next.previous = currNode.previous;
        currNode.previous.next = currNode.next;

        size--;

        modificationCount++;
    }

    /**
     * Inserts an element at the provided position,
     * shifting the elements from the [position, size-1] interval to the right.
     * <p>
     * Valid indexes are 0 to size.
     *
     * @param value    element to be inserted
     * @param position index at which to insert the element
     * @throws NullPointerException      when value of a new element is null
     * @throws IndexOutOfBoundsException when position is a number outside [0, size] interval
     */
    public void insert(T value, int position) {

        if (value == null) {
            throw new NullPointerException("Value of a new element cannot be null");
        }

        if (position > size || position < 0) {
            throw new IndexOutOfBoundsException("Position cannot be a number outside [0, size] interval");
        }

        ListNode<T> currNode;

        if (position < size / 2) {
            currNode = first;

            for (int i = 0; i != position && i < size / 2; i++) {
                currNode = currNode.next;
            }
        } else {
            currNode = last;

            for (int i = size - 1; i != position && i >= size / 2; i--) {
                currNode = currNode.previous;
            }
        }

        ListNode<T> newNode = new ListNode<>();
        newNode.value = value;

        newNode.next = currNode;
        newNode.previous = currNode.previous;
        currNode.previous.next = newNode;
        currNode.previous = newNode;

        size++;

        modificationCount++;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value.
     *
     * @param value element to search the index of
     * @return index of the first occurrence of the element, or -1 if the element is not found.
     */
    public int indexOf(Object value) {

        ListNode<T> currNode = first;

        for (int i = 0; i < size; i++) {
            if (currNode.value.equals(value)) {
                return i;
            }
            currNode = currNode.next;
        }

        return -1;
    }

    /**
     * Removes first occurrence of the specified element value from collection.
     *
     * @param value object to remove
     * @return true if collection contains the element, false if not
     */
    @Override
    public boolean remove(Object value) {

        ListNode<T> currNode = first;
        int i;
        for (i = 0; i < size; i++, currNode = currNode.next) {
            if (currNode.value.equals(value)) {
                currNode.next.previous = currNode.previous;
                currNode.previous.next = currNode.next;
                break;
            }
        }

        if (i == size) {
            return false;
        }

        size--;

        modificationCount++;

        return true;
    }

    /**
     * @return ElementsGetter for traversing this collection instance
     */
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListElementsGetter<>(this);
    }

}
