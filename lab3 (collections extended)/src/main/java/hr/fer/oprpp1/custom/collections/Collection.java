package hr.fer.oprpp1.custom.collections;


/**
 * Generic collection interface, i.e. data container interface, providing useful methods for data manipulation
 * in custom containers.
 *
 * @Author Danijel Barišić
 * @param <T> type of object to store in collection
 */
public interface Collection<T> {

    /**
     * Checks if the collection has no elements.
     *
     * @return true if number of elements in the collection is 0, false otherwise
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks the collection size, i.e. the number of elements in the collection
     *
     * @return number of elements in the collection
     */
    int size();

    /**
     * Adds a new element to the collection.
     *
     * @param value value of the new element to add
     */
    void add(T value);

    /**
     * Checks if a particular element is contained
     * in the collection.
     *
     * @param value object for which to check existence in the collection
     * @return false
     */
    boolean contains(Object value);

    /**
     * Removes an element from the collection.
     *
     * @param value object to be removed
     * @return false
     */
    boolean remove(Object value);

    /**
     * Converts a collection to plain Java array.
     */
    Object[] toArray();

    /**
     * Processes each element with a custom processor,
     * i.e. executes an action for each element.
     *
     * @param processor a processor to process each element of the collection with
     */
    default void forEach(Processor<? super T> processor) {

        ElementsGetter<T> eg = createElementsGetter();
        while (eg.hasNextElement()) {
            processor.process(eg.getNextElement());
        }
    }

    /**
     * Add all elements from another collection into the current collection.
     *
     * @param other collection to add elements from
     */
    default void addAll(Collection<? extends T> other) {

        class AddProcessor implements Processor<T> {

            public void process(T value) {
                Collection.this.add(value);
            }
        }

        other.forEach(new AddProcessor());
    }

    /**
     * Removes all elements from the collection.
     */
    void clear();

    /**
     * @return ElementsGetter for traversing this collection instance
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all elements from collection col to current collection,
     * that satisfy the test governed by tester (i.e. elements for which
     * the tester.test() returns true, they're added to the collection).
     *
     * @param col    collection that contains the elements to be tested and potentially added
     * @param tester particular tester to test the elements of the collection col with
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {

        ElementsGetter<? extends T> eg = col.createElementsGetter();

        T currElement;
        while (eg.hasNextElement()) {
            currElement = eg.getNextElement();

            if (tester.test(currElement)) {
                add(currElement);
            }
        }
    }
}
