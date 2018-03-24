package calculator.interpreter.datatypes;

import calculator.interpreter.DataType;

public class CScalar extends CValue {
    public final double value;

    public CScalar(double value) {
        this.value = value;
    }

    @Override
    public DataType getType() {
        return DataType.SCALAR;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
