package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Model of a student record row from the database.
 *
 * @author Danijel Barišić
 */
public class StudentRecord {

    /**
     * Student's JMBAG.
     */
    private String jmbag;

    /**
     * Student's first name.
     */
    private String firstName;

    /**
     * Student's last name.
     */
    private String lastName;

    /**
     * Student's final grade.
     */
    private int finalGrade;

    /**
     * Initialises student record.
     *
     * @param jmbag      student's JMBAG
     * @param firstName  student's first name
     * @param lastName   student's last name
     * @param finalGrade student's final grade
     */
    public StudentRecord(String jmbag, String firstName, String lastName, int finalGrade) {
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalGrade = finalGrade;
    }

    /**
     * @return student's JMBAG
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * @return student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return student's final grade
     */
    public int getFinalGrade() {
        return finalGrade;
    }

    /**
     * Checks if student records have equal JMBAGs.
     *
     * @param o other student record
     * @return true if JMBAGs of both records are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentRecord that = (StudentRecord) o;

        return Objects.equals(jmbag, that.jmbag);
    }

    /**
     * @return JMBAG's hashCode()
     */
    @Override
    public int hashCode() {
        return jmbag != null ? jmbag.hashCode() : 0;
    }
}
