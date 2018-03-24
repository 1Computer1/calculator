package calculator.interpreter.datatypes;

import calculator.interpreter.DataType;

public class CMatrix extends CValue {
    public final CValue[][] values;

    public CMatrix(CValue[][]  values) {
        this.values = values;
    }

    public int getRows() {
        return this.values.length;
    }

    public int getColumns() {
        return this.values[0].length;
    }

    public DataType getElementType() {
        return this.values[0][0].getType();
    }

    @Override
    public DataType getType() {
        return DataType.MATRIX;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append("[")
            .append(this.values[0][0]);

        for (int i = 1; i < this.values[0].length; i++) {
            builder.append(", ").append(this.values[0][i]);
        }

        for (int i = 1; i < this.values.length; i++) {
            CValue[] row = this.values[i];
            builder.append("; ").append(row[0]);
            for (int j = 1; j < row.length; j++) {
                builder.append(", ").append(row[j].toString());
            }
        }

        return builder
            .append("]")
            .toString();
    }
}
