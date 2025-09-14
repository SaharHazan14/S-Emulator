package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class GotoLabelInstruction extends AbstractInstruction {
    private final Label gotoLabel;

    public GotoLabelInstruction(Label gotoLabel) {
        this(gotoLabel, FixedLabel.EMPTY);
    }

    public GotoLabelInstruction(Label gotoLabel, Label label) {
        super(InstructionSemantic.GOTO_LABEL, Variable.EMPTY,  label);
        this.gotoLabel = gotoLabel;
    }

    @Override
    public Label execute(Context context) {
        return gotoLabel;
    }

    @Override
    public String getStringInstruction() {
        String command = String.format("GOTO %s", gotoLabel.getStringLabel());

        return getInstructionDisplay(command);
    }

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel(), gotoLabel);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        List<Instruction> instructions = new ArrayList<>();
        Variable z1 = workVariableGenerator.getNextFreeWorkVariable();
        Label l = this.getLabel();

        instructions.add(new IncreaseInstruction(z1, l));
        instructions.add(new JumpNotZeroInstruction(z1, gotoLabel));

        for (Instruction instruction : instructions) {
            instruction.setAncientInstruction(this);
        }

        return instructions;
    }
}
