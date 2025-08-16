package components.variable;

public class StandardVariable implements Variable {
    public enum VariableType {INPUT, WORK, OUTPUT}

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
}
