package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.Label;
import components.variable.Variable;

public class DecreaseInstruction extends AbstractInstruction {
    public DecreaseInstruction(Variable variable) {
        super(InstructionSemantic.DECREASE, variable);
    }

    public DecreaseInstruction(Variable variable, Label label) {
        super(InstructionSemantic.DECREASE, variable, label);
    }

    // *** ADD THIS CONSTRUCTOR ***
    public DecreaseInstruction(Variable variable, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.DECREASE, variable, label, originalInstruction);
    }


    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %s - 1", variable, variable);

        return getInstructionDisplay(command);
    }
}