package dtos;

public record DebugDetails (ContextDetails context, int cycles, int lineIndex, boolean programEnded) {}
