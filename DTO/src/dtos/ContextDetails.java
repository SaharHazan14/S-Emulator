package dtos;

import java.util.List;
import java.util.Map;

public record ContextDetails (List<Map.Entry<VariableDetails, Long>> variablesContext) {
    public ContextDetails {
        variablesContext = List.copyOf(variablesContext);
    }
}
