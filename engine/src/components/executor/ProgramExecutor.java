package components.executor;

import components.program.Program;
import components.variable.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramExecutor implements Executor {

    private Map<Variable, Long> variables;
    private long cyclesConsumed;

    public ProgramExecutor() {
        this.variables = new HashMap<>();
        this.cyclesConsumed = 0;
    }
    @Override
    public Long run(Program program, List<Long> input) {
        return 0L;
    }

    @Override
    public Map<Variable, Long> getVariablesStates() {
        return Map.of();
    }

    @Override
    public long getCyclesConsumed() {
        return cyclesConsumed;
    }
}
