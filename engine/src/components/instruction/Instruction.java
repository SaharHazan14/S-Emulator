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
    InstructionSemantic getInstructionSemantic();
    Instruction getOriginalInstruction();

    /**
     * Finds the top-level ancestor of this instruction in an expansion chain.
     * If the instruction was not created by an expansion, it returns itself.
     * @return The highest-level original instruction.
     */
    Instruction getUltimateOriginalInstruction();
}
