package components.instruction;

import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionSemantic instructionSemantic;
    private final Variable variable;
    private final Label label;
    private final Instruction originalInstruction;

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable, Label label, Instruction originalInstruction) {
        this.instructionSemantic = instructionSemantic;
        this.variable = variable;
        this.label = label;
        this.originalInstruction = originalInstruction;
    }

    // Add this constructor
    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable, Instruction originalInstruction) {
        this(instructionSemantic, variable, FixedLabel.EMPTY, originalInstruction);
    }

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable, Label label) {
        this(instructionSemantic, variable, label, null);
    }

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable) {
        this(instructionSemantic, variable, FixedLabel.EMPTY, null);
    }

    @Override
    public String getName() {
        return instructionSemantic.getName();
    }

    @Override
    public InstructionSemantic getInstructionSemantic() {
        return instructionSemantic;
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

    @Override
    public Instruction getOriginalInstruction() {
        return originalInstruction;
    }

    @Override
    public Instruction getUltimateOriginalInstruction() {
        Instruction ultimate = this;
        while (ultimate.getOriginalInstruction() != null) {
            ultimate = ultimate.getOriginalInstruction();
        }
        return ultimate;
    }

    public String getInstructionDisplay(String command) {
        return String.format("(%c) [%-5s] %s (%d)",
                instructionSemantic.getInstructionTypeChar(), label.getStringLabel(), command, instructionSemantic.getCyclesNumber());
    }
}