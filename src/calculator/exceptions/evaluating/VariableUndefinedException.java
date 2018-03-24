package calculator.exceptions.evaluating;

import calculator.exceptions.CalculatorException;

public class VariableUndefinedException extends CalculatorException {
    public VariableUndefinedException(String id) {
        super(String.format("The variable %s has not been defined", id));
    }
}