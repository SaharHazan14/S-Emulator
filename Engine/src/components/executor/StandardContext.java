package components.executor;

import components.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class StandardContext implements Context {
    private final Map<Variable, Long> variables;

    public StandardContext() {
        this.variables = new HashMap<>();
    }

    @Override
    public long getVariableValue(Variable variable) {
        if (variables.containsKey(variable)) {
            return variables.get(variable);
        }

        return 0;
    }

    @Override
    public void updateVariableValue(Variable variable, long value) {
        variables.put(variable, value);
    }

    @Override
    public Map<Variable, Long> getVariables() {
        return variables;
    }
}
