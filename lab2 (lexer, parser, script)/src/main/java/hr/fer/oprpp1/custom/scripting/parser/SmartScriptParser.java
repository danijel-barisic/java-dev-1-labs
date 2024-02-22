package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * Parser that performs simple syntactical analysis.
 *
 * @Author Danijel Barišić
 */
public class SmartScriptParser {

    /**
     * Document node that the parser builds according to the token stream from lexer.
     */
    private DocumentNode documentNode;

    /**
     * Lexer that provides a token stream for parsing.
     */
    private SmartScriptLexer lexer; // for production of tokens

    /**
     * Stack used for tracking opening tokens and closing tokens of for-loops, to appropriately build the document node.
     */
    private ObjectStack nodeStack;

    /**
     * @param documentBody text of a document to parse
     * @throws SmartScriptParserException when document body is null, and for any exception during the parsing operation
     */
    public SmartScriptParser(String documentBody) {

        if (documentBody == null) {
            throw new SmartScriptParserException("Document body cannot be null.");
        }

        try {
            lexer = new SmartScriptLexer(documentBody);
            parse();
        } catch (Exception exc) {
            throw new SmartScriptParserException(exc.getMessage());
        }
    }

    /**
     * Method for parsing the tokens provided by the internal lexer n building the document node.
     *
     * @throws SmartScriptParserException for various exceptions throughout the parsing operation (unexpected element, too many elements...)
     */
    public void parse() {

        nodeStack = new ObjectStack();
        documentNode = new DocumentNode();
        nodeStack.push(documentNode);

        lexer.nextToken();

        while (!(lexer.getToken().getType().equals(TokenType.EOF))) {
            Node parentNode = (Node) nodeStack.peek();

            if (lexer.getToken().getType().equals(TokenType.TEXT)) {
                Node newTextNode = new TextNode(lexer.getToken().getValue().toString());
                parentNode.addChildNode(newTextNode);

                lexer.nextToken(); // move to open-tag or EOF
            } else if (lexer.getToken().getType().equals(TokenType.TAG_OPEN)) {

                lexer.nextToken(); // move to keyword

                if (lexer.getToken().equals(new Token(TokenType.KEYWORD, "FOR"))) {
                    ElementVariable variable;
                    Element startExpression, endExpression, stepExpression; // stepExpression can be null, i.e. closing tag can follow

                    lexer.nextToken(); // move to variable
                    if (!(lexer.getToken().getType().equals(TokenType.VARIABLE))) {
                        throw new SmartScriptParserException("Unexpected element (variable expected).");
                    }
                    variable = new ElementVariable(lexer.getToken().getValue().toString());

                    startExpression = parseExpressionInFor(false); // move to startExpression
                    endExpression = parseExpressionInFor(false); // move to endExpression
                    stepExpression = parseExpressionInFor(true); // move to step-expression / close-tag if null

                    if (stepExpression != null) {
                        lexer.nextToken(); //move to close-tag
                        if (!(lexer.getToken().getType().equals(TokenType.TAG_CLOSE))) {
                            throw new SmartScriptParserException("Too many arguments");
                        }
                    }

                    Node newForLoopNode = new ForLoopNode(variable, startExpression, endExpression, stepExpression);
                    parentNode.addChildNode(newForLoopNode);
                    nodeStack.push(newForLoopNode);

                    lexer.nextToken(); // move beyond close-tag
                } else if (lexer.getToken().equals(new Token(TokenType.KEYWORD, "END"))) {

                    lexer.nextToken();
                    if (!(lexer.getToken().getType().equals(TokenType.TAG_CLOSE))) {
                        throw new SmartScriptParserException("End tag cannot have arguments.");
                    }
                    lexer.nextToken(); // move beyond close-tag
                    nodeStack.pop();

                    if (nodeStack.isEmpty()) {
                        throw new SmartScriptParserException("Too many END tags (more than FOR tags).");
                    }
                } else if (lexer.getToken().equals(new Token(TokenType.KEYWORD, "="))) {

                    ArrayIndexedCollection elements = new ArrayIndexedCollection();

                    while (!(lexer.nextToken().getType().equals(TokenType.TAG_CLOSE))) {
                        if (lexer.getToken().getType().equals(TokenType.EOF)) {
                            throw new SmartScriptParserException("Reached EOF before closing echo tag.");
                        }
                        Element newElement = parseExpressionInEcho();
                        elements.add(newElement);
                    }

                    Element[] elementsArray = new Element[elements.size()];

                    for (int i = 0; i < elements.size(); i++) {
                        elementsArray[i] = (Element) elements.get(i);
                    }

                    Node newEchoNode = new EchoNode(elementsArray);

                    parentNode.addChildNode(newEchoNode);

                    lexer.nextToken(); // move beyond close-tag
                } else {
                    throw new SmartScriptParserException("Invalid keyword.");
                }
            }
        }
    }

    /**
     * @return document node that was built by parsing the document text
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Helper method for parsing expressions/elements of the for-loop tag.
     *
     * @param isFourthExpression tells whether the expression being parsed is the fourth expression (stepExpression) in the for-loop
     * @return next element within the for-loop tag
     * @throws SmartScriptParserException for various mistakes during parsing operation of for-loop tag (too few arguments, function elements not supported...)
     */
    private Element parseExpressionInFor(boolean isFourthExpression) {

        Element newElement;

        lexer.nextToken();
        if (lexer.getToken().getType().equals(TokenType.VARIABLE)) {
            newElement = new ElementVariable(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.INTEGER)) {
            newElement = new ElementConstantInteger((Integer) lexer.getToken().getValue());
        } else if (lexer.getToken().getType().equals(TokenType.DOUBLE)) {
            newElement = new ElementConstantDouble((Double) lexer.getToken().getValue());
        } else if (lexer.getToken().getType().equals(TokenType.OPERATOR)) {
            newElement = new ElementOperator(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.STRING)) {
            newElement = new ElementString(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.FUNCTION)) {
            throw new SmartScriptParserException("Function element not supported in for-loop.");
        } else if (lexer.getToken().getType().equals(TokenType.TAG_CLOSE)) {
            if (!isFourthExpression) {
                throw new SmartScriptParserException("Too few arguments");
            }
            newElement = null;
        } else {
            throw new SmartScriptParserException("Unexpected element.");
        }

        return newElement;
    }

    private Element parseExpressionInEcho() {

        Element newElement;

        if (lexer.getToken().getType().equals(TokenType.VARIABLE)) {
            newElement = new ElementVariable(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.INTEGER)) {
            newElement = new ElementConstantInteger((Integer) lexer.getToken().getValue());
        } else if (lexer.getToken().getType().equals(TokenType.DOUBLE)) {
            newElement = new ElementConstantDouble((Double) lexer.getToken().getValue());
        } else if (lexer.getToken().getType().equals(TokenType.OPERATOR)) {
            newElement = new ElementOperator(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.STRING)) {
            newElement = new ElementString(lexer.getToken().getValue().toString());
        } else if (lexer.getToken().getType().equals(TokenType.FUNCTION)) {
            newElement = new ElementFunction(lexer.getToken().getValue().toString());
        } else {
            throw new SmartScriptParserException("Unexpected element.");
        }

        return newElement;
    }
}
