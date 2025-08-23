package dtos;

import java.util.List;

/**
 * A record to hold the details of a single program execution for statistics.
 */
public record RunHistoryDetails(
        int runNumber,
        int expansionDegree,
        List<Long> inputs,
        long outputY,
        long cyclesConsumed
) {
}
