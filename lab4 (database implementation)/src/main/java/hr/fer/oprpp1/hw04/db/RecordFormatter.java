package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for formatting the query output.
 *
 * @author Danijel Barišić
 */
public class RecordFormatter {

    /**
     * Formats the query output.
     *
     * @param records result records of a priorly executed query
     * @return formatted output as a list of strings representing lines
     */
    public static List<String> format(List<StudentRecord> records) {

        if (records.size() == 0) {
            return null;
        }

        List<String> output = new ArrayList<>();

        int jmbagLen = 10;
        Optional<String> optMaxLastNameLen = records.stream()
                .map(StudentRecord::getLastName).max(Comparator.comparing(String::length));
        int maxLastNameLen = optMaxLastNameLen.map(String::length).orElse(0);
        Optional<String> optMaxFirstNameLen = records.stream()
                .map(StudentRecord::getFirstName).max(Comparator.comparing(String::length));
        int maxFirstNameLen = optMaxFirstNameLen.map(String::length).orElse(0);
        int gradeLen = 1;

        StringBuilder sbFrame = new StringBuilder();
        sbFrame.append("+");
        for (int i = 0; i < jmbagLen + 2; i++) sbFrame.append("=");
        sbFrame.append("+");
        for (int i = 0; i < maxLastNameLen + 2; i++) sbFrame.append("=");
        sbFrame.append("+");
        for (int i = 0; i < maxFirstNameLen + 2; i++) sbFrame.append("=");
        sbFrame.append("+");
        for (int i = 0; i < gradeLen + 2; i++) sbFrame.append("=");
        sbFrame.append("+");

        String frame = sbFrame.toString();
        output.add(frame);

        for (StudentRecord r : records) {
            String lastName = r.getLastName();
            String firstName = r.getFirstName();

            String line = String.format("| %s | %s%s | %s%s | %d |", r.getJmbag(),
                    lastName, " ".repeat(maxLastNameLen - lastName.length()),
                    firstName, " ".repeat(maxFirstNameLen - firstName.length()),
                    r.getFinalGrade());

            output.add(line);
        }
        output.add(frame);

        return output;
    }
}
