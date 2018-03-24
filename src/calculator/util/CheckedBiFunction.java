package calculator.util;

import calculator.exceptions.CalculatorException;

@FunctionalInterface
public interface CheckedBiFunction<T, U, R> {
    R apply(T t, U u) throws CalculatorException;
}
