package components.program;

import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.*;
import components.instruction.implementations.synthetic.*;
import components.label.FixedLabel;
import components.label.Label;
import components.label.LabelFactory;
import components.variable.StandardVariable;
import components.variable.Variable;
import components.variable.VariableFactory;

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

        for (Instruction instruction : instructions) {
            currentVariable = instruction.getVariable();
            if (currentVariable != null && currentVariable.getVariableType() == StandardVariable.VariableType.INPUT && !variables.contains(currentVariable)) {
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

        for (Instruction instruction : instructions) {
            currentLabel = instruction.getLabel();
            if (currentLabel != null && !labels.contains(currentLabel) && currentLabel != FixedLabel.EMPTY) {
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

    private boolean isSynthetic(Instruction instruction) {
        return instruction.getInstructionSemantic().getInstructionType() == InstructionSemantic.InstructionType.SYNTHETIC;
    }

    private List<Instruction> expandInstruction(Instruction instruction) {
        List<Instruction> expanded = new ArrayList<>();

        if (instruction instanceof ZeroVariableInstruction) {
            Label loopLabel = LabelFactory.createNewLabel();
            Variable var = instruction.getVariable();
            expanded.add(new DecreaseInstruction(var, loopLabel, instruction));
            expanded.add(new JumpNotZeroInstruction(var, loopLabel, instruction));
        }
        else if (instruction instanceof GotoLabelInstruction) {
            GotoLabelInstruction original = (GotoLabelInstruction) instruction;
            Variable tempVar = VariableFactory.createNewWorkVariable();
            expanded.add(new IncreaseInstruction(tempVar, FixedLabel.EMPTY, instruction));
            expanded.add(new JumpNotZeroInstruction(tempVar, original.getGotoLabel(), FixedLabel.EMPTY, instruction));
        }
        else if (instruction instanceof ConstantAssignmentInstruction) {
            ConstantAssignmentInstruction original = (ConstantAssignmentInstruction) instruction;
            Variable var = original.getVariable();
            int k = original.getConstantValue();
            expanded.add(new ZeroVariableInstruction(var, FixedLabel.EMPTY, instruction));
            for (int i = 0; i < k; i++) {
                expanded.add(new IncreaseInstruction(var, FixedLabel.EMPTY, instruction));
            }
        }
        else if (instruction instanceof JumpZeroInstruction) {
            JumpZeroInstruction original = (JumpZeroInstruction) instruction;
            Label endLabel = LabelFactory.createNewLabel();
            expanded.add(new JumpNotZeroInstruction(original.getVariable(), endLabel, FixedLabel.EMPTY, instruction));
            expanded.add(new GotoLabelInstruction(original.getJZLabel(), FixedLabel.EMPTY, instruction));
            expanded.add(new NeutralInstruction(Variable.EMPTY, endLabel, instruction));
        }
        else if (instruction instanceof AssignmentInstruction) {
            AssignmentInstruction original = (AssignmentInstruction) instruction;
            Variable v = original.getVariable();
            Variable vPrime = original.getAssignedVariable();
            Variable tempZ = VariableFactory.createNewWorkVariable();

            Label l1 = LabelFactory.createNewLabel();
            Label l2 = LabelFactory.createNewLabel();
            Label l3 = LabelFactory.createNewLabel();

            expanded.add(new ZeroVariableInstruction(v, FixedLabel.EMPTY, instruction));
            expanded.add(new JumpNotZeroInstruction(vPrime, l1, FixedLabel.EMPTY, instruction));
            expanded.add(new GotoLabelInstruction(l3, FixedLabel.EMPTY, instruction));
            expanded.add(new DecreaseInstruction(vPrime, l1, instruction));
            expanded.add(new IncreaseInstruction(tempZ, FixedLabel.EMPTY, instruction));
            expanded.add(new JumpNotZeroInstruction(vPrime, l1, FixedLabel.EMPTY, instruction));
            expanded.add(new DecreaseInstruction(tempZ, l2, instruction));
            expanded.add(new IncreaseInstruction(v, FixedLabel.EMPTY, instruction));
            expanded.add(new IncreaseInstruction(vPrime, FixedLabel.EMPTY, instruction));
            expanded.add(new JumpNotZeroInstruction(tempZ, l2, FixedLabel.EMPTY, instruction));
            expanded.add(new NeutralInstruction(v, l3, instruction));
        }

        if (expanded.isEmpty()) {
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
                break;
            }
        }
        return currentProgram;
    }

    @Override
    public boolean validate() {
        return false;
    }

    /**
     * Recursively calculates the expansion degree of a single instruction.
     */
    private int getInstructionDegree(Instruction instruction) {
        if (!isSynthetic(instruction)) {
            return 0;
        }

        List<Instruction> children = expandInstruction(instruction);
        // This case handles synthetic instructions that we haven't implemented the expansion for yet.
        if (children.size() == 1 && children.get(0) == instruction) {
            return 1;
        }

        int maxChildDegree = 0;
        for (Instruction child : children) {
            maxChildDegree = Math.max(maxChildDegree, getInstructionDegree(child));
        }

        return 1 + maxChildDegree;
    }

    /**
     * Calculates the maximum expansion degree for the entire program.
     */
    @Override
    public int calculateMaxDegree() {
        int maxDegree = 0;
        for (Instruction instruction : this.instructions) {
            maxDegree = Math.max(maxDegree, getInstructionDegree(instruction));
        }
        return maxDegree;
    }

    @Override
    public int calculateCyclesNumber() {
        return 0;
    }
}