package components.variable;

public interface Variable {
    String getStringVariable();

    Variable OUTPUT = new StandardVariable(StandardVariable.VariableType.OUTPUT, 0);
}
