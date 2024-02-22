package hr.fer.oprpp1.hw04.db;


/**
 * Model of a query filtering condition.
 *
 * @author Danijel Barišić
 */
public class ConditionalExpression {

    /**
     * Field getter corresponding to the attribute type.
     */
    private final IFieldValueGetter fieldGetter;

    /**
     * String literal that's in comparison with the attribute.
     */
    private final String stringLiteral;

    /**
     * Operator that decides the comparison operation.
     */
    private final IComparisonOperator comparisonOperator;

    /**
     * Defines conditional expression.
     *
     * @param fieldGetter field getter
     * @param stringLiteral string literal
     * @param comparisonOperator comparison operator
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
                                 IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * @return field getter corresponding to the attribute in the conditional expression
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * @return string literal corresponding to the string literal in the conditional expression
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * @return comparison operator used in the conditional expression
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
