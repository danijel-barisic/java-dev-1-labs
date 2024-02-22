package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * Node that models a for-loop.
 *
 * @Author Danijel Barišić
 */
public class ForLoopNode extends Node {

    /**
     * Loop iteration counter variable.
     */
    private ElementVariable variable;

    /**
     * Starting value of the counter.
     */
    private Element startExpression;

    /**
     * Ending value of the counter (end condition value).
     */
    private Element endExpression;

    /**
     * Step size of the counter.
     */
    private Element stepExpression;

    /**
     * @param variable        loop iteration counter variable
     * @param startExpression initial value of the counter
     * @param endExpression   ending value of the counter
     * @param stepExpression  step size of the counter (can be null)
     * @throws NullPointerException when variable, startExpression or endExpression are null
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {

        if (variable == null) {
            throw new NullPointerException("Variable element cannot be null.");
        }

        if (startExpression == null) {
            throw new NullPointerException("startExpression element cannot be null.");
        }

        if (endExpression == null) {
            throw new NullPointerException("endExpression element cannot be null.");
        }

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    /**
     * @return counter variable value
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * @return initial value of the counter
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * @return end value of the counter
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * @return step size of the counter
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * Checks if elements (/expressions) in two for-loop nodes are equal.
     *
     * @param o other for-loop node
     * @return true if all elements and all children of the two for-loop nodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForLoopNode other = (ForLoopNode) o;

        if (!variable.equals(other.variable)) return false;
        if (!startExpression.equals(other.startExpression)) return false;
        if (!endExpression.equals(other.endExpression)) return false;
        if (!(Objects.equals(this.stepExpression, other.stepExpression))) return false;

        if (this.numberOfChildren() != other.numberOfChildren()) {
            return false;
        }

        for (int i = 0; i < this.numberOfChildren(); i++) {
            if (!(this.getChild(i).equals(other.getChild(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return textual representation of the for-loop node, AKA. for-loop tag
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("{$ FOR ");
        sb.append(variable.asText() + " " + startExpression.asText() + " " + endExpression.asText()
                + ((stepExpression != null) ? (" " + stepExpression.asText()) : ""));
        sb.append(" $}");

        for (int i = 0; i < numberOfChildren(); i++) {
            sb.append(getChild(i).toString());
        }

        sb.append("{$END$}");

        return sb.toString();
    }

}
