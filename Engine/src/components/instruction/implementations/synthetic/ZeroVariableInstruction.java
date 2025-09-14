package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class ZeroVariableInstruction extends AbstractInstruction {
    public ZeroVariableInstruction(Variable variable) {
        super(InstructionSemantic.ZERO_VARIABLE, variable);
    }

    public ZeroVariableInstruction(Variable variable, Label label) {
        super(InstructionSemantic.ZERO_VARIABLE, variable, label);
    }

    @Override
    public Label execute(Context context) {
        context.updateVariableValue(getVariable(), 0);

        return FixedLabel.EMPTY;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        return String.format("%s <- 0", variable);
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        List<Instruction> instructions = new ArrayList<>();
        Variable v = this.getVariable();
        Label l;

        if (getLabel().equals(FixedLabel.EMPTY)) {
            l = labelGenerator.getNextFreeLabel();
        }
        else {
            l = getLabel();
        }

        instructions.add(new DecreaseInstruction(v, l));
        instructions.add(new JumpNotZeroInstruction(v, l));

        for (Instruction instruction : instructions) {
            instruction.setAncientInstruction(this);
        }

        return instructions;
    }
}
