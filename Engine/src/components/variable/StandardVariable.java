package components.variable;

import components.executor.Context;
import dtos.VariableDetails;

import java.util.List;
import java.util.Objects;

public class StandardVariable implements Variable {
    public enum VariableType {INPUT, WORK, OUTPUT, EMPTY}

    private final VariableType variableType;
    private final int serialNumber;

    public StandardVariable(VariableType variableType, int serialNumber) {
        this.variableType = variableType;
        this.serialNumber = serialNumber;
    }

    @Override
    public String getStringVariable() {
        return switch (variableType)
        {
            case INPUT -> "x" + serialNumber;
            case WORK -> "z" + serialNumber;
            case OUTPUT -> "y";
            case EMPTY -> "";
        };
    }

    @Override
    public VariableType getVariableType() {
        return variableType;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public VariableDetails getVariableDetails() {
        return new VariableDetails(this.getStringVariable());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StandardVariable that = (StandardVariable) o;
        return serialNumber == that.serialNumber && variableType == that.variableType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableType, serialNumber);
    }

    @Override
    public Long evaluate(Context context) {
        return context.getVariableValue(this);
    }

    @Override
    public String getStringArgument() {
        return getStringVariable();
    }

    @Override
    public List<Variable> getVariable() {
        return List.of(this);
    }
}
