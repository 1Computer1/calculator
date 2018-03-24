package calculator.lexer;

import calculator.exceptions.CalculatorException;
import calculator.exceptions.lexical.UnknownSymbolException;
import calculator.parser.ast.Token;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;

    private boolean done = false;
    private int position = 0;

    private Lexer(String source) {
        this.source = source;
    }

    private Token next() {
        if (this.position == this.source.length()) {
            this.done = true;
            return new Token(Symbol.EOF, "", this.position);
        }

        for (Symbol symbol : Symbol.values()) {
            int end = symbol.matchEnd(this.source, this.position);
            if (end == -1) continue;

            String matched = this.source.substring(this.position, end);
            this.position = end;

            return new Token(symbol, matched, this.position);
        }

        return null;
    }

    private Token nextToken() throws CalculatorException {
        Token token = this.next();
        if (token == null) {
            throw new UnknownSymbolException(this.source, this.position);
        }

        if (token.symbol.isIgnorable()) {
            return this.nextToken();
        }

        return token;
    }

    private List<Token> run() throws CalculatorException {
        List<Token> tokens = new ArrayList<>();
        while (!this.done) tokens.add(this.nextToken());
        return tokens;
    }

    public static List<Token> tokenize(String source) throws CalculatorException {
        return new Lexer(source).run();
    }
}
