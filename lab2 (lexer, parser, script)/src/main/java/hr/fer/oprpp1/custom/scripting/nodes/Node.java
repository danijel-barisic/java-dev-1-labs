package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Generic node class that models a syntactical parser node.
 *
 * @Author Danijel Barišić
 */
public class Node {

    /**
     * Direct children nodes of this node.
     */
    private ArrayIndexedCollection children;

    /**
     * Adds a new direct child node under this node.
     *
     * @param child new child node to add
     * @throws NullPointerException when the provided child node is null
     */
    public void addChildNode(Node child) {

        if (child == null) {
            throw new NullPointerException("New child node cannot be null.");
        }

        if (children == null) {
            children = new ArrayIndexedCollection();
        }

        children.add(child);
    }

    /**
     * @return number of direct children nodes belonging to this node
     */
    public int numberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    /**
     * @param index index of the child node to get
     * @return child node at the provided index
     * @throws IndexOutOfBoundsException when the provided index is out of the [0, numberOfChildren()] interval
     */
    public Node getChild(int index) {

        if (index >= children.size() || index < 0) {
            throw new IndexOutOfBoundsException("Index has to be a number between 0 and the total number of children.");
        }

        return (Node) children.get(index);
    }
}
