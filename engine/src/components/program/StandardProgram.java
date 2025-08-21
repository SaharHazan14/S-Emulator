package components.program;

import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.synthetic.ZeroVariableInstruction;
import components.jaxb.generated.SProgram;
import components.label.FixedLabel;
import components.label.Label;
import components.label.LabelFactory;
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
    public Program expand(int degree) {
        if (degree <= 0) {
            return this; // Or a copy of the program
        }

        Program expandedProgram = new StandardProgram(this.name + "_expanded_to_degree_" + degree);
        for (Instruction instruction : this.instructions) {
            // This is a simplified expansion. You will need to build this out.
            if (instruction.getName().equals("ZERO_VARIABLE")) {
                Label loopLabel = LabelFactory.createNewLabel();
                Variable var = instruction.getVariable();
                expandedProgram.addInstruction(new DecreaseInstruction(var, loopLabel));
                expandedProgram.addInstruction(new JumpNotZeroInstruction(var, loopLabel));
            } else {
                expandedProgram.addInstruction(instruction);
            }
        }

        return expandedProgram.expand(degree - 1);
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
        // This is a placeholder. You will need to implement the logic to
        // calculate the actual maximum degree of expansion possible.
        return 5;
    }

    @Override
    public int calculateCyclesNumber() {
        return 0;
        //
    }
}