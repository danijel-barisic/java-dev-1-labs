package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Objects;

/**
 * Class that models a lexical unit AKA. token.
 *
 * @Author Danijel Barišić
 */
public class Token {

    /**
     * Type of this token.
     */
    private TokenType type;

    /**
     * Value that this token contains.
     */
    private Object value;

    /**
     * @param type  type of token
     * @param value value of token
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @return value of this token
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return type of this token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Checks if two tokens are equal by type and value.
     *
     * @param o other token
     * @return true if token values and types are equal.
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
