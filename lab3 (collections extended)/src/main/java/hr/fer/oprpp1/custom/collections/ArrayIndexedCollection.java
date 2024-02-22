package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Resizable collection of objects backed by an internal array.
 *
 * @Author Danijel Barišić
 * @param <T> type of object to store in array list
 */
public class ArrayIndexedCollection<T> implements List<T> {

    /**
     * Number of elements stored in the collection.
     */
    private int size;

    /**
     * Internal array in which the elements are stored.
     */
    private Object[] elements;

    /**
     * Counts the number of times the collection has been modified,
     * which means elements were added/removed/changed/moved.
     */
    private long modificationCount;

    /**
     * ElementsGetter implementation for ArrayIndexedCollection.
     *
     * @Author Danijel Barišić
     */
    private static class ArrayElementsGetter<T> implements ElementsGetter<T>{

        /**
         * Index of the current element.
         */
        private int index;

        /**
         * Collection from which the ElementsGetter will get elements.
         */
        private final ArrayIndexedCollection<T> col;

        /**
         * Modification count at creation time,
         * which shouldn't differ from concurrent modification count
         * if this ElementsGetter instance will be used again.
         */
        private final long savedModificationCount;

        /**
         * @param col Collection to get elements from
         */
        public ArrayElementsGetter(ArrayIndexedCollection<T> col) {
            this.col = col;
            this.savedModificationCount = col.modificationCount;
        }

        /**
         * Checks if collection has remaining elements that haven't been fetched by this ElementsGetter yet.
         * <p>
         * This does not advance the index.
         *
         * @return true if there are remaining elements, false otherwise.
         * @throws ConcurrentModificationException when collection was modified after this ElementsGetter was created
         */
        public boolean hasNextElement() {

            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the meantime.");
            }

            if (index >= col.size) {
                return false;
            }

            return true;
        }

        /**
         * Fetches next element in the collection and advances the index.
         *
         * @return next element in the collection
         * @throws ConcurrentModificationException when collection was modified after this ElementsGetter was created
         * @throws NoSuchElementException          when end of the collection is reached so there is no more elements to get
         */
        public T getNextElement() {

            if (savedModificationCount != col.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the meantime.");
            }

            if (index >= col.size) {
                throw new NoSuchElementException("There is no more elements to get.");
            }

            return (T) col.elements[index++];
        }
    }

    /**
     * Allocates space for 16 elements in the collection.
     */
    public ArrayIndexedCollection() {
        this(16);
    }

    /**
     * Allocates space for initialCapacity number of elements in an internal array.
     *
     * @param initialCapacity initial size of the internal array
     * @throws IllegalArgumentException when initialCapacity is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity) {

        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity cannot be less than 1");
        }

        elements = new Object[initialCapacity];
    }

    /**
     * Copies elements from the specified collection (shallow copy),
     * with initial capacity becoming equal to other.size() or 16, whatever is larger.
     *
     * @param other collection to copy elements from
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 16);
    }

    /**
     * Copies elements from the specified collection (shallow copy),
     * with initial capacity becoming equal to other.size() or initialCapacity, whatever is larger.
     *
     * @param other           collection to copy elements from
     * @param initialCapacity suggested initial size of the internal array
     * @throws NullPointerException when null is passed as reference to other collection
     */
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {

        if (other == null) {
            throw new NullPointerException("Reference to the other collection cannot be null");
        }

        if (initialCapacity < other.size()) {
            elements = new Object[other.size()];
        } else {
            elements = new Object[initialCapacity];
        }

        addAll(other);
    }

    /**
     * Adds/appends a new element to the collection.
     * If capacity of the internal array is to be exceeded by adding the new element,
     * the capacity is doubled.
     *
     * @param value element to add
     * @throws NullPointerException when the value parameter is null
     */
    @Override
    public void add(T value) {

        if (value == null) {
            throw new NullPointerException("Value of a new element cannot be null");
        }

        if (size == elements.length) {
            Object[] doubledSizeArray = new Object[2 * size];

            for (int i = 0; i < size; i++) {
                doubledSizeArray[i] = elements[i];
            }

            elements = doubledSizeArray;
        }

        elements[size] = value;
        size++;

        modificationCount++;
    }

    /**
     * Removes all elements from the collection.
     * Max capacity remains unchanged.
     */
    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }

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

        for (int i = 0; i < size; i++) {
            if (elements[i].equals(value)) {
                return true;
            }
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

        for (int i = 0; i < size; i++) {
            resultArray[i] = elements[i];
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

        if (index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be a number outside [0, size-1] interval");
        }

        return (T) elements[index];
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

        if (index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be a number outside [0, size-1] interval");
        }

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = null;
        size--;

        modificationCount++;
    }

    /**
     * Inserts an element at the provided position,
     * shifting the elements from the [position, size-1] interval to the right.
     * If capacity of the internal array is to be exceeded by inserting the new element,
     * the capacity is doubled.
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

        if (size == elements.length) {
            Object[] doubledSizeArray = new Object[2 * size];

            for (int i = 0; i < size; i++) {
                doubledSizeArray[i] = elements[i];
            }

            elements = doubledSizeArray;
        }

        for (int i = size; i > position; i--) {
            elements[i] = elements[i - 1];
        }

        elements[position] = value;
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

        for (int i = 0; i < size; i++) {
            if (elements[i].equals(value)) {
                return i;
            }
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

        int index;
        if ((index = indexOf(value)) == -1) {
            return false;
        }

        remove(index);

        modificationCount++;

        return true;
    }

    /**
     * @return ElementsGetter for traversing this collection instance
     */
    public ElementsGetter<T> createElementsGetter() {
        return new ArrayElementsGetter<>(this);
    }
}