package components.instruction.implementations.basic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.List;

public class JumpNotZeroInstruction extends AbstractInstruction {
    private final Label JNZLabel;

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel) {
        this(variable, JNZLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel, Label label) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, label);
        this.JNZLabel = JNZLabel;
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        if (value != 0) {
            return JNZLabel;
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s != 0 GOTO %s", variable, JNZLabel.getStringLabel());

        return getInstructionDisplay(command);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        Instruction newInstruction = new JumpNotZeroInstruction(getVariable(), JNZLabel, getLabel());
        newInstruction.setAncientInstruction(getAncientInstruction());
        return List.of(newInstruction);
    }

    @Override
    public List<Label> getAllInvolvedLabels() {
        return List.of(getLabel(), JNZLabel);
    }
}
