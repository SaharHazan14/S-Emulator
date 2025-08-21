package components.instruction;

import components.label.Label;
import components.variable.Variable;

public interface Instruction {
    String getName();
    void execute();
    int getCyclesNumber();
    Label getLabel();
    Variable getVariable();
    String getStringInstruction();
    Instruction getOriginalInstruction(); // Add this method
    InstructionSemantic getInstructionSemantic();
}