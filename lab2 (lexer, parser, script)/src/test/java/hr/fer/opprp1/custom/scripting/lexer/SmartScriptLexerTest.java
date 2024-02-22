package hr.fer.opprp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.lexer.LexerException;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptLexerTest {

    @Test
    public void testThrowsNullPointerExceptionWhenConstructorIsCalledWithNullReference() {
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
    }

    @Test
    public void testBasicEchoTagWithVariableIsFiveAppropriateTokensTotal() {

        // appropriate tokens are: TAG_OPEN, "=" keyword, variable i, TAG_CLOSE, EOF
        SmartScriptLexer lexer = new SmartScriptLexer("{$=i$}");

        assertEquals(TokenType.TAG_OPEN, lexer.nextToken().getType());
        assertEquals(new Token(TokenType.KEYWORD, "="), lexer.nextToken());
        assertEquals(new Token(TokenType.VARIABLE, "i"), lexer.nextToken());
        assertEquals(TokenType.TAG_CLOSE, lexer.nextToken().getType());
        assertEquals(TokenType.EOF, lexer.nextToken().getType());
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testForLoopTagWithStepExpressionIsSevenAppropriateTokensTotal() {

        // appropriate tokens are: TAG_OPEN, "=" keyword, variable i, TAG_CLOSE, EOF
        SmartScriptLexer lexer = new SmartScriptLexer("{$ FOR i 0 10 1 $}");

        assertEquals(TokenType.TAG_OPEN, lexer.nextToken().getType());
        assertEquals(new Token(TokenType.KEYWORD, "FOR"), lexer.nextToken());
        assertEquals(new Token(TokenType.VARIABLE, "i"), lexer.nextToken());
        assertEquals(new Token(TokenType.INTEGER, 0), lexer.nextToken());
        assertEquals(new Token(TokenType.INTEGER, 10), lexer.nextToken());
        assertEquals(new Token(TokenType.INTEGER, 1), lexer.nextToken());
        assertEquals(TokenType.TAG_CLOSE, lexer.nextToken().getType());
        assertEquals(TokenType.EOF, lexer.nextToken().getType());
        assertThrows(LexerException.class, lexer::nextToken);
    }

    @Test
    public void testThrowsLexerExceptionForInvalidEscapingInText(){
        SmartScriptLexer lexer = new SmartScriptLexer("Ovo je nevaljano \\g dada.");
        assertThrows(LexerException.class, lexer::nextToken);
    }

}
