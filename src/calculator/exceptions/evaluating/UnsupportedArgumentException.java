package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;

public class UnsupportedArgumentException extends CalculatorException {
    public UnsupportedArgumentException(String message) {
        super(message);
    }
}
