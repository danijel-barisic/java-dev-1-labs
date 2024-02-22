package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Entity that belongs to a tag and contains name of the function.
 *
 * @Author Danijel Barišić
 */
public class ElementFunction extends Element {

    /**
     * Name of the function the element represents.
     */
    private String name;

    /**
     * @param name name of the function the element will represent.
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * @return name of the function the element represents
     */
    public String getName() {
        return name;
    }

    /**
     * @return name of the function the element represents
     */
    @Override
    public String asText() {
        return name;
    }

    /**
     * Compares function names contained in the elements being compared.
     *
     * @param o element whose function name to compare with function name in this element
     * @return true if function names are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementFunction other = (ElementFunction) o;

        return Objects.equals(this.name, other.name);
    }

}
