package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Entity that belongs to a tag and contains value of type integer.
 *
 * @Author Danijel Barišić
 */
public class ElementConstantInteger extends Element {

    /**
     * Value which the element represents.
     */
    private int value;

    /**
     * @param value value of the element
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * @return contained value of type int
     */
    public int getValue() {
        return value;
    }

    /**
     * @return text representation of the contained int
     */
    @Override
    public String asText() {
        return Integer.toString(value);
    }

    /**
     * Compares two values of type int.
     *
     * @param o element whose value to compare with value in this element
     * @return true if int values are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementConstantInteger other = (ElementConstantInteger) o;

        return this.value == other.value;
    }

}
