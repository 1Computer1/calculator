package calculator.interpreter;

import calculator.interpreter.datatypes.CFunction;
import calculator.interpreter.datatypes.CMatrix;
import calculator.interpreter.datatypes.CScalar;
import calculator.interpreter.datatypes.CValue;
import calculator.parser.ast.FunctionApplication;

import java.util.Map;

public class FunctionContext {
    public final Map<String, CValue> arguments;
    public final FunctionApplication ast;

    public FunctionContext(Map<String, CValue> arguments, FunctionApplication ast) {
        this.arguments = arguments;
        this.ast = ast;
    }

    public boolean isScalar(String arg) {
        return this.arguments.get(arg).getType() == DataType.SCALAR;
    }

    public CScalar scalar(String arg) {
        return (CScalar) this.arguments.get(arg);
    }

    public boolean isMatrix(String arg) {
        return this.arguments.get(arg).getType() == DataType.MATRIX;
    }

    public CMatrix matrix(String arg) {
        return (CMatrix) this.arguments.get(arg);
    }

    public boolean isFunction(String arg) {
        return this.arguments.get(arg).getType() == DataType.FUNCTION;
    }

    public CFunction function(String arg) {
        return (CFunction) this.arguments.get(arg);
    }

    public CValue[] getArgsAsArray() {
        return this.arguments.values().toArray(new CValue[this.arguments.size()]);
    }
}
