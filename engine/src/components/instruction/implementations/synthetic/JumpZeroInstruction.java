package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpZeroInstruction extends AbstractInstruction {
    private final Label JZLabel;

    public JumpZeroInstruction(Variable variable, Label JZLabel) {
        this(variable, JZLabel, FixedLabel.EMPTY);
    }

    public JumpZeroInstruction(Variable variable, Label JZLabel, Label label) {
        super(InstructionSemantic.JUMP_ZERO, variable, label, null);
        this.JZLabel = JZLabel;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s = 0 GOTO %s", variable, JZLabel.getStringLabel());

        return getInstructionDisplay(command);
    }

}
