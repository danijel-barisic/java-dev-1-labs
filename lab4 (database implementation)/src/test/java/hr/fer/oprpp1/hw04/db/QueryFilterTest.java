package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryFilterTest {

    @Test
    public void testFilter() {
        StudentRecord r1 = new StudentRecord("0036512345", "Marin", "Pavlovic", 5);
        StudentRecord r2 = new StudentRecord("0036512345", "Jovan", "Knez", 4);

        String queryString = "jmbag =\"0036512345\" aNd lastName LIKE \"Pa*\" And firstName <= \"N\"";
        QueryParser parser = new QueryParser(queryString);
        QueryFilter filter = new QueryFilter(parser.getQuery());

        assertTrue(filter.accepts(r1));
        assertFalse(filter.accepts(r2));
    }
}
