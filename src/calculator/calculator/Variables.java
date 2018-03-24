package calculator.calculator;

import calculator.exceptions.evaluating.UnsupportedArgumentException;
import calculator.exceptions.evaluating.UnsupportedFunctionOperationException;
import calculator.interpreter.Operator;
import calculator.interpreter.datatypes.CFunction;
import calculator.interpreter.datatypes.CMatrix;
import calculator.interpreter.datatypes.CScalar;
import calculator.interpreter.datatypes.CValue;
import calculator.util.CheckedFunction;
import calculator.interpreter.FunctionContext;
import calculator.util.Util;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    private static final Map<String, CValue> variables = new HashMap<>();

    static {
        Variables.define("pi", new CScalar(Math.PI));
        Variables.define("e", new CScalar(Math.E));

        Variables.define("abs", new String[]{ "x" }, (FunctionContext c) -> {
            if (c.isScalar("x")) {
                CScalar x = c.scalar("x");
                return new CScalar(Math.abs(x.value));
            }

            throw new UnsupportedFunctionOperationException(c.getArgsAsArray(), "abs");
        });

        Variables.define("sqrt", new String[]{ "x" }, (FunctionContext c) -> {
            if (c.isScalar("x")) {
                CScalar x = c.scalar("x");
                return new CScalar(Math.sqrt(x.value));
            }

            throw new UnsupportedFunctionOperationException(c.getArgsAsArray(), "sqrt");
        });

        Variables.define("root", new String[]{ "n", "x" }, (FunctionContext c) -> {
            if (c.isScalar("n") && c.isScalar("x")) {
                CScalar n = c.scalar("n");
                CScalar x = c.scalar("x");
                if (!Util.isIntegral(n.value)) {
                    throw new UnsupportedArgumentException("Degree of root must be an integer");
                }

                return new CScalar(Math.pow(x.value, 1 / n.value));
            }

            throw new UnsupportedFunctionOperationException(c.getArgsAsArray(), "root");
        });

        Variables.define("det", new String[] { "m" }, (FunctionContext c) -> {
            if (c.isMatrix("m")) {
                CMatrix m = c.matrix("m");
                if (m.getRows() != 2 || m.getColumns() != 2) {
                    // TODO: Make this work for all square matrices
                    throw new UnsupportedArgumentException("Determinant currently requires 2-by-2 matrix");
                }

                return Operator.MINUS.apply(
                    Operator.MULTIPLY.apply(m.values[0][0], m.values[1][1]),
                    Operator.MULTIPLY.apply(m.values[0][1], m.values[1][0])
                );
            }

            throw new UnsupportedFunctionOperationException(c.getArgsAsArray(), "det");
        });
    }

    public static Map<String, CValue> getVariables() {
        return Variables.variables;
    }

    private static void define(String name, CValue value) {
        Variables.variables.put(name, value);
    }

    private static void define(String name, String[] args, CheckedFunction<FunctionContext, CValue> process) {
        Variables.variables.put(name, new CFunction(name, args, process));
    }
}
