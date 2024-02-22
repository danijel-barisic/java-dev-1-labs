package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Entity that belongs to a tag and contains an operator as string value.
 *
 * @Author Danijel Barišić
 */
public class ElementOperator extends Element {

    /**
     * Operator which the element represents.
     */
    private String symbol;

    /**
     * @param symbol operator which the element will represent.
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return contained operator as String
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return contained operator as String
     */
    @Override
    public String asText() {
        return symbol;
    }

    /**
     * Compares two operators as values of type String.
     *
     * @param o element whose operator value to compare with value in this element
     * @return true if operators in both String values are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementOperator other = (ElementOperator) o;

        return Objects.equals(this.symbol, other.symbol);
    }

}
