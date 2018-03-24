package calculator.parser.ast;

import calculator.interpreter.Operator;
import calculator.interpreter.Visitor;

public class UnaryOperation extends AbstractSyntaxTree {
    public final AbstractSyntaxTree operand;
    public final Token operator;
    public final boolean postfix;

    public UnaryOperation(AbstractSyntaxTree operand, Token operator, boolean postfix) {
        this.operand = operand;
        this.operator = operator;
        this.postfix = postfix;
    }

    public UnaryOperation(AbstractSyntaxTree operand, Token operator) {
        this(operand, operator, false);
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.UNARY_OPERATION;
    }

    @Override
    public String toString() {
        if (this.postfix) {
            return String.format(
                "[%s: %s%s]",
                this.getVisitor().name(),
                this.operand.getVisitor().name(),
                Operator.fromSymbol(this.operator.symbol).getSymbol()
            );
        }

        return String.format(
            "[%s: %s%s]",
            this.getVisitor().name(),
            Operator.fromSymbol(this.operator.symbol).getSymbol(),
            this.operand.getVisitor().name()
        );
    }
}
