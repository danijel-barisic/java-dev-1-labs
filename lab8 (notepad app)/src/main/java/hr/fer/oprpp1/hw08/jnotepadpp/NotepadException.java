package hr.fer.oprpp1.hw08.jnotepadpp;

public class NotepadException extends RuntimeException{
    public NotepadException() {
    }

    public NotepadException(String message) {
        super(message);
    }

    public NotepadException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotepadException(Throwable cause) {
        super(cause);
    }

}
