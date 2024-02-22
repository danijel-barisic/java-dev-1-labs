package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Lexer that performs simple lexical analysis.
 *
 * @Author Danijel Barišić
 */
public class SmartScriptLexer {

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
     * State the lexer is in (TEXT/TAG).
     */
    private LexerState lexerState;

    /**
     * Initialises lexer with text to lexically analyse.
     *
     * @param text input text for tokenization
     * @throws NullPointerException when provided text is null
     */
    public SmartScriptLexer(String text) {
        if (text == null) {
            throw new NullPointerException("Input text cannot be null.");
        }
        this.data = text.toCharArray();
        lexerState = LexerState.TEXT;
    }

    /**
     * Extracts next token from the text that's being analysed, according to SmartScript rules.
     *
     * @return next analysed token from the text being analysed
     * @throws LexerException        which is a generic exception for plethora of Lexer operation mistakes (invalid keyword, invalid tag...)
     * @throws NumberFormatException when a double constant contains multiple dots
     */
    public Token nextToken() { // lexer only fetches next token, doesn't check parsing rules, i.e. node format rules.

        boolean pendingText = false; // indicates whether there's accumulated text that hasn't been tokenized yet

        if (token != null && token.getType().equals(TokenType.EOF)) {
            throw new LexerException("There is no more tokens left to read.");
        }

        Token newToken;

        if (data.length == 0) {
            newToken = new Token(TokenType.EOF, null);
            token = newToken;
            return newToken;
        }

        if (currentIndex >= data.length) {
            newToken = new Token(TokenType.EOF, null);
        } else if (lexerState == LexerState.TEXT) {

            StringBuilder sb = new StringBuilder();
            do {

                if (data[currentIndex] == '\\') {  // escaping
                    currentIndex++;
                    if (currentIndex >= data.length
                            || (data[currentIndex] != '{' && data[currentIndex] != '\\')) {
                        throw new LexerException("Invalid escaping.");
                    }
                } else if (data[currentIndex] == '{') {
                    currentIndex++;

                    if (currentIndex >= data.length) {
                        sb.append('{'); // retroactive append of '{'
                        break;
                    } else if (data[currentIndex] == '$') { // tag begin confirmed, the unescaped "{$"

                        // You only know TEXT ended when TAG is confirmed. How to return TEXT token first, TAG_OPEN second?
                        // In order not to lose a concurrent TEXT token:

                        //if{} tokenizes the TAG_OPEN
                        if (pendingText == false) { /* note: nothing else besides text could be PENDING in state of LexerState.TEXT
                            when token is null, text could still be pending for tokenization.
                            Same is with TAG_CLOSE (i.e. maybe there's pending text after TAG_CLOSE)*/
                            lexerState = LexerState.TAG;
                            currentIndex++;

                            newToken = new Token(TokenType.TAG_OPEN, null);
                            token = newToken;
                            return newToken;
                        } else { /* else{} is executed when token!=null i.e. the tag ain't the first thing in the document (i.e. tag is somewhere in the middle of text,
                            not the beginning), even if the thing before it is spaces (which is still considered TEXT).
                            else{} tokenizes the TEXT before TAG_OPEN, when TAG_OPEN sequence ("{$") is reached*/

                            currentIndex--; // bring back the index onto "{" for convenience.

                            break;
                        }
                    }
                }

                // cut off the text token where tag begins, n return it
                sb.append(data[currentIndex]);
                currentIndex++;
                pendingText = true;
            } while (currentIndex < data.length);

            newToken = new Token(TokenType.TEXT, sb.toString());

        } else { //LexerState.TAG state

            final int FOR_KEYWORD_LENGTH = 3;
            final int ECHO_KEYWORD_LENGTH = 1; // "="
            final int END_KEYWORD_LENGTH = 3;

            while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) { // skips whitespace
                currentIndex++;
            }

            //FOR keyword
            if (token.getType().equals(TokenType.TAG_OPEN)) { // start of the tag, once per tag obviously. Determines keyword
                if (currentIndex + FOR_KEYWORD_LENGTH < data.length
                        && String.valueOf(data).toUpperCase()
                        .substring(currentIndex, currentIndex + FOR_KEYWORD_LENGTH)
                        .equals("FOR")) {
                    newToken = new Token(TokenType.KEYWORD, "FOR");
                    currentIndex += FOR_KEYWORD_LENGTH;
                }
                //END keyword
                else if (currentIndex + END_KEYWORD_LENGTH < data.length
                        && String.valueOf(data).toUpperCase()
                        .substring(currentIndex, currentIndex + END_KEYWORD_LENGTH)
                        .equals("END")) {
                    newToken = new Token(TokenType.KEYWORD, "END");
                    currentIndex += END_KEYWORD_LENGTH;
                }
                //ECHO (=) keyword
                else if (currentIndex + ECHO_KEYWORD_LENGTH < data.length
                        && data[currentIndex] == '=') {
                    newToken = new Token(TokenType.KEYWORD, "=");
                    currentIndex += ECHO_KEYWORD_LENGTH;
                } else {
                    throw new LexerException("Invalid keyword exception.");
                }
            } else { // elements that aren't keyword

                //possibilities:
                //1. var (with valid name)
                //2. double ("-" too)
                //3. int ("-" too)
                //4. string (escape)
                //5. @function (with valid name)
                //6. operator (+,- (non-unary),*,/,^)
                //7. closing tag

                if (data[currentIndex] == '+'
                        || (data[currentIndex] == '-' && currentIndex + 1 < data.length
                        && !(Character.isDigit(data[currentIndex + 1])))
                        || data[currentIndex] == '*'
                        || data[currentIndex] == '/'
                        || data[currentIndex] == '^') {
                    newToken = new Token(TokenType.OPERATOR, data[currentIndex]);
                    currentIndex++;
                } else {

                    StringBuilder sb = new StringBuilder();

                    if (Character.isLetter(data[currentIndex])) {
                        do {

                            sb.append(data[currentIndex]);
                            currentIndex++;
                        } while (currentIndex < data.length && (Character.isLetterOrDigit(data[currentIndex])
                                || data[currentIndex] == '_'));

                        newToken = new Token(TokenType.VARIABLE, sb.toString());
                    } else if (Character.isDigit(data[currentIndex]) || data[currentIndex] == '-') {  // already ruled out the binary '-'

                        boolean isDouble = false;

                        do {
                            if (data[currentIndex] == '.') {
                                if (isDouble) {
                                    throw new NumberFormatException("Invalid double format, multiple dots.");
                                }
                                isDouble = true;
                            } else if (data[currentIndex] == '-' && currentIndex + 1 >= data.length) {
                                throw new LexerException("Invalid tag format.");
                            }
                            sb.append(data[currentIndex]);
                            currentIndex++;
                        } while (currentIndex < data.length && (Character.isDigit(data[currentIndex])
                                || data[currentIndex] == '.'));

                        if (isDouble) {
                            newToken = new Token(TokenType.DOUBLE, Double.parseDouble(sb.toString()));
                        } else {
                            newToken = new Token(TokenType.INTEGER, Integer.parseInt(sb.toString()));
                        }
                    } else if (data[currentIndex] == '\"') { //string

                        sb.append(data[currentIndex]);
                        currentIndex++;
                        while (currentIndex < data.length && data[currentIndex] != '\"') {

                            if (data[currentIndex] == '\\') {
                                if (currentIndex + 1 < data.length) {
                                    currentIndex++;
                                    switch (data[currentIndex]) {
                                        case '\\', '\"' -> sb.append(data[currentIndex]);
                                        case 'n' -> sb.append('\n');
                                        case 'r' -> sb.append('\r');
                                        case 't' -> sb.append('\t');
                                        default -> throw new LexerException("Unsupported character escaping.");
                                    }
                                    currentIndex++;
                                    continue;
                                } else {
                                    throw new LexerException("Invalid escaping.");
                                }
                            }
                            sb.append(data[currentIndex]);
                            currentIndex++;
                        }
                        sb.append(data[currentIndex]);
                        currentIndex++;

                        if (currentIndex >= data.length) {
                            throw new LexerException("String not closed.");
                        }

                        newToken = new Token(TokenType.STRING, sb.toString());

                    } else if (data[currentIndex] == '@') {
                        do {
                            sb.append(data[currentIndex]);
                            currentIndex++;
                        } while (currentIndex < data.length && (Character.isLetterOrDigit(data[currentIndex])
                                || data[currentIndex] == '_'));

                        newToken = new Token(TokenType.FUNCTION, sb.toString());
                    } else if (data[currentIndex] == '$') {

                        if (currentIndex + 1 < data.length) {
                            currentIndex++;
                            if (data[currentIndex] == '}') {
                                newToken = new Token(TokenType.TAG_CLOSE, null);
                                currentIndex++;
                                lexerState = LexerState.TEXT;
                            } else {
                                throw new LexerException("Invalid tag format.");
                            }
                        } else {
                            throw new LexerException("Invalid tag format.");
                        }

                    } else {
                        throw new LexerException("Invalid characters in tag.");
                    }

                }

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
