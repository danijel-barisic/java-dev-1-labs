package hr.fer.oprpp1.custom.scripting.parser;

/**
 * RuntimeException child that models various exceptions for SmartScriptParser operation.
 *
 * @Author Danijel Barišić
 */
public class SmartScriptParserException extends RuntimeException {

    /**
     * @param msg exception message to delegate to the RuntimeException
     */
    public SmartScriptParserException(String msg) {
        super(msg);
    }
}
