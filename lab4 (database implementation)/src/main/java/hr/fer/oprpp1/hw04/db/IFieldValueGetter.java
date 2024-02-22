package hr.fer.oprpp1.hw04.db;

/**
 * Interface for record field getter, modelling a getter strategy.
 *
 * @author Danijel Barišić
 */
public interface IFieldValueGetter {

    /**
     * Gets the appropriate property's value from student record.
     *
     * @param record record to which the property belongs
     * @return value of the appropriate property, depending on the strategy
     */
    public String get(StudentRecord record);
}
