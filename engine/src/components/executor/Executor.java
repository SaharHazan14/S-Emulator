package components.executor;

import components.variable.Variable;

import java.util.Map;

public interface Executor {
    Long run(Long... input);
    Map<Variable, Long> getVariablesStates();
}
