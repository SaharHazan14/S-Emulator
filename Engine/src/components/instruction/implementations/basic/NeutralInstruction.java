package components.instruction.implementations.basic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.program.Program;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.List;

public class NeutralInstruction extends AbstractInstruction {
    public NeutralInstruction(Variable variable) {
        super(InstructionSemantic.NEUTRAL, variable);
    }

    public NeutralInstruction(Variable variable, Label label) {
        super(InstructionSemantic.NEUTRAL, variable, label);
    }

    @Override
    public Label execute(Context context) {
        long value = context.getVariableValue(getVariable());

        context.updateVariableValue(getVariable(), value);

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        return String.format("%s <- %s", variable, variable);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        Instruction newInstruction = new NeutralInstruction(getVariable(), getLabel());
        newInstruction.setAncientInstruction(getAncientInstruction());
        return List.of(newInstruction);
    }
}
