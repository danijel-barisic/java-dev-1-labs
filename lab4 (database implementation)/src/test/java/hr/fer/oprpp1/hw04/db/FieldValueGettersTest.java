package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldValueGettersTest {

    @Test
    public void testGetters() {
        StudentRecord sr = new StudentRecord("0036523232", "Ankica", "Kovač", 5);

        assertEquals("0036523232", FieldValueGetters.JMBAG.get(sr));
        assertEquals("Ankica", FieldValueGetters.FIRST_NAME.get(sr));
        assertEquals("Kovač", FieldValueGetters.LAST_NAME.get(sr));

    }
}
