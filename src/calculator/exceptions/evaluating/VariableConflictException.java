package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;

public class VariableConflictException extends CalculatorException {
    public VariableConflictException(String id) {
        super(String.format("The variable %s has already been defined", id));
    }
}
