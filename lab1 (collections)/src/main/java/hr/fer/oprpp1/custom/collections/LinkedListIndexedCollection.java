package hr.fer.oprpp1.custom.collections;

/**
 * Resizable collection of objects backed with an internal linked list.
 *
 * @Author Danijel Barišić
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * Class that represents a node in the internal linked list,
     * which contains references to the previous and next neighbouring nodes,
     * as well as a reference to an element/value.
     *
     * @Author Danijel Barišić
     */
    private static class ListNode {

        /**
         * Reference to the previous node in the linked list.
         */
        public ListNode previous;

        /**
         * Reference to the next node in the linked list.
         */
        public ListNode next;

        /**
         * Reference to an element to be contained in this node.
         */
        public Object value;
    }

    /**
     * Number of elements stored in the collection.
     */
    private int size;

    /**
     * Reference to the first node of the internal linked list.
     */
    private ListNode first;

    /**
     * Reference to the last node of the internal linked list.
     */
    private ListNode last;

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
    public LinkedListIndexedCollection(Collection other) {

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
    public void add(Object value) {

        if (value == null) {
            throw new NullPointerException("Value of a new element cannot be null");
        }

        ListNode newNode = new ListNode();
        newNode.value = value;

        size++;

        if (size == 1) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.previous = last;
        }
        last = newNode;
    }

    /**
     * Removes all elements from the collection,
     * i.e. deletes references to the first and last elements of the internal linked list,
     * thereby "forgetting" the internal linked list.
     */
    public void clear() {

        first = last = null;
        size = 0;
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

        ListNode currNode = first;

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

        ListNode currNode = first;

        for (int i = 0; i < size; i++) {
            resultArray[i] = currNode.value;
            currNode = currNode.next;
        }

        return resultArray;
    }

    /**
     * Processes each element of the collection via a specified processor,
     * i.e. executes a particular action for each object.
     *
     * @param processor a processor to process each element of the collection with
     */
    @Override
    public void forEach(Processor processor) {

        ListNode currNode = first;

        for (int i = 0; i < size; i++) {
            processor.process(currNode.value);
            currNode = currNode.next;
        }
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
    public Object get(int index) {

        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("Index cannot be a number outside [0, size-1] interval");
        }

        ListNode currNode;

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

        ListNode currNode;

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
    public void insert(Object value, int position) {

        if (value == null) {
            throw new NullPointerException("Value of a new element cannot be null");
        }

        if (position > size || position < 0) {
            throw new IndexOutOfBoundsException("Position cannot be a number outside [0, size] interval");
        }

        ListNode currNode;

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

        ListNode newNode = new ListNode();
        newNode.value = value;

        newNode.next = currNode;
        newNode.previous = currNode.previous;
        currNode.previous.next = newNode;
        currNode.previous = newNode;

        size++;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value.
     *
     * @param value element to search the index of
     * @return index of the first occurrence of the element, or -1 if the element is not found.
     */
    public int indexOf(Object value) {

        ListNode currNode = first;

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

        ListNode currNode = first;
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

        return true;
    }

}
