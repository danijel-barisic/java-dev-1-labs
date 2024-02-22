package hr.fer.oprpp1.hw02.prob1;

/**
 * Lexer that performs simple lexical analysis.
 *
 * @Author Danijel Barišić
 */
public class Lexer {

    /**
     * Data/text to lexically analyse.
     */
    private char[] data;

    /**
     * Last fetched token.
     */
    private Token token;

    /**
     * Index of a character to process next.
     */
    private int currentIndex;

    /**
     * State the lexer is in (BASIC/EXTENDED).
     */
    private LexerState lexerState;

    /**
     * @param text input text for tokenization
     * @throws NullPointerException when input text is null
     */
    public Lexer(String text) {

        if (text == null) {
            throw new NullPointerException("Input text cannot be null.");
        }

        this.data = text.toCharArray();
        this.lexerState = LexerState.BASIC;
    }

    /**
     * Extracts next token from the text that's being analysed.
     *
     * @return next analysed token from the text being analysed
     * @throws LexerException for various mistakes during lexer analysis (no more tokens left to read, invalid escaping...)
     */
    public Token nextToken() {

        if (token != null && token.getType().equals(TokenType.EOF)) {
            throw new LexerException("There is no more tokens left to read.");
        }

        Token newToken;

        if (data.length == 0) {
            newToken = new Token(TokenType.EOF, null);
            token = newToken;
            return newToken;
        }

        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) { // skips whitespace
            currentIndex++;
        }

        if (currentIndex >= data.length) { // checks if all tokens are read (EOF)
            newToken = new Token(TokenType.EOF, null);
        } else if (lexerState.equals(LexerState.BASIC)) {

            if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') { // checks if token is a WORD

                StringBuilder sb = new StringBuilder();

                do {
                    if (data[currentIndex] == '\\') {

                        currentIndex++;
                        if (currentIndex >= data.length ||
                                !(Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\')) {
                            throw new LexerException("Invalid escaping.");
                        }

                        sb.append(data[currentIndex]);
                        currentIndex++;

                        continue;
                    }
                    sb.append(data[currentIndex]);
                    currentIndex++;
                } while (currentIndex < data.length
                        && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\'));

                newToken = new Token(TokenType.WORD, sb.toString());

            } else if (Character.isDigit(data[currentIndex])) { // checks if token is a NUMBER

                StringBuilder sb = new StringBuilder();

                do {
                    sb.append(data[currentIndex]);
                    currentIndex++;
                } while (currentIndex < data.length && Character.isDigit(data[currentIndex]));

                Long newTokenValue;
                try {
                    newTokenValue = Long.parseLong(sb.toString());
                } catch (NumberFormatException exc) {
                    throw new LexerException("Number is too big for token.");
                }

                newToken = new Token(TokenType.NUMBER, newTokenValue);

            } else {
                if (data[currentIndex] == '#') {
                    lexerState = LexerState.EXTENDED;
                }
                newToken = new Token(TokenType.SYMBOL, data[currentIndex]);
                currentIndex++;
            }
        } else {
            if (data[currentIndex] == '#') {
                lexerState = LexerState.BASIC;
                newToken = new Token(TokenType.SYMBOL, data[currentIndex]);
                currentIndex++;
            } else {
                StringBuilder sb = new StringBuilder();

                while (currentIndex < data.length &&
                        !(Character.isWhitespace(data[currentIndex]))) {

                    if (data[currentIndex] != '#') {
                        sb.append(data[currentIndex]);
                        currentIndex++;

                    } else {
                        lexerState = LexerState.BASIC;
                        break;
                    }
                }
                newToken = new Token(TokenType.WORD, sb.toString());
            }
        }

        token = newToken;
        return newToken;
    }

    /**
     * Returns last previously extracted token (doesn't advance the lexical analysis onto next token).
     *
     * @return last previously extracted token
     */
    public Token getToken() {
        return token;
    }

    /**
     * @param state state to put the Lexer in
     * @throws NullPointerException when provided state is null
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new NullPointerException("State cannot be null.");
        }
        lexerState = state;
    }


}
