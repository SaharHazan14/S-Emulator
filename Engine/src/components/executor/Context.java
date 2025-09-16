package components.executor;

import com.sun.tools.rngdatatype.ValidationContext;
import components.variable.Variable;
import dtos.ContextDetails;
import dtos.VariableDetails;

import java.util.List;
import java.util.Map;

public interface Context {
    long getVariableValue(Variable variable);
    void updateVariableValue(Variable variable, long value);
    Map<Variable, Long> getVariables();
    List<Map.Entry<Variable, Long>> getVariablesContext();
    ContextDetails getContextDetails();
}
