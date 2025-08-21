package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.Label;
import components.variable.Variable;

public class ZeroVariableInstruction extends AbstractInstruction {
    public ZeroVariableInstruction(Variable variable) {
        super(InstructionSemantic.ZERO_VARIABLE, variable);
    }

    public ZeroVariableInstruction(Variable variable, Label label) {
        super(InstructionSemantic.ZERO_VARIABLE, variable, label);
    }

    // *** ADD THIS CONSTRUCTOR ***
    // This is the missing constructor for the expansion logic
    public ZeroVariableInstruction(Variable variable, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.ZERO_VARIABLE, variable, label, originalInstruction);
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("%s <- 0", variable);

        return getInstructionDisplay(command);
    }
}