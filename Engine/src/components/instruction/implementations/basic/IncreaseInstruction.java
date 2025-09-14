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

public class IncreaseInstruction extends AbstractInstruction {
    public IncreaseInstruction(Variable variable) {
        super(InstructionSemantic.INCREASE, variable);
    }

    public IncreaseInstruction(Variable variable, Label label) {
        super(InstructionSemantic.INCREASE, variable, label);
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        value++;
        context.updateVariableValue(getVariable(), value);

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- %s + 1", variable, variable);

        return getInstructionDisplay(command);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        Instruction newInstruction = new IncreaseInstruction(getVariable(), getLabel());
        newInstruction.setAncientInstruction(getAncientInstruction());
        return List.of(newInstruction);
    }
}
