package components.instruction.implementations;

import components.instruction.AbstractInstruction;
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
