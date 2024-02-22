package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Exception related to lexical analysis of a query.
 *
 * @author Danijel Barišić
 */
public class QueryLexerException extends RuntimeException {

    /**
     * @param message exception message
     */
    public QueryLexerException(String message) {
        super(message);
    }
}
