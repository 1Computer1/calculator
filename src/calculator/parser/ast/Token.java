package calculator.parser.ast;

import calculator.interpreter.Visitor;
import calculator.lexer.Symbol;

public class Token extends AbstractSyntaxTree {
    public final Symbol symbol;
    public final String value;
    public final int position;

    public Token(Symbol symbol, String value, int position) {
        this.symbol = symbol;
        this.value = value;
        this.position = position;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.TOKEN;
    }

    @Override
    public String toString() {
        return String.format("[%s: %s]", this.getVisitor().name(), this.value);
    }
}
