package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpEqualVariableInstruction extends AbstractInstruction {
    private final Label JEVariableLabel;
    private final Variable variableName;

    public JumpEqualVariableInstruction(Variable variable, Label JEVariableLabel, Variable variableName) {
        this(variable, JEVariableLabel, variableName, FixedLabel.EMPTY);
    }

    public JumpEqualVariableInstruction(Variable variable, Label JEVariableLabel, Variable variableName, Label label) {
        super(InstructionSemantic.JUMP_EQUAL_VARIABLE, variable, label);
        this.JEVariableLabel = JEVariableLabel;
        this.variableName = variableName;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s = %s GOTO %s", variable, variableName.getStringVariable(), JEVariableLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}
