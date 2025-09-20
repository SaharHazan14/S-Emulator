package dtos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record RunHistoryDetails (int runNumber, int expansionDegree, List<Long> inputs, ContextDetails context, int cyclesNumber) implements Serializable {
    public RunHistoryDetails {
        inputs = List.copyOf(inputs);
    }
}
