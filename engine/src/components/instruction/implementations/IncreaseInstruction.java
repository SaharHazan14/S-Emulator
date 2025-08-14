package components.instruction.implementations;

import components.instruction.AbstractInstruction;
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

    @Override
    public void execute() {

    }
}
