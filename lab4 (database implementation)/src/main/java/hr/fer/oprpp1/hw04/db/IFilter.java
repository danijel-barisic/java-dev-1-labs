package hr.fer.oprpp1.hw04.db;

/**
 * Interface for filtering student records.
 *
 * @author Danijel Barišić
 */
public interface IFilter {

    /**
     * Checks if a student record satisfies a condition.
     *
     * @param record record to examine
     * @return true if record satisfies the condition, false otherwise
     */
    public boolean accepts(StudentRecord record);
}
