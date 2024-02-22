package hr.fer.oprpp1.hw04.db.lexer;

import java.util.Objects;


/**
 * Model of a lexical token.
 *
 * @author Danijel Barišić
 */
public class Token {

    /**
     * Type of token.
     */
    private TokenType type;

    /**
     * Value of token.
     */
    private String value;

    /**
     * @param type  type of token
     * @param value value of token
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @return token type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @return token value
     */
    public String getValue() {
        return value;
    }

    /**
     * Compares two tokens based on type and value.
     *
     * @param o other token
     * @return true if types and values of both tokens are equal (via .equals()), false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (type != token.type) return false;
        return Objects.equals(value, token.value);
    }
}
