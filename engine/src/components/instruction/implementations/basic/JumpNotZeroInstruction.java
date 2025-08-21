package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction {
    private final Label JNZLabel;

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel) {
        this(variable, JNZLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Label JNZLabel, Label label) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, label);
        this.JNZLabel = JNZLabel;
    }

    // *** ADD THIS CONSTRUCTOR ***
    public JumpNotZeroInstruction(Variable variable, Label JNZLabel, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, label, originalInstruction);
        this.JNZLabel = JNZLabel;
    }

    // This constructor is also needed for the expansion logic in StandardProgram
    public JumpNotZeroInstruction(Variable var, Label loopLabel, Instruction instruction) {
        super(InstructionSemantic.JUMP_NOT_ZERO, var, loopLabel, instruction);
        this.JNZLabel = loopLabel;
    }


    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s != 0 GOTO %s", variable, JNZLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}