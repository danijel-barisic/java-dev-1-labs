package hr.fer.oprpp1.custom.collections;

/**
 * Interface for traversing collections one by one element.
 *
 * @Author Danijel Barišić
 */
public interface ElementsGetter {

    /**
     * @return true if there are elements in the collection that haven't been traversed yet
     */
    boolean hasNextElement();

    /**
     * @return next element in the collection
     */
    Object getNextElement();

    /**
     * Process remaining non-traversed elements in the collection.
     *
     * @param p processor to process the remaining elements with
     */
    default void processRemaining(Processor p) {
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }

}
