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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaxbConversion {
    public static Program SProgramToProgram(SProgram sProgram) {
        // --- THE DEFINITIVE FIX STARTS HERE ---
        List<Instruction> instructions = new ArrayList<>();
        Set<Variable> foundInputVariables = new HashSet<>();

        // First, we must iterate through all instructions from the XML to build our own instruction list
        // and simultaneously discover all the input variables (x1, x2, etc.) that the program uses.
        if (sProgram.getSInstructions() != null && sProgram.getSInstructions().getSInstruction() != null) {
            for (SInstruction sInstruction : sProgram.getSInstructions().getSInstruction()) {
                Instruction instruction = SInstructionToInstruction(sInstruction);
                instructions.add(instruction);

                // Check the primary variable of the instruction (e.g., the 'x1' in "x1 <- x1 - 1").
                Variable primaryVar = instruction.getVariable();
                if (primaryVar != null && primaryVar.getVariableType() == StandardVariable.VariableType.INPUT) {
                    foundInputVariables.add(primaryVar);
                }

                // Also check variables in the arguments (e.g., the second 'x1' in "y <- x1").
                if (sInstruction.getSInstructionArguments() != null && sInstruction.getSInstructionArguments().getSInstructionArgument() != null) {
                    for (SInstructionArgument arg : sInstruction.getSInstructionArguments().getSInstructionArgument()) {
                        // A simple check to see if an argument is an input variable.
                        if (arg.getValue() != null && arg.getValue().toLowerCase().startsWith("x")) {
                            foundInputVariables.add(SVariableToVariable(arg.getValue()));
                        }
                    }
                }
            }
        }

        // Convert the set of unique input variables we found into a list.
        List<Variable> inputVariables = new ArrayList<>(foundInputVariables);
        // Sort the list so that x1 comes before x2, etc. This is crucial for correct assignment.
        inputVariables.sort((v1, v2) -> Integer.compare(v1.getSerialNumber(), v2.getSerialNumber()));

        // Finally, create the Program object with the correctly identified input variables and the full instruction list.
        return new StandardProgram(sProgram.getName(), inputVariables, instructions);
        // --- DEFINITIVE FIX ENDS HERE ---
    }

    private static Instruction SInstructionToInstruction(SInstruction sInstruction) {
        Variable instructionVariable = SVariableToVariable(sInstruction.getSVariable());
        Label instructionLabel = SLabelToLabel(sInstruction.getSLabel());
        SInstructionArguments sInstructionArguments = sInstruction.getSInstructionArguments();
        List<SInstructionArgument> argumentsList = new ArrayList<>();
        if (sInstructionArguments != null)
        {
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
        if (sVariable == null || sVariable.isEmpty())
        {
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

        if (sLabel.equals("EXIT"))
        {
            return FixedLabel.EXIT;
        }

        return new StandardLabel(Integer.parseInt(sLabel.substring(1)));
    }
}