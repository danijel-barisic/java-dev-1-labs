package hr.fer.oprpp1.custom.collections;

/**
 * Exception that is thrown when trying to do operations
 * that require the stack to have elements, but the stack is empty.
 *
 * @Author Danijel Barišić
 */
public class EmptyStackException extends RuntimeException {

    /**
     * Prints the provided exception message
     *
     * @param message message to be printed
     */
    public EmptyStackException(String message) {
        super(message);
    }
}
