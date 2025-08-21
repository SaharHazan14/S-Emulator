package components.instruction.implementations.synthetic;

import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.Label;
import components.variable.Variable;

public class GotoLabelInstruction extends AbstractInstruction {
    private final Label gotoLabel;

    public GotoLabelInstruction(Label gotoLabel) {
        this(gotoLabel, FixedLabel.EMPTY);
    }

    public GotoLabelInstruction(Label gotoLabel, Label label) {
        super(InstructionSemantic.GOTO_LABEL, Variable.EMPTY,  label);
        this.gotoLabel = gotoLabel;
    }

    // This is the constructor that fixes the error
    public GotoLabelInstruction(Label gotoLabel, Label label, Instruction originalInstruction) {
        super(InstructionSemantic.GOTO_LABEL, Variable.EMPTY, label, originalInstruction);
        this.gotoLabel = gotoLabel;
    }

    public Label getGotoLabel() {
        return gotoLabel;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getStringInstruction() {
        String command = String.format("GOTO %s", gotoLabel.getStringLabel());

        return getInstructionDisplay(command);
    }
}