package calculator.interpreter;

import calculator.exceptions.CalculatorException;
import calculator.interpreter.datatypes.CValue;
import calculator.parser.ast.AbstractSyntaxTree;

import java.util.Map;

public class Interpreter {
    public final Map<String, CValue> variables;

    public Interpreter(Map<String, CValue> variables) {
        this.variables = variables;
    }

    public Interpreter createScope() {
        return new Interpreter(this.variables);
    }

    public CValue process(AbstractSyntaxTree ast) throws CalculatorException {
        return ast.getVisitor().apply(this, ast);
    }
}
