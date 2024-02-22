package hr.fer.oprpp1.hw05.shell;

/**
 * Exception that is thrown by shell for IO operations exceptions.
 *
 * @author Danijel Barišić
 */
public class ShellIOException extends RuntimeException {

    public ShellIOException(String message) {
        super(message);
    }

    public ShellIOException(Throwable cause) {
        super(cause);
    }
}
