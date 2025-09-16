package dtos;

import components.executor.Context;

public record ExecutionDetails(ProgramDetails programDetails, ContextDetails variablesContext, int cycles) {}
