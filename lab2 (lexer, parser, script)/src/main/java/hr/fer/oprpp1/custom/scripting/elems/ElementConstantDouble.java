package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Entity that belongs to a tag and contains value of type double.
 *
 * @Author Danijel Barišić
 */
public class ElementConstantDouble extends Element {

    /**
     * Value which the element represents.
     */
    private double value;

    /**
     * @param value value of the element
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * @return contained value of type double
     */
    public double getValue() {
        return value;
    }

    /**
     * @return text representation of the contained double
     */
    @Override
    public String asText() {
        return Double.toString(value);
    }

    /**
     * Compares two values of type double.
     *
     * @param o element whose value to compare with value in this element
     * @return true if difference between doubles is less than 10E-6, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementConstantDouble other = (ElementConstantDouble) o;

        if (Math.abs(this.value - other.value) < 10E-6) {
            return true;
        }

        return false;
    }
}
