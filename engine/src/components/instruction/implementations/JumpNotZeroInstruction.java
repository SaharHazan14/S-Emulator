package components.instruction.implementations;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction {
    private Label jumpToLabel;

    public JumpNotZeroInstruction(Variable variable, Label jumpToLabel) {
        this(variable, jumpToLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Label jumpToLabel, Label label) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, label);
        this.jumpToLabel = jumpToLabel;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s != 0 GOTO %s", variable, jumpToLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}
