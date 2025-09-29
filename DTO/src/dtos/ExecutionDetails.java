package dtos;

public record ExecutionDetails(ProgramDetails programDetails, ContextDetails variablesContext, int cycles) {}
