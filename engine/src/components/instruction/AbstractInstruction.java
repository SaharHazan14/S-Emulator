package components.instruction;

import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionSemantic instructionSemantic;
    private final Variable variable;
    private final Label label;

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable) {
        this(instructionSemantic, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable,  Label label) {
        this.instructionSemantic = instructionSemantic;
        this.variable = variable;
        this.label = label;
    }

    @Override
    public String getName() {
        return instructionSemantic.getName();
    }

    @Override
    public int getCyclesNumber() {
        return instructionSemantic.getCyclesNumber();
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }
}
