package components.executor;

import components.program.Program;
import components.variable.Variable;

import java.util.List;
import java.util.Map;

public interface Executor {
    Long run(Program program, List<Long> input);
    Map<Variable, Long> getVariablesStates();
    long getCyclesConsumed();

}
