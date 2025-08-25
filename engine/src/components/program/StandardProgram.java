package components.program;

import components.instruction.Instruction;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.instruction.implementations.synthetic.*;
import components.label.FixedLabel;
import components.label.Label;
import components.label.LabelFactory;
import components.variable.Variable;
import components.variable.VariableFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StandardProgram implements Program {

    private final String name;
    private final List<Variable> inputVariables;
    private final List<Instruction> instructions;

    public StandardProgram(String name, List<Variable> inputVariables, List<Instruction> instructions) {
        this.name = name;
        this.inputVariables = inputVariables;
        this.instructions = instructions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Variable> getInputVariables() {
        return Collections.unmodifiableList(inputVariables);
    }

    @Override
    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        for (Instruction instruction : instructions) {
            if (instruction.getLabel() != null && instruction.getLabel() != FixedLabel.EMPTY) {
                labels.add(instruction.getLabel());
            }
        }
        return Collections.unmodifiableList(labels);
    }

    @Override
    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public Program expand(int degree) {
        if (degree == 0) {
            return this;
        }

        List<Instruction> expanded = new ArrayList<>();
        for (Instruction instruction : this.instructions) {
            if (instruction.getInstructionSemantic().getDegree() > 0) {
                // This is a synthetic instruction, expand it
                if (instruction instanceof ZeroVariableInstruction) {
                    ZeroVariableInstruction original = (ZeroVariableInstruction) instruction;
                    Label loopLabel = LabelFactory.createNewLabel();
                    Variable var = original.getVariable();
                    expanded.add(new DecreaseInstruction(var, loopLabel, instruction));
                    expanded.add(new JumpNotZeroInstruction(var, loopLabel, instruction));
                }
                else if (instruction instanceof GotoLabelInstruction) {
                    GotoLabelInstruction original = (GotoLabelInstruction) instruction;
                    Variable tempVar = VariableFactory.createNewWorkVariable();
                    expanded.add(new IncreaseInstruction(tempVar, FixedLabel.EMPTY, instruction));
                    expanded.add(new JumpNotZeroInstruction(tempVar, original.getGotoLabel(), instruction));
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
                    expanded.add(new JumpNotZeroInstruction(original.getVariable(), endLabel, instruction));
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
                    expanded.add(new JumpNotZeroInstruction(vPrime, l1, instruction));
                    expanded.add(new GotoLabelInstruction(l3, FixedLabel.EMPTY, instruction));
                    expanded.add(new DecreaseInstruction(vPrime, l1, instruction));
                    expanded.add(new IncreaseInstruction(tempZ, FixedLabel.EMPTY, instruction));
                    expanded.add(new JumpNotZeroInstruction(vPrime, l1, instruction));
                    expanded.add(new DecreaseInstruction(tempZ, l2, instruction));
                    expanded.add(new IncreaseInstruction(v, FixedLabel.EMPTY, instruction));
                    expanded.add(new IncreaseInstruction(vPrime, FixedLabel.EMPTY, instruction));
                    expanded.add(new JumpNotZeroInstruction(tempZ, l2, instruction));
                }
                else {
                    expanded.add(instruction);
                }

            } else {
                // Basic instruction, just add it
                expanded.add(instruction);
            }
        }

        Program expandedProgram = new StandardProgram(this.name, this.inputVariables, expanded);
        return expandedProgram.expand(degree - 1);
    }

    @Override
    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int calculateMaxDegree() {
        int maxDegree = 0;
        for (Instruction instruction : instructions) {
            if (instruction.getInstructionSemantic().getDegree() > maxDegree) {
                maxDegree = instruction.getInstructionSemantic().getDegree();
            }
        }
        return maxDegree;
    }

    @Override
    public int calculateCyclesNumber() {
        int cycles = 0;
        for (Instruction instruction : instructions) {
            cycles += instruction.getCyclesNumber();
        }
        return cycles;
    }

    @Override
    public Optional<Variable> getVariableByName(String name) {
        if(name.equalsIgnoreCase("y"))
            return Optional.of(Variable.OUTPUT);

        return this.inputVariables.stream().filter(v -> v.getStringVariable().equalsIgnoreCase(name)).findFirst();
    }
}