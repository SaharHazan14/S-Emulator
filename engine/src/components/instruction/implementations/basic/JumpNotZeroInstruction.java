package components.instruction.implementations.basic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.Label;
import components.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction {

    private final Label targetLabel;

    public JumpNotZeroInstruction(Variable variable, Label targetLabel) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable);
        this.targetLabel = targetLabel;
    }

    public JumpNotZeroInstruction(Variable variable, Label targetLabel, Label instructionLabel) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, instructionLabel);
        this.targetLabel = targetLabel;
    }

    public JumpNotZeroInstruction(Variable variable, Label targetLabel, Instruction originalInstruction) {
        super(InstructionSemantic.JUMP_NOT_ZERO, variable, originalInstruction);
        this.targetLabel = targetLabel;
    }

    public Label getTargetLabel() {
        return targetLabel;
    }

    @Override
    public void execute() {
        // Execution logic is handled by the ProgramExecutor
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String command = String.format("IF %s != 0 GOTO %s", variable, targetLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}