package calculator.parser.ast;

import calculator.interpreter.Operator;
import calculator.interpreter.Visitor;

public class BinaryOperation extends AbstractSyntaxTree {
    public final AbstractSyntaxTree left;
    public final AbstractSyntaxTree right;
    public final Token operator;

    public BinaryOperation(AbstractSyntaxTree left, AbstractSyntaxTree right, Token operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.BINARY_OPERATION;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s: %s %s %s]",
            this.getVisitor().name(),
            this.left.getVisitor().name(),
            Operator.fromSymbol(this.operator.symbol).getSymbol(),
            this.right.getVisitor().name()
        );
    }
}
