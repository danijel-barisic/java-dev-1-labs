package hr.fer.oprpp1.hw02.prob1;

/**
 * There are two states:
 * BASIC - everything is being tokenized according to basic lexer rules (there's words, numbers, symbols...)
 * EXTENDED -  everything is being tokenized as a word (separated by whitespace)
 *
 * @Author Danijel Barišić
 */
public enum LexerState {
    BASIC, EXTENDED
}
