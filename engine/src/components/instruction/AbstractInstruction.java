package components.instruction;

import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionSemantic instructionSemantic;
    private final Variable variable;
    private final Label label;
    private final Instruction originalInstruction;

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable, Instruction originalInstruction) {
        this(instructionSemantic, variable, FixedLabel.EMPTY, originalInstruction);
    }

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable,  Label label, Instruction originalInstruction) {
        this.instructionSemantic = instructionSemantic;
        this.variable = variable;
        this.label = label;
        this.originalInstruction = originalInstruction;
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

    public String getInstructionDisplay(String command) {
        return String.format("(%c) [ %-3s ] %s (%d)",
                instructionSemantic.getInstructionTypeChar(), label.getStringLabel(), command, instructionSemantic.getCyclesNumber());
    }

    public Instruction getOriginalInstruction() {
        return originalInstruction;
    }


    public InstructionSemantic getInstructionSemantic() {
        return instructionSemantic;
    }
}
