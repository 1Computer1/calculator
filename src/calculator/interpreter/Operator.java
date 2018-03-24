package calculator.interpreter;

import calculator.exceptions.CalculatorException;
import calculator.exceptions.evaluating.UnsupportedArgumentException;
import calculator.exceptions.evaluating.UnsupportedBinaryOperationException;
import calculator.exceptions.evaluating.UnsupportedUnaryOperationException;
import calculator.interpreter.datatypes.CMatrix;
import calculator.interpreter.datatypes.CScalar;
import calculator.interpreter.datatypes.CValue;
import calculator.lexer.Symbol;
import calculator.util.CheckedBiFunction;
import calculator.util.CheckedFunction;
import calculator.util.Pair;
import calculator.util.Util;

import java.util.HashMap;
import java.util.Map;

public enum Operator {
    PLUS("+") {{
        this.define(
            DataType.SCALAR,
            (CScalar op) -> op
        );

        this.define(
            DataType.SCALAR, DataType.SCALAR,
            (CScalar lh, CScalar rh) -> new CScalar(lh.value + rh.value)
        );

        this.define(
            DataType.MATRIX, DataType.MATRIX,
            (CMatrix lh, CMatrix rh) -> {
                if (lh.getRows() != rh.getRows() || lh.getColumns() != rh.getColumns()) {
                    throw new UnsupportedArgumentException("Matrices must be of the same dimensions");
                }

                CValue[][] res = new CValue[rh.getRows()][rh.getColumns()];
                for (int i = 0; i < rh.getRows(); i++) {
                    for (int j = 0; j < rh.getColumns(); j++) {
                        res[i][j] = Operator.valueOf("PLUS").apply(lh.values[i][j], rh.values[i][j]);
                    }
                }

                return new CMatrix(res);
            }
        );
    }},

    MINUS("-") {{
        this.define(
            DataType.SCALAR,
            (CScalar op) -> new CScalar(-op.value)
        );

        this.define(
            DataType.SCALAR, DataType.SCALAR,
            (CScalar lh, CScalar rh) -> new CScalar(lh.value - rh.value)
        );

        this.define(
            DataType.MATRIX, DataType.MATRIX,
            (CMatrix lh, CMatrix rh) -> {
                if (lh.getRows() != rh.getRows() || lh.getColumns() != rh.getColumns()) {
                    throw new UnsupportedArgumentException("Matrices must be of the same dimensions");
                }

                CValue[][] res = new CValue[rh.getRows()][rh.getColumns()];
                for (int i = 0; i < rh.getRows(); i++) {
                    for (int j = 0; j < rh.getColumns(); j++) {
                        res[i][j] = Operator.valueOf("MINUS").apply(lh.values[i][j], rh.values[i][j]);
                    }
                }

                return new CMatrix(res);
            }
        );
    }},

    MULTIPLY("*") {{
        this.define(
            DataType.SCALAR, DataType.SCALAR,
            (CScalar lh, CScalar rh) -> new CScalar(lh.value * rh.value)
        );

        this.define(
            DataType.SCALAR, DataType.MATRIX,
            (CScalar lh, CMatrix rh) -> {
                CValue[][] res = new CValue[rh.getRows()][rh.getColumns()];
                for (int i = 0; i < rh.getRows(); i++) {
                    for (int j = 0; j < rh.getColumns(); j++) {
                        res[i][j] = Operator.valueOf("MULTIPLY").apply(lh, rh.values[i][j]);
                    }
                }

                return new CMatrix(res);
            }
        );

        this.define(
            DataType.MATRIX, DataType.SCALAR,
            (CMatrix lh, CScalar rh) -> this.binaryOperations.get(Pair.of(DataType.SCALAR, DataType.MATRIX)).apply(rh, lh)
        );

        this.define(
            DataType.MATRIX, DataType.MATRIX,
            (CMatrix lh, CMatrix rh) -> {
                if (lh.getColumns() != rh.getRows()) {
                    throw new UnsupportedArgumentException("Matrices do not have compatible dimensions");
                }

                CValue[][] res = new CValue[lh.getRows()][rh.getColumns()];
                for (int il = 0; il < lh.getRows(); il++) {
                    for (int jr = 0; jr < rh.getColumns(); jr++) {
                        CValue[] products = new CValue[lh.getColumns()];
                        for (int i = 0; i < lh.getColumns(); i++) {
                            products[i] = Operator.valueOf("MULTIPLY").apply(lh.values[il][i], rh.values[i][jr]);
                        }

                        CValue sum = products[0];
                        for (int i = 1; i < products.length; i++) {
                            sum = Operator.valueOf("PLUS").apply(sum, products[i]);
                        }

                        res[il][jr] = sum;
                    }
                }

                return new CMatrix(res);
            }
        );
    }},

