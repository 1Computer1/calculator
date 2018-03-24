package calculator.parser.ast;

import calculator.interpreter.Visitor;
import calculator.util.Util;

public class Matrix extends AbstractSyntaxTree {
    public final AbstractSyntaxTree[][] values;

    public Matrix(AbstractSyntaxTree[][] values) {
        this.values = values;
    }

    @Override
    public Visitor getVisitor() {
        return Visitor.MATRIX;
    }

    @Override
    public String toString() {
        return String.format(
            "[%s: %d[%s]]",
            this.getVisitor().name(),
            this.values.length,
            Util.join(", ", this.values, (AbstractSyntaxTree[] l) -> String.valueOf(l.length))
        );
    }
}
