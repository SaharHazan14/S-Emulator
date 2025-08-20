package components.program;

import components.instruction.Instruction;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.jaxb.generated.SInstruction;
import components.jaxb.generated.SInstructionArgument;
import components.jaxb.generated.SInstructionArguments;
import components.jaxb.generated.SProgram;
import components.label.FixedLabel;
import components.label.Label;
import components.label.StandardLabel;
import components.variable.StandardVariable;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class JaxbConversion {
    public static Program SProgramToProgram(SProgram sProgram) {
        Program program = new StandardProgram(sProgram.getName());

        for (SInstruction sInstruction: sProgram.getSInstructions().getSInstruction()) {
            program.addInstruction(SInstructionToInstruction(sInstruction));
        }

        return program;
    }

    private static Instruction SInstructionToInstruction(SInstruction sInstruction) {
        Variable instructionVariable = SVariableToVariable(sInstruction.getSVariable());
        Label instructionLabel = SLabelToLabel(sInstruction.getSLabel());
        SInstructionArguments sInstructionArguments = sInstruction.getSInstructionArguments();
        List<SInstructionArgument> argumentsList = new ArrayList<SInstructionArgument>();
        if (sInstructionArguments != null)
        {
            argumentsList = sInstructionArguments.getSInstructionArgument();
        }

        switch (sInstruction.getName()) {
            case "INCREASE" -> {
                return new IncreaseInstruction(instructionVariable, instructionLabel);
            }
            case "DECREASE" -> {
                return new DecreaseInstruction(instructionVariable, instructionLabel);
            }
            case "JUMP_NOT_ZERO" -> {
                Label JNZLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                return  new JumpNotZeroInstruction(instructionVariable,  JNZLabel, instructionLabel);
            }
            case "NEUTRAL" -> {
                return new NeutralInstruction(instructionVariable, instructionLabel);
            }
            default -> throw new RuntimeException("Unknown instruction: " + sInstruction.getName());
        }
    }

    private static Variable SVariableToVariable(String sVariable) {
        if (sVariable.isEmpty())
        {
            return Variable.EMPTY;
        }

        switch (sVariable.substring(0, 1)) {
            case "x" -> {
                return new StandardVariable(StandardVariable.VariableType.INPUT,
                        Integer.parseInt(sVariable.substring(1)));
            }
            case "z" -> {
                return new StandardVariable(StandardVariable.VariableType.WORK,
                        Integer.parseInt(sVariable.substring(1)));
            }
            case "y" -> {
                return Variable.OUTPUT;
            }
            default -> throw new IllegalArgumentException("Invalid variable name: " + sVariable);
        }
    }

    private static Label SLabelToLabel(String sLabel) {
        if (sLabel == null) {
            return FixedLabel.EMPTY;
        }

        if (sLabel.equals("EXIT"))
        {
            return FixedLabel.EXIT;
        }

        return new StandardLabel(Integer.parseInt(sLabel.substring(1)));
    }
}
