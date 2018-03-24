package calculator.exceptions.lexical;

import calculator.exceptions.CalculatorException;

public class UnknownSymbolException extends CalculatorException {
    public UnknownSymbolException(String source, int position) {
        super(String.format("Unknown symbol %s at position %d", source.charAt(position), position));
    }
}
