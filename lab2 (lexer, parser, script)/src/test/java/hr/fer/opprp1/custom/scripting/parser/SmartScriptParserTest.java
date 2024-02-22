package hr.fer.opprp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;

import static org.junit.jupiter.api.Assertions.*;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SmartScriptParserTest {

    // primjer 1
    @Test
    public void testOneTextNode() {
        String text = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        assertEquals(1, document.numberOfChildren());
        assertTrue(document.getChild(0) instanceof TextNode);
    }

    // primjer 2
    @Test
    public void testOneEscapedTextNode() {
        String text = readExample(2);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        assertEquals(1, document.numberOfChildren());
        assertTrue(document.getChild(0) instanceof TextNode);
    }

    // primjer 3
    @Test
    public void testOneMultiEscapedTextNode() {
        String text = readExample(3);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        assertEquals(1, document.numberOfChildren());
        assertTrue(document.getChild(0) instanceof TextNode);
    }

    // primjer 4
    @Test
    public void testThrowsSmartScriptParserExceptionInvalidEscaping() {
        String text = readExample(4);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    // primjer 5
    @Test
    public void testThrowsSmartScriptParserExceptionInvalidEscapingAndQuotes() {
        String text = readExample(5);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    // primjer 6
    @Test
    public void testThreeNodesInTotalTwoTextOneEcho() {
        String text = readExample(6);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        assertEquals(3, document.numberOfChildren());
        assertTrue(document.getChild(0) instanceof TextNode);
        assertTrue(document.getChild(1) instanceof EchoNode);
        assertTrue(document.getChild(2) instanceof TextNode);
    }

    // primjer 7
    @Test
    public void testThreeNodesInTotalTwoTextOneEchoAndTagWithNewlineEscapeWorksFine() {
        String text = readExample(7);
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode document = parser.getDocumentNode();
        assertEquals(3, document.numberOfChildren());
        assertTrue(document.getChild(0) instanceof TextNode);
        assertTrue(document.getChild(1) instanceof EchoNode);
        assertTrue(document.getChild(2) instanceof TextNode);
    }

    // primjer 8
    @Test
    public void testThrowsSmartScriptParserExceptionForEscapingInvalidCharacter() {
        String text = readExample(8);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    // primjer 9
    @Test
    public void testThrowsSmartScriptParserExceptionForEscapingOutsideOfString() {
        String text = readExample(9);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testEqualDocumentNodes() {
        String docBody = "{$ FOR i 1 10 5 $}\n" +
                "{$FOR j 0 9 $}\n" +
                "{$FOR k 0 9 $}\n" +
                " For inside for inside for.\n" +
                " Here are the counters: {$= i \"j\\n is middle loop counter\" k $}\n" +
                "{$END$}\n" +
                "{$END$}\n" +
                "                              - that's a lotta space\n" +
                "  {$= \"Outermost loop ending\" $}\n" +
                "{$END$}\n";
        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        String originalDocumentBody = document.toString();
        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();

        // now document and document2 should be structurally identical trees
        assertEquals(document, document2);
    }

    @Test
    public void testThrowsSmartScriptParserExceptionWhenConstructorIsCalledWithNullReference() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(null));
    }

    private String readExample(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
            if (is == null) throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch (IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }
}
