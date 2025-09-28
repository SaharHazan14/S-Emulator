package dtos;

import components.instruction.InstructionSemantic;

import java.util.List;

public record InstructionDetails (int ordinalNumber, InstructionSemantic type,
                                  LabelDetails label, String instructionContent, int cycles,
                                  List<InstructionDetails> ancientInstructionsList) {
    public InstructionDetails {
        ancientInstructionsList = List.copyOf(ancientInstructionsList);
    }
}
