package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.program.Program;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class JumpEqualConstantInstruction extends AbstractInstruction {
    private final Label JEConstantLabel;
    private final int constantValue;

    public JumpEqualConstantInstruction(Variable variable, Label JEConstantLabel, int constantValue) {
        this(variable, JEConstantLabel, constantValue, FixedLabel.EMPTY);
    }

    public JumpEqualConstantInstruction(Variable variable, Label JEConstantLabel, int constantValue, Label label) {
        super(InstructionSemantic.JUMP_EQUAL_CONSTANT, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.constantValue = constantValue;
    }

    public Label getJEConstantLabel() {
        return JEConstantLabel;
    }

    public int getConstantValue() {
        return constantValue;
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        if (value == constantValue) {
            return JEConstantLabel;
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        return String.format("IF %s = %d GOTO %s", variable, constantValue, JEConstantLabel.getStringLabel());
    }

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel(), JEConstantLabel);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        List <Instruction> instructions = new ArrayList<>();
        Variable v =  this.getVariable();
        Variable z1 = workVariableGenerator.getNextFreeWorkVariable();
        Label l = this.getLabel();
        Label l1 = labelGenerator.getNextFreeLabel();

        instructions.add(new AssignmentInstruction(z1, v, l));
        for (int i = 0; i < constantValue; i++)
        {
            instructions.add(new JumpZeroInstruction(z1, l1));
            instructions.add(new DecreaseInstruction(z1));
        }
        instructions.add(new JumpNotZeroInstruction(z1, l1));
        instructions.add(new GotoLabelInstruction(JEConstantLabel));
        instructions.add(new NeutralInstruction(Variable.OUTPUT, l1));

        for (Instruction instruction : instructions) {
            instruction.setAncientInstruction(this);
        }

        return instructions;
    }
}
