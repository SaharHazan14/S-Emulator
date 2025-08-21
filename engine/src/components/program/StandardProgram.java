package components.program;

import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.synthetic.ZeroVariableInstruction;
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

    private boolean isSynthetic(Instruction instruction) {
        return instruction.getInstructionSemantic().getInstructionType() == InstructionSemantic.InstructionType.SYNTHETIC;
    }

    private List<Instruction> expandInstruction(Instruction instruction) {
        List<Instruction> expanded = new ArrayList<>();

        if (instruction instanceof ZeroVariableInstruction) {
            Label loopLabel = LabelFactory.createNewLabel();
            Variable var = instruction.getVariable();

            // Create new instructions, passing the original instruction to the constructor
            expanded.add(new DecreaseInstruction(var, loopLabel, instruction));
            expanded.add(new JumpNotZeroInstruction(var, loopLabel, instruction));
        }
        // Add else-if blocks here for other synthetic instructions
        // else if (instruction instanceof GotoLabelInstruction) { ... }

        // If it's a synthetic instruction we haven't implemented yet, just return it as is for now
        if(expanded.isEmpty()){
            expanded.add(instruction);
        }

        return expanded;
    }


    @Override
    public Program expand(int degree) {
        if (degree <= 0) {
            return this;
        }

        Program currentProgram = this;
        for (int i = 0; i < degree; i++) {
            Program nextProgram = new StandardProgram(this.name + "_expanded_degree_" + (i + 1));
            boolean wasExpanded = false;
            for (Instruction instruction : currentProgram.getInstructions()) {
                if (isSynthetic(instruction)) {
                    nextProgram.getInstructions().addAll(expandInstruction(instruction));
                    wasExpanded = true;
                } else {
                    nextProgram.addInstruction(instruction);
                }
            }
            currentProgram = nextProgram;
            if (!wasExpanded) {
                // No more synthetic instructions to expand
                break;
            }
        }
        return currentProgram;
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
        int maxDegree = 0;
        for (Instruction instruction : instructions) {
            if (isSynthetic(instruction)) {
                // This is a simplified calculation. A more accurate one would
                // recursively check the expansion depth of each synthetic instruction.
                // For now, we assume each synthetic instruction adds one degree.
                maxDegree = Math.max(maxDegree, 1); // Placeholder
            }
        }
        // A simple but effective placeholder for now
        return maxDegree > 0 ? 5 : 0;
    }

    @Override
    public int calculateCyclesNumber() {
        return 0;
        //
    }
}