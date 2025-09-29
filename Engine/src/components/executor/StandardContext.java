package components.executor;

import components.variable.Variable;
import dtos.ContextDetails;
import dtos.VariableDetails;

import java.util.*;

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

    @Override
    public List<Map.Entry<Variable, Long>> getVariablesContext() {
        List<Map.Entry<Variable, Long>> entriesList = new ArrayList<>(variables.entrySet());

        entriesList.sort((e1, e2) -> {
            int groupCompare = Integer.compare(groupOrder(e1.getKey()), groupOrder(e2.getKey()));
            if (groupCompare != 0) {
                return groupCompare;
            }
            return Integer.compare(e1.getKey().getSerialNumber(), e2.getKey().getSerialNumber());
        });

        return entriesList;
    }

    private int groupOrder(Variable key) {
        return switch (key.getVariableType()) {
            case OUTPUT -> 0;
            case INPUT -> 1;
            case WORK -> 2;
            default -> 3;
        };
    }

    @Override
    public ContextDetails getContextDetails() {
        List<Map.Entry<VariableDetails, Long>> sortedList = new ArrayList<>();

        for (Map.Entry<Variable,  Long> entry : getVariablesContext()) {
            sortedList.add(new AbstractMap.SimpleEntry<>(entry.getKey().getVariableDetails(), entry.getValue()));
        }

        return new ContextDetails(sortedList);
    }
}
