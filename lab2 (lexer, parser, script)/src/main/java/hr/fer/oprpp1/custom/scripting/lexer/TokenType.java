package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Types of tokens:
 * EOF - end of file
 * KEYWORD - reserved SmartScript word (only in tag state)
 * TEXT - regular text
 * OPERATOR - "+, - , *, /, ^" characters (only in tag state)
 * TAG_OPEN - initiates tag state
 * TAG_CLOSE - closes tag state (only in tag state)
 * DOUBLE - double value (only in tag state)
 * STRING - String value (only in tag state)
 * INTEGER - int value (only in tag state)
 * FUNCTION - function identifier (only in tag state)
 * VARIABLE - variable identifier (only in tag state)
 *
 * @Author Danijel Barišić
 */
public enum TokenType {
    EOF, KEYWORD, TEXT, OPERATOR, TAG_OPEN, TAG_CLOSE, DOUBLE, STRING, INTEGER, FUNCTION, VARIABLE
}
