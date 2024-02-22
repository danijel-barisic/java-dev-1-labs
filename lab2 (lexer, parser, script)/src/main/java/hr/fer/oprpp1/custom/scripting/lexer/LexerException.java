package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * RuntimeException child that models various exceptions for SmartScriptLexer operation.
 *
 * @Author Danijel Barišić
 */
public class LexerException extends RuntimeException {

    /**
     * @param msg exception message to delegate to the RuntimeException
     */
    public LexerException(String msg) {
        super(msg);
    }
}