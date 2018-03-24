package calculator.parser.ast;

import calculator.interpreter.Visitor;
import calculator.util.Util;

public class FunctionAssignment extends AbstractSyntaxTree {
    public final Token identifier;
    public final Token[] parameters;
    public final AbstractSyntaxTree body;

    public FunctionAssignment(Token identifier, Token[] parameters, AbstractSyntaxTree body) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.FUNCTION_ASSIGNMENT;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s: %s(%s) = %s]",
            this.getVisitor().name(),
            this.identifier.value,
            Util.join(", ", this.parameters, (Token t) -> t.value),
            this.body.getVisitor().name()
        );
    }
}
