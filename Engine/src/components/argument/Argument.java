package components.argument;

import components.executor.Context;
import components.variable.Variable;

import java.util.List;

public interface Argument {
    Long evaluate(Context context);
    List<Variable> getVariable();
    String getStringArgument();
}
