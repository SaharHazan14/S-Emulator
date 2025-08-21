package components.program;

import components.instruction.Instruction;
import components.jaxb.generated.SProgram;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.StandardVariable;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StandardProgram implements Program {
    private final String name;
    private final List<Instruction> instructions;

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
            currentVariable = instruction.getVariable();

            if (currentVariable.getVariableType() == StandardVariable.VariableType.INPUT && !variables.contains(currentVariable)) {
                variables.add(currentVariable);
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
            currentLabel = instruction.getLabel();

            if (!labels.contains(currentLabel) && currentLabel != FixedLabel.EMPTY) {
                labels.add(currentLabel);
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
        instructions.add(instruction);
    }

    @Override
    public boolean validate() {
        return false;
        //
    }

    @Override
    public int calculateMaxDegree() {
        return 0;
        //
    }

    @Override
    public int calculateCyclesNumber() {
        return 0;
        //
    }
}
