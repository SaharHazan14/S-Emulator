package components.variable;

public interface Variable {
    String getStringVariable();
    StandardVariable.VariableType getVariableType();
    int getSerialNumber();

    Variable OUTPUT = new StandardVariable(StandardVariable.VariableType.OUTPUT, 0);
}
