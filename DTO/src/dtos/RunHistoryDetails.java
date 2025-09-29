package dtos;

import java.io.Serializable;
import java.util.List;

public record RunHistoryDetails (int runNumber, int expansionDegree, List<Long> inputs, ContextDetails context, int cyclesNumber) implements Serializable {
    public RunHistoryDetails {
        inputs = List.copyOf(inputs);
    }
}
