package calculator.interpreter.datatypes;

import calculator.interpreter.DataType;
import calculator.parser.ast.AbstractSyntaxTree;
import calculator.parser.ast.FunctionAssignment;
import calculator.util.CheckedFunction;
import calculator.interpreter.FunctionContext;

public class CFunction extends CValue {
    public final String id;
    public final String[] params;
    public final AbstractSyntaxTree body;

    public final boolean internal;
    public final CheckedFunction<FunctionContext, CValue> process;

    public CFunction(FunctionAssignment sourceAST) {
        String[] params = new String[sourceAST.parameters.length];
        for (int j = 0; j < sourceAST.parameters.length; j++) {
            params[j] = sourceAST.parameters[j].value;
        }

        this.id = sourceAST.identifier.value;
        this.params = params;
        this.body = sourceAST.body;

        this.internal = false;
        this.process = null;
    }

    public CFunction(
        String id,
        String[] params,
        CheckedFunction<FunctionContext, CValue> process
    ) {
        this.id = id;
        this.params = params;
        this.body = null;

        this.internal = true;
        this.process = process;
    }

    @Override
    public DataType getType() {
        return DataType.FUNCTION;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.id, String.join(", ", this.params));
    }
}
