package calculator.util;

import calculator.exceptions.CalculatorException;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws CalculatorException;
}
