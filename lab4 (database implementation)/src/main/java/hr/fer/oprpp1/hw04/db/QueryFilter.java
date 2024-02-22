package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Implementation of IFilter for queries.
 *
 * @author Danijel Barišić
 */
public class QueryFilter implements IFilter {

    /**
     * List of conditional expressions to filter records with.
     */
    private List<ConditionalExpression> query;

    /**
     * Initialises conditional expression list.
     *
     * @param query conditional expression list
     */
    public QueryFilter(List<ConditionalExpression> query) {
        this.query = query;
    }

    /**
     * Checks if a student record satisfies the list of conditional expressions.
     *
     * @param record record to examine
     * @return true if record satisfies all conditions in the list, false otherwise
     */
    @Override
    public boolean accepts(StudentRecord record) {

        for (ConditionalExpression expr : query) {
            if (!(expr.getComparisonOperator()
                    .satisfied(expr.getFieldGetter().get(record), expr.getStringLiteral()))) {
                return false;
            }
        }
        return true;
    }
}
