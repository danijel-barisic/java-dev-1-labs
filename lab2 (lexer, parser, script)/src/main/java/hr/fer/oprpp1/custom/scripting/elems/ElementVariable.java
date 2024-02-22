package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

/**
 * Entity that belongs to a tag and contains variable name as string.
 *
 * @Author Danijel Barišić
 */
public class ElementVariable extends Element {

    /**
     * Name of the value which the element represents
     */
    private String name;

    /**
     * @param name name of the variable which the element will represent.
     */
    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * @return name of the variable the element represents
     */
    public String getName() {
        return name;
    }

    /**
     * @return name of the variable the element represents
     */
    @Override
    public String asText() {
        return name;
    }

    /**
     * Compares variable names contained in the elements being compared.
     *
     * @param o element whose variable name to compare with variable name in this element
     * @return true if variable names are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementVariable other = (ElementVariable) o;

        return Objects.equals(this.name, other.name);
    }

}
