package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.lexer.QueryLexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

    @Test
    public void testParsing() {
        String queryString = "jmbag =\"0036512345\" aNd lastName LIKE \"BO*\" And firstName <= \"G\"";
        QueryParser parser = new QueryParser(queryString);

        assertEquals(FieldValueGetters.JMBAG, parser.getQuery().get(0).getFieldGetter());
        assertEquals(ComparisonOperators.EQUALS, parser.getQuery().get(0).getComparisonOperator());
        assertEquals("0036512345", parser.getQuery().get(0).getStringLiteral());

        assertEquals(FieldValueGetters.LAST_NAME, parser.getQuery().get(1).getFieldGetter());
        assertEquals(ComparisonOperators.LIKE, parser.getQuery().get(1).getComparisonOperator());
        assertEquals("BO*", parser.getQuery().get(1).getStringLiteral());

        assertEquals(FieldValueGetters.FIRST_NAME, parser.getQuery().get(2).getFieldGetter());
        assertEquals(ComparisonOperators.LESS_OR_EQUALS, parser.getQuery().get(2).getComparisonOperator());
        assertEquals("G", parser.getQuery().get(2).getStringLiteral());
    }

    @Test
    public void testDirectQuery() {
        String queryString = "jmbag =\"0036512345\"";
        QueryParser parser = new QueryParser(queryString);

        assertEquals(FieldValueGetters.JMBAG, parser.getQuery().get(0).getFieldGetter());
        assertEquals(ComparisonOperators.EQUALS, parser.getQuery().get(0).getComparisonOperator());
        assertEquals("0036512345", parser.getQuery().get(0).getStringLiteral());

        assertTrue(parser.isDirectQuery());
        assertEquals("0036512345", parser.getQueriedJMBAG());
    }

}
