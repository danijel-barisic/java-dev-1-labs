package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Node that models an entire document, root node.
 *
 * @Author Danijel Barišić
 */
public class DocumentNode extends Node {

    /**
     * Checks if two document nodes are syntactically equal (i.e. if all the children nodes are equal).
     *
     * @param o other document node
     * @return true if all children from both document nodes correspond in order and value, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentNode other = (DocumentNode) o;

        if (this.numberOfChildren() != other.numberOfChildren()) {
            return false;
        }

        for (int i = 0; i < numberOfChildren(); i++) {
            if (!(this.getChild(i).equals(other.getChild(i)))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return textual representation of the document node, comprised of textual representation of its individual children
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfChildren(); i++) {
            sb.append(getChild(i).toString());
        }

        return sb.toString();
    }
}
