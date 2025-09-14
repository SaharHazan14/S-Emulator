package components.program;

import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.StandardVariable;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StandardProgram implements Program {
    private final String name;
    private final List<Instruction> instructions;
    private int nextInstructionNumber;

    public StandardProgram(String name) {
        this.name = name;
        this.instructions = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Variable> getInputVariables() {
        List<Variable> variables = new ArrayList<>();
        Variable currentVariable;

        for(Instruction instruction : instructions) {
            for (Variable variable: instruction.getAllInvolvedVariables()) {
                currentVariable = variable;

                if (currentVariable.getVariableType() == StandardVariable.VariableType.INPUT && !variables.contains(currentVariable)) {
                    variables.add(currentVariable);
                }
            }
        }

        variables.sort(Comparator.comparingInt(Variable::getSerialNumber));
        return variables;
    }
    @Override
    public List<Variable> getWorkVariables() {
        List<Variable> variables = new ArrayList<>();
        Variable currentVariable;

        for(Instruction instruction : instructions) {
            for (Variable variable: instruction.getAllInvolvedVariables()) {
                currentVariable = variable;

                if (currentVariable.getVariableType() == StandardVariable.VariableType.WORK && !variables.contains(currentVariable)) {
                    variables.add(currentVariable);
                }
            }
        }

        variables.sort(Comparator.comparingInt(Variable::getSerialNumber));
        return variables;
    }

    @Override
    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        Label currentLabel;

        for(Instruction instruction : instructions) {
            for (Label label: instruction.getAllInvolvedLabels()) {
                currentLabel = label;

                if (!labels.contains(currentLabel) && currentLabel != FixedLabel.EMPTY) {
                    labels.add(currentLabel);
                }
            }
        }

        labels.sort(Comparator.comparingInt(Label::getSerialNumber));
        return labels;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public void addInstruction(Instruction instruction) {
        instruction.setInstructionNumber(++nextInstructionNumber);
        instructions.add(instruction);
    }

    @Override
    public int calculateMaxDegree() {
        int maxDegree = 0;

        for (Instruction instruction : instructions) {
            if (instruction.getDegree() > maxDegree) {
                maxDegree = instruction.getDegree();
            }
        }

        return maxDegree;
    }

    @Override
    public int getNextFreeLabelNumber() {
        int maxLabelNumber = 0;

        for (Label label : getLabels()) {
            if (label.getSerialNumber() > maxLabelNumber && label != FixedLabel.EXIT) {
                maxLabelNumber = label.getSerialNumber();
            }
        }

        return maxLabelNumber + 1;
    }

    @Override
    public int getNextFreeWorkVariableNumber() {
        int maxWorkVariableNumber = 0;

        for(Instruction instruction : instructions) {
            for (Variable variable: instruction.getAllInvolvedVariables()) {
                if (variable.getVariableType() == StandardVariable.VariableType.WORK && variable.getSerialNumber() > maxWorkVariableNumber) {
                    maxWorkVariableNumber = variable.getSerialNumber();
                }
            }
        }

        return maxWorkVariableNumber + 1;
    }

    @Override
    public Program expand() {
        Program expandedProgram = new StandardProgram(name);
        FreeLabelGenerator nextFreeLabel = new FreeLabelGenerator(getNextFreeLabelNumber());
        FreeWorkVariableGenerator nextFreeWorkVariable = new FreeWorkVariableGenerator(getNextFreeWorkVariableNumber());

        for (Instruction instruction : instructions) {
            List<Instruction> currentExpand = instruction.expand(nextFreeLabel, nextFreeWorkVariable);
            for (Instruction baseInstruction : currentExpand) {
                expandedProgram.addInstruction(baseInstruction);
            }
        }

        return expandedProgram;
    }

    @Override
    public int calculateBasicInstructionsNumber() {
        int sum = 0;

        for (Instruction instruction : instructions) {
            if (instruction.getInstructionType() == InstructionSemantic.InstructionType.BASIC) {
                sum++;
            }
        }

        return sum;
    }
}
