package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;
import calculator.interpreter.datatypes.CValue;

public class UnsupportedUnaryOperationException extends CalculatorException {
    public UnsupportedUnaryOperationException(CValue operand, String operator, boolean postfix) {
        super(
            postfix
                ? String.format(
                "Operation %s%s is not defined",
                operator,
                operand.getType().name()
            )
                : String.format(
                "Operation %s%s is not defined",
                operand.getType().name(),
                operator
            )
        );
    }
}
