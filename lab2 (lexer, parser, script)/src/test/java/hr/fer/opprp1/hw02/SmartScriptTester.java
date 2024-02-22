package hr.fer.opprp1.hw02;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SmartScriptTester {

    public static void main(String[] args) {

        if (args.length != 1) {
            throw new IllegalArgumentException("One argument expected.");
        }
        String filepath = args[0];

        String docBody;
        try {
            docBody = new String(
                    Files.readAllBytes(Paths.get(filepath)),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exc) {
            throw new SmartScriptParserException("Can't read file for testing.");
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            e.printStackTrace();
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        System.out.println(originalDocumentBody); // should write something like original content of docBody

    }
}
