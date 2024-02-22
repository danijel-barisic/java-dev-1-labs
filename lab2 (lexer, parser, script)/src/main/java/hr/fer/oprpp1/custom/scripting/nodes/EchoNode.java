package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Node that models text output.
 *
 * @Author Danijel Barišić
 */
public class EchoNode extends Node {

    /**
     * Elements within the echo node.
     */
    private Element[] elements;

    /**
     * @param elements array of elements to put in the echo node
     * @throws NullPointerException when the provided elements array is null
     */
    public EchoNode(Element[] elements) {
        if (elements == null) {
            throw new NullPointerException("Elements array cannot be null.");
        }
        this.elements = elements;
    }

    /**
     * @return internal array of elements in the echo node
     */
    public Element[] getElements() {
        return elements;
    }

    /**
     * Checks if elements of the two echo nodes are equal.
     *
     * @param o other echo node
     * @return true if two echo nodes contain identical elements, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EchoNode other = (EchoNode) o;

        if (this.elements.length != other.elements.length) {
            return false;
        }

        for (int i = 0; i < this.elements.length; i++) {
            if (!(this.elements[i].equals(other.elements[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return textual representation of the echo node, AKA. the echo tag
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("{$= ");
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i].asText() + " ");
        }
        sb.append("$}");

        return sb.toString();
    }
}
