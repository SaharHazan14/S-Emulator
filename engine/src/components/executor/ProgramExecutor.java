package components.executor;

import components.variable.Variable;

import java.util.Map;

public class ProgramExecutor implements Executor {
    @Override
    public Long run(Long... input) {
        return 0L;
    }

    @Override
    public Map<Variable, Long> getVariablesStates() {
        return Map.of();
    }
}