    DIVIDE("/") {{
        this.define(
            DataType.SCALAR, DataType.SCALAR,
            (CScalar lh, CScalar rh) -> new CScalar(lh.value / rh.value)
        );
    }},

    POWER("^") {{
        this.define(
            DataType.SCALAR, DataType.SCALAR,
            (CScalar lh, CScalar rh) -> new CScalar(Math.pow(lh.value, rh.value))
        );
    }},

    FACTORIAL("!") {{
        this.define(
            DataType.SCALAR,
            (CScalar op) -> {
                if (!Util.isIntegral(op.value) || op.value < 0) {
                    throw new UnsupportedArgumentException("Argument to factorial must be a positive integer");
                }

                if (Util.isWithinEpsilon(op.value, 0)) return new CScalar(1);

                double res = 1;
                for (int i = 2; i <= Math.round(op.value); i++) {
                    res *= i;
                }

                return new CScalar(res);
            }
        );
    }};

    private final String symbol;
    protected final Map<Pair<DataType>, CheckedBiFunction<CValue, CValue, CValue>> binaryOperations = new HashMap<>();
    protected final Map<DataType, CheckedFunction<CValue, CValue>> unaryOperations = new HashMap<>();

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    @SuppressWarnings("unchecked")
    protected <A extends CValue, B extends CValue> void define(DataType lh, DataType rh, CheckedBiFunction<A, B, CValue> fn) {
        this.binaryOperations.put(Pair.of(lh, rh), (CValue a, CValue b) -> fn.apply((A) a, (B) b));
    }

    @SuppressWarnings("unchecked")
    protected <A extends CValue> void define(DataType op, CheckedFunction<A, CValue> fn) {
        this.unaryOperations.put(op, (CValue a) -> fn.apply((A) a));
    }

    public CValue apply(CValue lh, CValue rh) throws CalculatorException {
        CheckedBiFunction<CValue, CValue, CValue> fn = this.binaryOperations.getOrDefault(Pair.of(lh.getType(), rh.getType()), null);
        if (fn == null) {
            throw new UnsupportedBinaryOperationException(lh, rh, this.getSymbol());
        }

        return fn.apply(lh, rh);
    }

    public CValue apply(CValue op) throws CalculatorException {
        CheckedFunction<CValue, CValue> fn = this.unaryOperations.getOrDefault(op.getType(), null);
        if (fn == null) {
            throw new UnsupportedUnaryOperationException(op, this.getSymbol(), this == FACTORIAL);
        }

        return fn.apply(op);
    }

    public static Operator fromSymbol(Symbol symbol) {
        switch (symbol) {
            case PLUS:
                return Operator.PLUS;
            case MINUS:
                return Operator.MINUS;
            case ASTERISK:
                return Operator.MULTIPLY;
            case SLASH:
                return Operator.DIVIDE;
            case CARET:
                return Operator.POWER;
            case EXCLAMATION:
                return Operator.FACTORIAL;
            default:
                throw new EnumConstantNotPresentException(Operator.class, symbol.name());
        }
    }
}
