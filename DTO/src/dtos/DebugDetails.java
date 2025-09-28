package dtos;

import java.util.List;

public record DebugDetails (ContextDetails context, int cycles, int lineIndex, boolean programEnded, VariableDetails changedVariable) {}
