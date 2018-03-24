package calculator.parser.ast;

import calculator.interpreter.Visitor;

public class VariableAssignment extends AbstractSyntaxTree {
    public final Token identifier;
    public final AbstractSyntaxTree body;

    public VariableAssignment(Token identifier, AbstractSyntaxTree body) {
        this.identifier = identifier;
        this.body = body;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.VARIABLE_ASSIGNMENT;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s: %s = %s]",
            this.getVisitor().name(),
            this.identifier.value,
            this.body.getVisitor().name()
        );
    }
}
