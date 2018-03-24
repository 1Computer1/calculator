package calculator.util;

import calculator.interpreter.datatypes.CFunction;
import calculator.interpreter.datatypes.CScalar;
import calculator.interpreter.datatypes.CValue;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Util {
    /**
     * Epsilon value, 2^53, which is "close enough to 0".
     */
    public static final double EPSILON = Math.pow(2, -52);

    /**
     * Reduces a list from the left.
     * @param list List to reduce.
     * @param firstItem Initial item.
     * @param step Step to skip by.
     * @param fn Reduce function.
     * @param <R> Type of the accumulator.
     * @param <E> Type of list elements.
     * @return The accumulator.
     */
    public static <R, E> R reduce(List<E> list, R firstItem, int step, BiFunction<R, Integer, R> fn) {
        R accumulator = firstItem;
        for (int i = 0; i < list.size() - step; i += step) {
            accumulator = fn.apply(accumulator, i);
        }

        return accumulator;
    }

    /**
     * Reduces a list from the right.
     * @param list List to reduce.
     * @param firstItem Initial item.
     * @param step Step to skip by.
     * @param fn Reduce function.
     * @param <R> Type of the accumulator.
     * @param <E> Type of list elements.
     * @return The accumulator.
     */
    public static <R, E> R reduceRight(List<E> list, R firstItem, int step, BiFunction<R, Integer, R> fn) {
        R accumulator = firstItem;
        for (int i = list.size() - 1; i >= step; i -= step) {
            accumulator = fn.apply(accumulator, i);
        }

        return accumulator;
    }

    /**
     * Joins an array.
     * @param delimiter Delimiter string between each element.
     * @param array The array to join.
     * @param fn The mapping function to map elements.
     * @param <E> Type of elements.
     * @return Joined array.
     */
    public static <E> String join(String delimiter, E[] array, Function<E, String> fn) {
        String[] strings = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            strings[i] = fn.apply(array[i]);
        }

        return String.join(delimiter, strings);
    }

    /**
     * Joins a list.
     * @param delimiter Delimiter string between each element.
     * @param list The list to join.
     * @param fn The mapping function to map elements.
     * @param <E> Type of elements.
     * @return Joined list.
     */
    public static <E> String join(String delimiter, List<E> list, Function<E, String> fn) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = fn.apply(list.get(i));
        }

        return String.join(delimiter, strings);
    }

    /**
     * Checks if two numbers are close enough to each other.
     * @param a First number.
     * @param b Second number.
     * @return True if numbers are close enough to each other.
     */
    public static boolean isWithinEpsilon(double a, double b) {
        return Math.abs(a - b) < Util.EPSILON;
    }

    /**
     * Checks if a number is close enough to an integer.
     * @param a Number to check.
     * @return True if number is close to an integer.
     */
    public static boolean isIntegral(double a) {
        return a % 1.0 < Util.EPSILON;
    }

    public static String getTypeName(CValue value) {
        if (value instanceof CScalar) return "SCALAR";
        if (value instanceof CFunction) return "FUNCTION";
        return "UNKNOWN";
    }
}
