package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction {
    private final Label JNZLabel;

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel) {
        this(variable, JNZLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel, Label label) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, label);
        this.JNZLabel = JNZLabel;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s != 0 GOTO %s", variable, JNZLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}
