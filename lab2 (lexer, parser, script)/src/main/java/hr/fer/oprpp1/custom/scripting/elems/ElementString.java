package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Entity that belongs to a tag and contains value of type string.
 *
 * @Author Danijel Barišić
 */
public class ElementString extends Element {

    /**
     * String value which the element represents.
     */
    private String value;

    /**
     * @param value string value of the element
     */
    public ElementString(String value) {
        this.value = value;
    }

    /**
     * @return contained value of type String
     */
    public String getValue() {
        return value;
    }

    /**
     * @return contained value of type String
     */
    @Override
    public String asText() {
        return value;
    }

    /**
     * Compares two values of type String.
     *
     * @param o element whose value to compare with value in this element
     * @return true if string values are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementString other = (ElementString) o;

        return Objects.equals(this.value, other.value);
    }

}
