package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class JumpZeroInstruction extends AbstractInstruction {
    private final Label JZLabel;

    public JumpZeroInstruction(Variable variable, Label JZLabel) {
        this(variable, JZLabel, FixedLabel.EMPTY);
    }

    public JumpZeroInstruction(Variable variable, Label JZLabel, Label label) {
        super(InstructionSemantic.JUMP_ZERO, variable, label);
        this.JZLabel = JZLabel;
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        if (value == 0) {
            return JZLabel;
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        return String.format("IF %s = 0 GOTO %s", variable, JZLabel.getStringLabel());
    }

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel(), JZLabel);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        List<Instruction> instructions = new ArrayList<>();
        Variable v = this.getVariable();
        Label l =  this.getLabel();
        Label l1 = labelGenerator.getNextFreeLabel();

        instructions.add(new JumpNotZeroInstruction(v, l1, l));
        instructions.add(new GotoLabelInstruction(JZLabel));
        instructions.add(new NeutralInstruction(Variable.OUTPUT, l1));

        for (Instruction instruction : instructions) {
            instruction.setAncientInstruction(this);
        }

        return instructions;
    }
}
