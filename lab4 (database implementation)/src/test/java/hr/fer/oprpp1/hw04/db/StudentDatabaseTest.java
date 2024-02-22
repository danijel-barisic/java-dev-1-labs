package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDatabaseTest {

    List<String> dbLines;
    StudentDatabase sdb;

    @BeforeAll
    public void initDatabase() throws IOException {
        dbLines = Files.readAllLines(
                Paths.get("./src/test/resources/database.txt"),
                StandardCharsets.UTF_8
        );
        sdb = new StudentDatabase(dbLines);
    }

    @Test
    public void testForJMBAG() {
        StudentRecord sr = sdb.forJMBAG("0000000010");

        assertEquals(sr.getJmbag(), "0000000010");
        assertEquals(sr.getFirstName(), "Luka");
        assertEquals(sr.getLastName(), "Dokleja");
        assertEquals(sr.getFinalGrade(), 3);
    }

    @Test
    public void testFilterReturnsAllRecords() {
        List<StudentRecord> filteredStudents = sdb.filter(sr -> true);

        assertArrayEquals(sdb.getRecords().toArray(), filteredStudents.toArray());
    }

    @Test
    public void testFilterReturnsNoRecords() {
        List<StudentRecord> filteredStudents = sdb.filter(sr -> false);

        assertEquals(0, filteredStudents.size());
    }

}
