package hr.fer.oprpp1.custom.collections;

/**
 * Interface that provides generic methods for working with list-like collections.
 *
 * @Author Danijel Barišić
 */
public interface List extends Collection {

    /**
     * Obtain element at index.
     *
     * @param index index of desired element
     * @return element at the provided index
     */
    Object get(int index);

    /**
     * Insert element at a particular position in the collection.
     *
     * @param value    element to insert
     * @param position position in collection to insert element at
     */
    void insert(Object value, int position);

    /**
     * Find index of an element.
     *
     * @param value element to find index of
     * @return index of the provided element
     */
    int indexOf(Object value);

    /**
     * Remove element at provided index.
     *
     * @param index index of element to remove
     */
    void remove(int index);
}
