package hr.fer.oprpp1.custom.collections;

/**
 * Generic processor class which executes an action for an object
 * passed to the process() method.
 * <p>
 * Note: This particular generic class does nothing,
 * and should be extended in order to implement specific functionality,
 * by overriding process() method.
 *
 * @Author Danijel Barišić
 */
public interface Processor {

    /**
     * Executes an action on an object,
     * i.e. processes an object.
     *
     * @param value an object to process
     */
    void process(Object value);
}