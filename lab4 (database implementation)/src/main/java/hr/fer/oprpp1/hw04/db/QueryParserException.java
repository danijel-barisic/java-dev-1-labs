package hr.fer.oprpp1.hw04.db;

/**
 * Exception related to syntactical analysis of a query.
 *
 * @author Danijel Barišić
 */
public class QueryParserException extends RuntimeException {

    /**
     * @param message exception message
     */
    public QueryParserException(String message) {
        super(message);
    }

}
