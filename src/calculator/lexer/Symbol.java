package calculator.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Symbol {
    WHITESPACE(Pattern.compile("^\\s+"), true),
    LET(Pattern.compile("^let\\b")),
    NUMBER(Pattern.compile("^(?:(?:0|[1-9][0-9]*)(?:\\.[0-9]*)?|(?:0|[1-9][0-9]*)?\\.[0-9]+)(?:[Ee][+-]?[1-9][0-9]*)?")),
    IDENTIFIER(Pattern.compile("^[A-Za-z_$][A-Za-z_$0-9]*")),
    LPAREN(Pattern.compile("^\\(")),
    RPAREN(Pattern.compile("^\\)")),
    LBRACKET(Pattern.compile("^\\[")),
    RBRACKET(Pattern.compile("^]")),
    PLUS(Pattern.compile("^\\+")),
    MINUS(Pattern.compile("^-")),
    ASTERISK(Pattern.compile("^\\*")),
    SLASH(Pattern.compile("^/")),
    CARET(Pattern.compile("^\\^")),
    EQUAL(Pattern.compile("^=")),
    COMMA(Pattern.compile("^,")),
    SEMICOLON(Pattern.compile("^;")),
    EXCLAMATION(Pattern.compile("^!")),
    EOF(Pattern.compile("^$"));

    private final Pattern pattern;
    private final boolean ignorable;

    Symbol(Pattern pattern, boolean ignorable) {
        this.pattern = pattern;
        this.ignorable = ignorable;
    }

    Symbol(Pattern pattern) {
        this(pattern, false);
    }

    public boolean isIgnorable() {
        return this.ignorable;
    }

    /**
     * Finds the index of which the next match should start from.
     * @param text Text to match.
     * @param start Starting index.
     * @return The index of the character after the match.
     */
    public int matchEnd(String text, int start) {
        Matcher matcher = this.pattern.matcher(text).region(start, text.length());
        if (!matcher.find()) return -1;
        return matcher.end();
    }
}
