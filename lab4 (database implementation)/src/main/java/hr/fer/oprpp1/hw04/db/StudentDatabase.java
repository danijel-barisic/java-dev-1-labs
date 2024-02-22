package hr.fer.oprpp1.hw04.db;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Database that keeps student records stored in an internal list,
 * as well as a hash map for fast retrieval of records with known JMBAGs.
 *
 * @author Danijel Barišić
 */
public class StudentDatabase {

    /**
     * All student records in the database.
     */
    private List<StudentRecord> records;

    /**
     * All student records in the database, indexed by JMBAG.
     */
    private Map<String, StudentRecord> index;

    /**
     * Initialises database with records.
     *
     * @param records list of records as strings (rows in the database)
     */
    public StudentDatabase(List<String> records) {

        this.records = new ArrayList<>();
        Set<String> uniqueJmbags = new HashSet<>();

        for (String record : records) {
            String[] studentAttr = record.split("\t");

            int finalGrade;
            try {
                finalGrade = Integer.parseInt(studentAttr[3]);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Not a number.");
            }

            if (finalGrade < 0 || finalGrade > 5) {
                throw new IllegalArgumentException("Grade isn't a number between 1 and 5.");
            }

            if (uniqueJmbags.contains(studentAttr[0])) {
                throw new IllegalArgumentException("The following jmbag is not unique: " + studentAttr[0]);
            }

            StudentRecord newStudentRecord = new StudentRecord(studentAttr[0], studentAttr[2], studentAttr[1], finalGrade);

            this.records.add(newStudentRecord);

            uniqueJmbags.add(newStudentRecord.getJmbag());

        }

        index = this.records.stream().collect(
                Collectors.toMap(StudentRecord::getJmbag, Function.identity(),
                        (o1, o2) -> o1, HashMap::new));

    }

    /**
     * @return list of records stored in the database
     */
    public List<StudentRecord> getRecords() {
        return records;
    }

    /**
     * @param jmbag JMBAG of the record to look for in the database through (fast) hash map
     * @return record with the given jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        return index.get(jmbag);
    }

    /**
     * Filters records according to the provided filter condition.
     *
     * @param filter filter condition upon which to accept or ignore a record
     * @return the list of records that satisfy the filter condition
     */
    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> filteredRecords = new ArrayList<>();

        for (StudentRecord record : records) {
            if (filter.accepts(record)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }

}
