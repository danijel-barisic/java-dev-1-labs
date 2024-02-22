package hr.fer.oprpp1.hw04.db.lexer;

/**
 * Lexer that performs lexical analysis of a query.
 *
 * @author Danijel Barišić
 */
public class QueryLexer {

    /**
     * Length of "jmbag" attribute name.
     */
    private final int JMBAG_WORD_LENGTH = "jmbag".length();

    /**
     * Length of "firstName" attribute name.
     */
    private final int FIRSTNAME_WORD_LENGTH = "firstName".length();

    /**
     * Length of "lastName" attribute name.
     */
    private final int LASTNAME_WORD_LENGTH = "lastName".length();

    /**
     * Length of "and" keyword.
     */
    private final int AND_WORD_LENGTH = "and".length();

    /**
     * Length of "like" keyword.
     */
    private final int LIKE_WORD_LENGTH = "LIKE".length();

    /**
     * Query text to be lexically analysed.
     */
    private char[] data;

    /**
     * Last fetched token.
     */
    private Token token;

    /**
     * Index of the current character in lexer during analysis.
     */
    private int currentIndex;

    /**
     * Initialises lexer.
     *
     * @param text query text
     * @throws NullPointerException when query text is null/empty
     */
    public QueryLexer(String text) {
        if (text == null || text.length() == 0) {
            throw new NullPointerException("Query cannot be empty.");
        }

        this.data = text.toCharArray();
    }

    /**
     * Performs lexical analysis by extracting next token from query text.
     *
     * @return next token from query text
     * @throws QueryLexerException when there's no more tokens left to read or token is invalid
     */
    public Token nextToken() {

        if (token != null && token.getType().equals(TokenType.EOL)) {
            throw new QueryLexerException("There is no more tokens left to read.");
        }

        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
            currentIndex++;
        }

        Token newToken;

        if (currentIndex < data.length) {
            StringBuilder sb = new StringBuilder();

            if (Character.isLetter(data[currentIndex])) {
                do {
                    sb.append(data[currentIndex]);
                    currentIndex++;
                } while (currentIndex < data.length && Character.isLetter(data[currentIndex]));

                String word = sb.toString();
                if (word.equals("jmbag")) {
                    newToken = new Token(TokenType.ATTRIBUTE, "jmbag");
                } else if (word.equals("firstName")) {
                    newToken = new Token(TokenType.ATTRIBUTE, "firstName");
                } else if (word.equals("lastName")) {
                    newToken = new Token(TokenType.ATTRIBUTE, "lastName");
                } else if (word.equals("LIKE")) {
                    newToken = new Token(TokenType.OPERATOR, "LIKE");
                } else if (word.equalsIgnoreCase("and")) {
                    newToken = new Token(TokenType.AND, null);
                } else {
                    throw new QueryLexerException("Unknown token/word: \"" + word + "\"");
                }

            } else if (data[currentIndex] == '\"') {

                currentIndex++;
                if (currentIndex >= data.length) {
                    throw new QueryLexerException("Abrupt line finish whilst defining a literal.");
                }

                do {
                    sb.append(data[currentIndex]);
                    currentIndex++;
                } while (currentIndex < data.length
                        && (Character.isLetterOrDigit(data[currentIndex])
                        || data[currentIndex] == '*'));

                if (currentIndex < data.length && data[currentIndex] == '\"') {
                    newToken = new Token(TokenType.LITERAL, sb.toString());
                    currentIndex++;
                } else {
                    throw new QueryLexerException("Invalid literal format.");
                }
            } else if (data[currentIndex] == '<' || data[currentIndex] == '>') {
                if (currentIndex + 1 < data.length && data[currentIndex + 1] == '=') {
                    newToken = new Token(TokenType.OPERATOR, data[currentIndex] + "=");
                    currentIndex += 2;
                } else {
                    newToken = new Token(TokenType.OPERATOR, String.valueOf(data[currentIndex]));
                    currentIndex++;
                }
            } else if (data[currentIndex] == '!') {
                if (currentIndex + 1 < data.length && data[currentIndex + 1] == '=') {
                    newToken = new Token(TokenType.OPERATOR, data[currentIndex] + "=");
                    currentIndex += 2;
                } else {
                    throw new QueryLexerException("Invalid operator.");
                }
            } else if (data[currentIndex] == '=') {
                newToken = new Token(TokenType.OPERATOR, "=");
                currentIndex++;
            } else {
                throw new QueryLexerException("Invalid token.");
            }
        } else {
            newToken = new Token(TokenType.EOL, null);
        }

        token = newToken;
        return newToken;
    }

    /**
     * @return last fetched token
     */
    public Token getToken() {
        return token;
    }

}
