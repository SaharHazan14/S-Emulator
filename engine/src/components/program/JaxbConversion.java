package components.program;

import components.instruction.Instruction;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.instruction.implementations.synthetic.*;
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
        // Per your instructions, we create empty input lists. The executor will handle all terminal inputs.
        List<Variable> inputVariables = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();

        Program program = new StandardProgram(sProgram.getName(), inputVariables, instructions);

        if (sProgram.getSInstructions() != null && sProgram.getSInstructions().getSInstruction() != null) {
            for (SInstruction sInstruction : sProgram.getSInstructions().getSInstruction()) {
                program.addInstruction(SInstructionToInstruction(sInstruction));
            }
        }
        return program;
    }

    private static Instruction SInstructionToInstruction(SInstruction sInstruction) {
        Variable instructionVariable = SVariableToVariable(sInstruction.getSVariable());
        Label instructionLabel = SLabelToLabel(sInstruction.getSLabel());
        SInstructionArguments sInstructionArguments = sInstruction.getSInstructionArguments();
        List<SInstructionArgument> argumentsList = new ArrayList<>();
        if (sInstructionArguments != null) {
            argumentsList = sInstructionArguments.getSInstructionArgument();
        }

        switch (sInstruction.getName()) {
            case "INCREASE":
                return new IncreaseInstruction(instructionVariable, instructionLabel);
            case "DECREASE":
                return new DecreaseInstruction(instructionVariable, instructionLabel);
            case "JUMP_NOT_ZERO":
                Label JNZLabel = SLabelToLabel(argumentsList.get(0).getValue());
                return new JumpNotZeroInstruction(instructionVariable,  JNZLabel, instructionLabel);
            case "NEUTRAL":
                return new NeutralInstruction(instructionVariable, instructionLabel);
            case "ZERO_VARIABLE":
                return new ZeroVariableInstruction(instructionVariable, instructionLabel);
            case "GOTO_LABEL":
                Label gotoLabel = SLabelToLabel(argumentsList.get(0).getValue());
                return new GotoLabelInstruction(gotoLabel, instructionLabel);
            case "ASSIGNMENT":
                Variable assignmentVariable = SVariableToVariable(argumentsList.get(0).getValue());
                return new AssignmentInstruction(instructionVariable, assignmentVariable, instructionLabel);
            case "CONSTANT_ASSIGNMENT":
                int constantValue = Integer.parseInt(argumentsList.get(0).getValue());
                return new ConstantAssignmentInstruction(instructionVariable, constantValue, instructionLabel);
            case "JUMP_ZERO":
                Label JZLabel = SLabelToLabel(argumentsList.get(0).getValue());
                return new JumpZeroInstruction(instructionVariable, JZLabel, instructionLabel);
            case "JUMP_EQUAL_CONSTANT":
                Label JEConstantLabel = SLabelToLabel(argumentsList.get(0).getValue());
                int constValue = Integer.parseInt(argumentsList.get(1).getValue());
                return new JumpEqualConstantInstruction(instructionVariable, JEConstantLabel, constValue, instructionLabel);
            case "JUMP_EQUAL_VARIABLE":
                Label JEVariableLabel = SLabelToLabel(argumentsList.get(0).getValue());
                Variable variableName = SVariableToVariable(argumentsList.get(1).getValue());
                return new JumpEqualVariableInstruction(instructionVariable, JEVariableLabel, variableName, instructionLabel);
            default:
                throw new RuntimeException("Unknown instruction: " + sInstruction.getName());
        }
    }

    private static Variable SVariableToVariable(String sVariable) {
        if (sVariable == null || sVariable.isEmpty()) {
            return Variable.EMPTY;
        }
        switch (sVariable.substring(0, 1).toLowerCase()) {
            case "x":
                return new StandardVariable(StandardVariable.VariableType.INPUT,
                        Integer.parseInt(sVariable.substring(1)));
            case "z":
                return new StandardVariable(StandardVariable.VariableType.WORK,
                        Integer.parseInt(sVariable.substring(1)));
            case "y":
                return Variable.OUTPUT;
            default:
                throw new IllegalArgumentException("Invalid variable name: " + sVariable);
        }
    }

    private static Label SLabelToLabel(String sLabel) {
        if (sLabel == null || sLabel.isEmpty()) {
            return FixedLabel.EMPTY;
        }
        if (sLabel.equals("EXIT")) {
            return FixedLabel.EXIT;
        }
        return new StandardLabel(Integer.parseInt(sLabel.substring(1)));
    }
}