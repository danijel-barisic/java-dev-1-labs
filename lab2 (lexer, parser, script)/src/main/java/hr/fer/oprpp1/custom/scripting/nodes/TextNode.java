package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.Objects;

/**
 * Node that models a piece of regular text.
 *
 * @Author Danijel Barišić
 */
public class TextNode extends Node {

    /**
     * Text written within the text node.
     */
    private String text;

    /**
     * @param text text to write into the text node
     * @throws NullPointerException when text string is null
     */
    public TextNode(String text) {
        if (text == null) {
            throw new NullPointerException("Text string cannot be null.");
        }
        this.text = text;
    }

    /**
     * @return text written in this text node
     */
    public String getText() {
        return text;
    }

    /**
     * Checks if text strings within the two text nodes are equal.
     *
     * @param o other text node
     * @return true if text strings are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextNode other = (TextNode) o;

        return Objects.equals(this.text, other.text);
    }

    @Override
    public String toString() {
        return text;
    }

}
