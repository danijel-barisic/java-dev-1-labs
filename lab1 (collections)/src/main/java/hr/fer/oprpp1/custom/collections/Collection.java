package hr.fer.oprpp1.custom.collections;


/**
 * Generic collection class.
 * <p>
 * In order to create a custom collection, available methods should be overridden
 * in the custom implementation.
 *
 * @Author Danijel Barišić
 */
public class Collection {

    /**
     * Default empty constructor
     */
    protected Collection() {

    }

    /**
     * Checks if the collection has no elements.
     *
     * @return true if number of elements in the collection is 0, false otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Empty placeholder method for checking collection size.
     * <p>
     * Note: should be overridden for custom behaviour
     *
     * @return 0, as a placeholder
     */
    public int size() {
        return 0;
    }

    /**
     * Empty placeholder method for adding a new element to the collection.
     * <p>
     * Note: should be overridden for custom behaviour
     */
    public void add(Object value) {

    }

    /**
     * Empty placeholder method for checking if a particular element is contained
     * in the collection.
     * <p>
     * Note: should be overridden for custom behaviour
     *
     * @param value object for which to check existence in the collection
     * @return false, as a placeholder
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Empty placeholder method for removing an element from the collection.
     * <p>
     * Note: should be overridden for custom behaviour
     *
     * @param value object to be removed
     * @return false, as a placeholder
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Placeholder method for converting a collection to plain Java array.
     * <p>
     * Note: should be overridden for custom behaviour
     *
     * @throws UnsupportedOperationException, because this generic collection doesn't store elements
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Empty placeholder method for processing each element with a custom processor.
     * <p>
     * Note: should be overridden for custom behaviour
     *
     * @param processor a processor to process each element of the collection with
     */
    public void forEach(Processor processor) {

    }

    /**
     * Add all elements from another collection into the current collection.
     *
     * @param other collection to add elements from
     */
    public void addAll(Collection other) {

        class AddProcessor extends Processor {

            public void process(Object value) {
                Collection.this.add(value);
            }
        }

        other.forEach(new AddProcessor());
    }

    /**
     * Empty placeholder method for removing all elements from the collection.
     * <p>
     * Note: should be overridden for custom behaviour
     */
    public void clear() {

    }

}
