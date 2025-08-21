package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpEqualConstantInstruction extends AbstractInstruction {
    private final Label JEConstantLabel;
    private final int constantValue;

    public JumpEqualConstantInstruction(Variable variable, Label JEConstantLabel, int constantValue) {
        this(variable, JEConstantLabel, constantValue, FixedLabel.EMPTY);
    }

    public JumpEqualConstantInstruction(Variable variable, Label JEConstantLabel, int constantValue, Label label) {
        super(InstructionSemantic.JUMP_EQUAL_CONSTANT, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.constantValue = constantValue;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s = %d GOTO %s", variable, constantValue, JEConstantLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}
