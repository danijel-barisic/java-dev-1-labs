package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {

    @Test
    public void testComparisonOperators() {
        String a = "a"; //less than b
        String b = "b"; //greater than a

        assertTrue(ComparisonOperators.LESS.satisfied(a, b));
        assertTrue(ComparisonOperators.GREATER.satisfied(b, a));
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(a, a));
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(a, b));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(b, b));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(b, a));
        assertTrue(ComparisonOperators.EQUALS.satisfied(a, a));
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(a, b));

    }

    @Test
    public void testLikeOperator() {
        String value1 = "Anica";
        String pattern1 = "A*ca";

        String value2 = "AAAA";
        String value3 = "AAA";
        String value4 = "AAAAAA";
        String pattern2 = "AA*AA";

        String pattern3 = "AA*";
        String pattern4 = "*AA";

        assertTrue(ComparisonOperators.LIKE.satisfied(value1, pattern1));
        assertTrue(ComparisonOperators.LIKE.satisfied(value2, pattern2));
        assertFalse(ComparisonOperators.LIKE.satisfied(value3, pattern2));
        assertTrue(ComparisonOperators.LIKE.satisfied(value4, pattern2));
        assertTrue(ComparisonOperators.LIKE.satisfied(value4, pattern3));
        assertTrue(ComparisonOperators.LIKE.satisfied(value4, pattern4));

    }

}
