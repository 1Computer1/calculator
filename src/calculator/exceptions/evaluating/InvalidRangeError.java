package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;

public class InvalidRangeError extends CalculatorException {
    public InvalidRangeError(String message) {
        super(message);
    }
}
