package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.program.Program;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class JumpEqualVariableInstruction extends AbstractInstruction {
    private final Label JEVariableLabel;
    private final Variable variableName;

    public JumpEqualVariableInstruction(Variable variable, Label JEVariableLabel, Variable variableName) {
        this(variable, JEVariableLabel, variableName, FixedLabel.EMPTY);
    }

    public JumpEqualVariableInstruction(Variable variable, Label JEVariableLabel, Variable variableName, Label label) {
        super(InstructionSemantic.JUMP_EQUAL_VARIABLE, variable, label);
        this.JEVariableLabel = JEVariableLabel;
        this.variableName = variableName;
    }

    public Label getJEVariableLabel() {
        return JEVariableLabel;
    }

    public Variable getVariableName() {
        return variableName;
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        if (value == context.getVariableValue(variableName)) {
            return JEVariableLabel;
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        return String.format("IF %s = %s GOTO %s", variable, variableName.getStringVariable(), JEVariableLabel.getStringLabel());
    }

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel(), JEVariableLabel);
    }

    @Override
    public List<Variable> getAllInvolvedVariables() {
        return List.of(getVariable(), variableName);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        List<Instruction> instructions = new ArrayList<>();
        Variable v =  this.getVariable();
        Variable z1 = workVariableGenerator.getNextFreeWorkVariable();
        Variable z2 = workVariableGenerator.getNextFreeWorkVariable();
        Label l = this.getLabel();
        Label l1 = labelGenerator.getNextFreeLabel();
        Label l2 = labelGenerator.getNextFreeLabel();
        Label l3 = labelGenerator.getNextFreeLabel();

        instructions.add(new AssignmentInstruction(z1, v, l));
        instructions.add(new AssignmentInstruction(z2, variableName));
        instructions.add(new JumpZeroInstruction(z1, l3, l2));
        instructions.add(new JumpZeroInstruction(z2, l1));
        instructions.add(new DecreaseInstruction(z1));
        instructions.add(new DecreaseInstruction(z2));
        instructions.add(new GotoLabelInstruction(l2));
        instructions.add(new JumpZeroInstruction(z2, JEVariableLabel, l3));
        instructions.add(new NeutralInstruction(Variable.OUTPUT, l1));

        for (Instruction instruction : instructions) {
            instruction.setAncientInstruction(this);
        }

        return instructions;
    }
}
