package hr.fer.oprpp1.custom.collections;

/**
 * Functional interface that returns a boolean depending on whether the object satisfies.
 *
 * @Author Danijel Barišić
 */
public interface Tester {

    /**
     * Checks whether an object satisfies a condition.
     *
     * @param obj object to test/check
     * @return true if object satisfies a condition, false if otherwise
     */
    boolean test(Object obj);
}
