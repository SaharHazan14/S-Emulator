package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.Label;
import components.variable.Variable;

public class NeutralInstruction extends AbstractInstruction {
    public NeutralInstruction(Variable variable) {
        super(InstructionSemantic.NEUTRAL, variable);
    }

    public NeutralInstruction(Variable variable, Label label) {
        super(InstructionSemantic.NEUTRAL, variable, label);
    }

    // *** ADD THIS CONSTRUCTOR ***
    // This is the missing constructor for the expansion logic
    public NeutralInstruction(Variable variable, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.NEUTRAL, variable, label, originalInstruction);
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %s", variable, variable);

        return getInstructionDisplay(command);
    }
}