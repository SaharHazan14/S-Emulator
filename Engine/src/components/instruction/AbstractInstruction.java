package components.instruction;

import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;
import dtos.InstructionDetails;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionSemantic instructionSemantic;
    private final Variable variable;
    private final Label label;
    private int instructionNumber;
    private Instruction ancientInstruction;

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable) {
        this(instructionSemantic, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionSemantic instructionSemantic, Variable variable, Label label) {
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
    public int getDegree() {
        return instructionSemantic.getDegree();
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

//    public String getInstructionDisplay(String command) {
//        /*return String.format("#%d (%c) [ %-3s ] %s (%d)", instructionNumber,
//                instructionSemantic.getInstructionTypeChar(), label.getStringLabel(), command, instructionSemantic.getCyclesNumber());*/
//        return command;
//    }
    public abstract String getStringInstruction();

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel());
    }

    @Override
    public List<Variable> getAllInvolvedVariables() {
        return List.of(getVariable());
    }

    @Override
    public int getInstructionNumber() {
        return instructionNumber;
    }

    @Override
    public void setInstructionNumber(int instructionNumber) {
        this.instructionNumber = instructionNumber;
    }

    @Override
    public boolean hasAncientInstruction() {
        return ancientInstruction != null;
    }

    @Override
    public Instruction getAncientInstruction() {
        return ancientInstruction;
    }

    @Override
    public void setAncientInstruction(Instruction ancientInstruction) {
        this.ancientInstruction = ancientInstruction;
    }

    @Override
    public InstructionSemantic.InstructionType getInstructionType() {
        return instructionSemantic.getInstructionType();
    }

    @Override
    public InstructionDetails getInstructionDetails() {
        List<InstructionDetails> ancientInstructions = new ArrayList<>();
        Instruction currentInstruction = this;
        while (currentInstruction.hasAncientInstruction()) {
            currentInstruction = currentInstruction.getAncientInstruction();
            ancientInstructions.add(currentInstruction.getInstructionDetails());
        }

        return new InstructionDetails(instructionNumber, instructionSemantic,
                label.getLabelDetails(), getStringInstruction(), getCyclesNumber(), ancientInstructions);
    }
}
