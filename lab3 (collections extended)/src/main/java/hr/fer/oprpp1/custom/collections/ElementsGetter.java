package hr.fer.oprpp1.custom.collections;

/**
 * Interface for traversing collections one by one element.
 *
 * @Author Danijel Barišić
 * @param <T> type of element
 */
public interface ElementsGetter<T> {

    /**
     * @return true if there are elements in the collection that haven't been traversed yet
     */
    boolean hasNextElement();

    /**
     * @return next element in the collection
     */
    T getNextElement();

    /**
     * Process remaining non-traversed elements in the collection.
     *
     * @param p processor to process the remaining elements with
     */
    default void processRemaining(Processor<? super T> p) {
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }

}
