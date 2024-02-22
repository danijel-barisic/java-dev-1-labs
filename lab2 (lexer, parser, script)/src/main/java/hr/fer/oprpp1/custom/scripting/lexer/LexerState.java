package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * States of SmartScriptLexer:
 * TEXT - signifies that regular text is currently being analyzed.
 * TAG - signifies that a tag is being analyzed.
 *
 * @Author Danijel Barišić
 */
public enum LexerState {
    TEXT, TAG
}
