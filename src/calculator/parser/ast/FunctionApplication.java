package calculator.parser.ast;

import calculator.interpreter.Visitor;
import calculator.util.Util;

public class FunctionApplication extends AbstractSyntaxTree {
    public final AbstractSyntaxTree applicand;
    public final AbstractSyntaxTree[] arguments;

    public FunctionApplication(AbstractSyntaxTree applicand, AbstractSyntaxTree[] arguments) {
        this.applicand = applicand;
        this.arguments = arguments;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.FUNCTION_APPLICATION;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s: %s(%s)]",
            this.getVisitor().name(),
            this.applicand.getVisitor().name(),
            Util.join(",", this.arguments, (AbstractSyntaxTree a) -> a.getVisitor().name())
        );
    }
}
