package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;
import calculator.interpreter.datatypes.CValue;
import calculator.util.Util;

public class UnsupportedFunctionOperationException extends CalculatorException {
    public UnsupportedFunctionOperationException(CValue[] arguments, String id) {
        super(
            String.format(
                "Operation %s(%s) is not defined",
                id,
                Util.join(", ", arguments, (CValue v) -> v.getType().name())
            )
        );
    }
}
