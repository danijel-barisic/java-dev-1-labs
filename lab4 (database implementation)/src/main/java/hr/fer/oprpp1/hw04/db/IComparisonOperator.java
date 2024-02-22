package hr.fer.oprpp1.hw04.db;

/**
 * Interface for comparison operator.
 *
 * @author Danijel Barišić
 */
public interface IComparisonOperator {

    /**
     * Checks if comparison condition is true.
     *
     * @param value1 first value in comparison (AKA. left value)
     * @param value2 second value in comparison (AKA. right value)
     * @return true if comparison of values is true, false otherwise
     */
    boolean satisfied(String value1, String value2);
}
