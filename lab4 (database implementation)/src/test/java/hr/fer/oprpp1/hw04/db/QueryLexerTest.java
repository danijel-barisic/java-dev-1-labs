package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.lexer.QueryLexer;
import hr.fer.oprpp1.hw04.db.lexer.Token;
import hr.fer.oprpp1.hw04.db.lexer.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryLexerTest {

    @Test
    public void testLexicalAnalysis() {
        String queryString = "jmbag =\"0036512345\" aNd lastName LIKE \"BO*\" And firstName <= \"G\"";
        QueryLexer lexer = new QueryLexer(queryString);

        assertEquals(new Token(TokenType.ATTRIBUTE, "jmbag"), lexer.nextToken());
        assertEquals(new Token(TokenType.OPERATOR, "="), lexer.nextToken());
        assertEquals(new Token(TokenType.LITERAL, "0036512345"), lexer.nextToken());
        assertEquals(new Token(TokenType.AND, null), lexer.nextToken());
        assertEquals(new Token(TokenType.ATTRIBUTE, "lastName"), lexer.nextToken());
        assertEquals(new Token(TokenType.OPERATOR, "LIKE"), lexer.nextToken());
        assertEquals(new Token(TokenType.LITERAL, "BO*"), lexer.nextToken());
        assertEquals(new Token(TokenType.AND, null), lexer.nextToken());
        assertEquals(new Token(TokenType.ATTRIBUTE, "firstName"), lexer.nextToken());
        assertEquals(new Token(TokenType.OPERATOR, "<="), lexer.nextToken());
        assertEquals(new Token(TokenType.LITERAL, "G"), lexer.nextToken());
        assertEquals(new Token(TokenType.EOL, null), lexer.nextToken());

    }
}
