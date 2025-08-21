package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.Label;
import components.variable.Variable;

public class IncreaseInstruction extends AbstractInstruction {
    public IncreaseInstruction(Variable variable) {
        super(InstructionSemantic.INCREASE, variable);
    }

    public IncreaseInstruction(Variable variable, Label label) {
        super(InstructionSemantic.INCREASE, variable, label);
    }

    // *** ADD THIS CONSTRUCTOR ***
    // This is the missing constructor for the expansion logic
    public IncreaseInstruction(Variable variable, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.INCREASE, variable, label, originalInstruction);
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %s + 1", variable, variable);

        return getInstructionDisplay(command);
    }
}