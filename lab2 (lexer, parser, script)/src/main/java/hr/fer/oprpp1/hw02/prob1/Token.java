package hr.fer.oprpp1.hw02.prob1;

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
}
