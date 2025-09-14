package dtos;

import components.instruction.InstructionSemantic;

public record InstructionDetails (int ordinalNumber, InstructionSemantic.InstructionType type,
                                  LabelDetails label, String instructionContent, int cycles) {}
