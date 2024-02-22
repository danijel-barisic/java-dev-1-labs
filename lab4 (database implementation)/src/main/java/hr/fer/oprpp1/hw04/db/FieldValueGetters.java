package hr.fer.oprpp1.hw04.db;


/**
 * Getter lambdas for StudentRecord properties.
 *
 * @author Danijel Barišić
 */
public class FieldValueGetters {

    /**
     * Getter lambda for firstName, returns firstName from the provided record.
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    /**
     * Getter lambda for lastName, returns lastName from the provided record.
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    /**
     * Getter lambda for JMBAG, returns JMBAG from the provided record.
     */
    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
