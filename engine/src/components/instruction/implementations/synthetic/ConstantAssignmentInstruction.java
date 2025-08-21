package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class ConstantAssignmentInstruction extends AbstractInstruction {
    private final int constantValue;

    public ConstantAssignmentInstruction(Variable variable, int constantValue) {
        this(variable, constantValue, FixedLabel.EMPTY);
    }

    public ConstantAssignmentInstruction(Variable variable, int constantValue, Label label) {
        super(InstructionSemantic.CONSTANT_ASSIGNMENT, variable, label);
        this.constantValue = constantValue;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %d", variable, constantValue);

        return getInstructionDisplay(command);
    }


    public int getConstantValue() {
        return constantValue;
    }
}
