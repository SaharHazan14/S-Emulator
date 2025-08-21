package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class AssignmentInstruction extends AbstractInstruction {
    private final Variable assignedVariable;

    public AssignmentInstruction(Variable variable, Variable assignedVariable) {
        this(variable, assignedVariable, FixedLabel.EMPTY);
    }

    public AssignmentInstruction(Variable variable, Variable assignedVariable, Label label) {
        super(InstructionSemantic.ASSIGNMENT, variable, label);
        this.assignedVariable = assignedVariable;
    }

    public Variable getAssignedVariable() {
        return assignedVariable;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %s", variable, assignedVariable.getStringVariable());

        return getInstructionDisplay(command);
    }
}