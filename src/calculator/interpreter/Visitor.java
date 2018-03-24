package calculator.interpreter;

import calculator.exceptions.CalculatorException;
import calculator.exceptions.evaluating.InvalidRangeError;
import calculator.exceptions.evaluating.UnsupportedArgumentException;
import calculator.exceptions.evaluating.VariableConflictException;
import calculator.exceptions.evaluating.VariableUndefinedException;
import calculator.interpreter.datatypes.CFunction;
import calculator.interpreter.datatypes.CMatrix;
import calculator.interpreter.datatypes.CScalar;
import calculator.interpreter.datatypes.CValue;
import calculator.lexer.Symbol;
import calculator.parser.ast.*;
import calculator.util.CheckedBiFunction;

import java.util.HashMap;
import java.util.Map;

public enum Visitor {
    VARIABLE_ASSIGNMENT((Interpreter in, VariableAssignment a) -> {
        String id = a.identifier.value;

        if (in.variables.containsKey(id)) {
            throw new VariableConflictException(id);
        }

        CValue value = in.process(a.body);
        in.variables.put(id, value);
        return value;
    }),

    FUNCTION_ASSIGNMENT((Interpreter in, FunctionAssignment a) -> {
        String id = a.identifier.value;

        if (in.variables.containsKey(id)) {
            throw new VariableConflictException(id);
        }

        CFunction fn = new CFunction(a);
        in.variables.put(id, fn);
        return fn;
    }),

    FUNCTION_APPLICATION((Interpreter in, FunctionApplication a) -> {
        CValue applicand = in.process(a.applicand);

        if (applicand instanceof CFunction) {
            CFunction fn = (CFunction) applicand;
            if (fn.params.length != a.arguments.length) {
                throw new InvalidRangeError("Invalid amount of arguments passed to function");
            }

            if (fn.internal) {
                Map<String, CValue> passed = new HashMap<>();
                for (int i = 0; i < fn.params.length; i++) {
                    passed.put(fn.params[i], in.process(a.arguments[i]));
                }

                assert fn.process != null;
                return fn.process.apply(new FunctionContext(passed, a));
            }

            Interpreter scope = in.createScope();
            for (int i = 0; i < fn.params.length; i++) {
                scope.variables.put(fn.params[i], in.process(a.arguments[i]));
            }

            return scope.process(fn.body);
        }

        if (a.arguments.length != 1) {
            throw new InvalidRangeError("Invalid amount of values to multiply");
        }

        return Operator.MULTIPLY.apply(applicand, in.process(a.arguments[0]));
    }),

    BINARY_OPERATION((Interpreter in, BinaryOperation a) -> {
        CValue lh = in.process(a.left);
        CValue rh = in.process(a.right);
        return Operator.fromSymbol(a.operator.symbol).apply(lh, rh);
    }),

    UNARY_OPERATION((Interpreter in, UnaryOperation a) -> {
        CValue op = in.process(a.operand);
        return Operator.fromSymbol(a.operator.symbol).apply(op);
    }),

    TOKEN((Interpreter in, Token a) -> {
        if (a.symbol == Symbol.IDENTIFIER) {
            CValue res = in.variables.get(a.value);
            if (res == null) throw new VariableUndefinedException(a.value);
            return res;
        }

        return new CScalar(Double.valueOf(a.value));
    }),

    MATRIX((Interpreter in, Matrix a) -> {
        CValue[][] values = new CValue[a.values.length][];
        for (int i = 0; i < values.length; i++) {
            CValue[] row = new CValue[a.values[i].length];
            for (int j = 0; j < row.length; j++) {
                CValue value = in.process(a.values[i][j]);
                row[j] = value;
            }

            values[i] = row;
        }

        DataType type = values[0][0].getType();
        for (CValue[] row : values) {
            for (CValue c : row) {
                if (c.getType() != type) {
                    throw new UnsupportedArgumentException(String.format("Matrix elements must match type %s", type.name()));
                }
            }
        }

        return new CMatrix(values);
    });

    private final CheckedBiFunction<Interpreter, AbstractSyntaxTree, CValue> visit;

    @SuppressWarnings("unchecked")
    <T extends AbstractSyntaxTree> Visitor(CheckedBiFunction<Interpreter, T, CValue> visit) {
        this.visit = (Interpreter i, AbstractSyntaxTree a) -> visit.apply(i, (T) a);
    }

    public CValue apply(Interpreter interpreter, AbstractSyntaxTree ast) throws CalculatorException {
        return this.visit.apply(interpreter, ast);
    }
}
