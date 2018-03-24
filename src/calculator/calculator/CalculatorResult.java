package calculator.calculator;

import calculator.interpreter.datatypes.CValue;

public class CalculatorResult {
    public final String message;
    public final CalculatorResultType result;

    private CalculatorResult(String message, CalculatorResultType result) {
        this.message = message;
        this.result = result;
    }

    public static CalculatorResult info(String string) {
        return new CalculatorResult(string, CalculatorResultType.INFO);
    }

    public static CalculatorResult info(CValue value) {
        return CalculatorResult.info(value.toString());
    }

    public static CalculatorResult error(String string) {
        return new CalculatorResult(string, CalculatorResultType.ERROR);
    }

    public static CalculatorResult error(Exception ex) {
        return CalculatorResult.error(ex.getMessage());
    }

    public static CalculatorResult exit(String string) {
        return new CalculatorResult(string, CalculatorResultType.EXIT);
    }
}
