package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;
import calculator.interpreter.datatypes.CValue;

public class UnsupportedBinaryOperationException extends CalculatorException {
    public UnsupportedBinaryOperationException(CValue left, CValue right, String operator) {
        super(
            String.format(
                "Operation %s %s %s is not defined",
                left.getType().name(),
                operator,
                right.getType().name()
            )
        );
    }
}
