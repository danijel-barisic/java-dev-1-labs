package hr.fer.oprpp1.hw04.db;

import java.util.regex.Pattern;

/**
 * Contains static utility lambdas for comparison operations.
 *
 * @author Danijel Barišić
 */
public class ComparisonOperators {

    /**
     * Returns true if first argument is less than second, false otherwise.
     */
    public static final IComparisonOperator LESS = (arg1, arg2) -> arg1.compareTo(arg2) < 0;

    /**
     * Returns true if first argument is less than or equal to second, false otherwise.
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (arg1, arg2) -> arg1.compareTo(arg2) <= 0;

    /**
     * Returns true if first argument is greater than second, false otherwise.
     */
    public static final IComparisonOperator GREATER = (arg1, arg2) -> arg1.compareTo(arg2) > 0;

    /**
     * Returns true if first argument is greater than or equal to second, false otherwise.
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (arg1, arg2) -> arg1.compareTo(arg2) >= 0;

    /**
     * Returns true if first argument is equal to second, false otherwise.
     */
    public static final IComparisonOperator EQUALS = (arg1, arg2) -> arg1.compareTo(arg2) == 0;

    /**
     * Returns true if first argument is not equal to second, false otherwise.
     */
    public static final IComparisonOperator NOT_EQUALS = (arg1, arg2) -> arg1.compareTo(arg2) != 0;

    /**
     * Returns true if value (first argument) fits the pattern (second argument).
     * For pattern, you can use alphanumeric characters and wildcard * for any sequence (once).
     */
    public static final IComparisonOperator LIKE = new IComparisonOperator() {

        /**
         * @param value value to match
         * @param pattern pattern to match the value to
         * @return true if value fits the pattern
         */
        @Override
        public boolean satisfied(String value, String pattern) {

            boolean oneWildcardAlreadyFound = false;

            for (Character c : pattern.toCharArray()) {
                if (c.equals('*')) {
                    if (oneWildcardAlreadyFound) {
                        throw new IllegalArgumentException("There can't be multiple wildcards in a pattern.");
                    }
                    oneWildcardAlreadyFound = true;
                }
            }

            if (!oneWildcardAlreadyFound) {
                return value.equals(pattern);
            }

            String regexCompatiblePattern = pattern.replace("*", ".*?");

            return Pattern.matches(regexCompatiblePattern, value);
        }
    };
}
