package calculator.exceptions.semantical;

import calculator.exceptions.CalculatorException;
import calculator.parser.ast.Token;

public class ExpectedExpressionException extends CalculatorException {
    public ExpectedExpressionException(String expected, Token got) {
        super(String.format("Expected %s at position %s", expected, got.position));
    }
}
