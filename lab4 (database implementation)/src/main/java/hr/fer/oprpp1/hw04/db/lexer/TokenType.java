package hr.fer.oprpp1.hw04.db.lexer;


/**
 * Types of tokens:
 * AND - logical AND operator for stringing conditional expressions in a query together
 * <p>
 * ATTRIBUTE - a column name in database
 * <p>
 * OPERATOR - comparison operator (<, >, <=, >=, =, !=, LIKE)
 * <p>
 * LITERAL - string literal
 * <p>
 * EOL - end of line
 *
 * @author Danijel Barišić
 */
public enum TokenType {
    AND, ATTRIBUTE, OPERATOR, LITERAL, EOL
}
