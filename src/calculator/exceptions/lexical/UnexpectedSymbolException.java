package calculator.exceptions.lexical;

import calculator.exceptions.CalculatorException;
import calculator.lexer.Symbol;
import calculator.parser.ast.Token;

public class UnexpectedSymbolException extends CalculatorException {
    public UnexpectedSymbolException(Symbol expected, Token got) {
        super(String.format("Expected %s, got %s at position %d", expected.name(), got.symbol.name(), got.position));
    }
}
