package components.variable;

import dtos.VariableDetails;

public interface Variable {
    String getStringVariable();
    StandardVariable.VariableType getVariableType();
    int getSerialNumber();
    VariableDetails getVariableDetails();

    Variable OUTPUT = new StandardVariable(StandardVariable.VariableType.OUTPUT, 0);
    Variable EMPTY = new StandardVariable(StandardVariable.VariableType.EMPTY, 0);
}
