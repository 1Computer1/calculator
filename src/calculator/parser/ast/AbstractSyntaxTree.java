package calculator.parser.ast;

import calculator.interpreter.Visitor;

public class AbstractSyntaxTree {
    public Visitor getVisitor() {
        return null;
    }

    @Override
    public String toString() {
        return "[ABSTRACT_SYNTAX_TREE]";
    }
}
