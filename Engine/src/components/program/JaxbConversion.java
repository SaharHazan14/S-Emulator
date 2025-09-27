package components.program;

import components.argument.Argument;
import components.argument.FunctionArgument;
import components.executor.Context;
import components.function.Function;
import components.function.FunctionFactory;
import components.instruction.Instruction;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.instruction.implementations.synthetic.*;
import components.jaxb.generated.*;
import components.label.FixedLabel;
import components.label.Label;
import components.label.StandardLabel;
import components.variable.StandardVariable;
import components.variable.Variable;

import java.util.*;

public class JaxbConversion {

    public static Program SProgramToProgram(SProgram sProgram) {
        Program program = new StandardProgram(sProgram.getName());
        FunctionFactory functionFactory = new FunctionFactory();

        if (sProgram.getSFunctions() != null) {
            for (SFunction sFunction : sProgram.getSFunctions().getSFunction()) {
                Function function = SFunctionToFunction(sFunction);
                functionFactory.addFunction(function);
            }
        }

        for (SInstruction sInstruction : sProgram.getSInstructions().getSInstruction()) {
            program.addInstruction(SInstructionToInstruction(sInstruction));
        }

        initializeFunctions(functionFactory, program);

        for (Function function : functionFactory.getFunctions()) {
            program.addFunction(function);
        }

        initializeArguments(functionFactory, program);

        return program;
    }

    private static void initializeFunctions(FunctionFactory functionFactory, Program program) {
        // may add check for null pointer
        for (Function function : functionFactory.getFunctions()) {
            for (Instruction instruction : function.getInstructions()) {
                if (instruction instanceof QuoteProgramInstruction quoteProgramInstruction) {
                    Function quotedFunction = functionFactory.getFunction(quoteProgramInstruction.getFunctionName());
                    if (quotedFunction != null) {
                        quoteProgramInstruction.setFunction(quotedFunction);
                    }
                    else {
                        throw new RuntimeException("Function " + quoteProgramInstruction.getFunctionName() + " not found.");
                    }
                }
                else if (instruction instanceof JumpEqualFunctionInstruction jumpEqualFunctionInstruction) {
                    Function quotedFunction = functionFactory.getFunction(jumpEqualFunctionInstruction.getFunctionName());
                    if (quotedFunction != null) {
                        jumpEqualFunctionInstruction.setFunction(quotedFunction);
                    }
                    else {
                        throw new RuntimeException("Function " + jumpEqualFunctionInstruction.getFunctionName() + " not found.");
                    }
                }
            }
        }

        for (Instruction instruction : program.getInstructions()) {
            if (instruction instanceof QuoteProgramInstruction quoteProgramInstruction) {
                Function quotedFunction = functionFactory.getFunction(quoteProgramInstruction.getFunctionName());
                if (quotedFunction != null) {
                    quoteProgramInstruction.setFunction(quotedFunction);
                }
                else {
                    throw new RuntimeException("Function " + quoteProgramInstruction.getFunctionName() + " not found.");
                }
            }
            else if (instruction instanceof JumpEqualFunctionInstruction jumpEqualFunctionInstruction) {
                Function quotedFunction = functionFactory.getFunction(jumpEqualFunctionInstruction.getFunctionName());
                if (quotedFunction != null) {
                    jumpEqualFunctionInstruction.setFunction(quotedFunction);
                }
                else {
                    throw new RuntimeException("Function " + jumpEqualFunctionInstruction.getFunctionName() + " not found.");
                }
            }
        }
    }

    private static void initializeArguments(FunctionFactory functionFactory, Program program) {
        for (Instruction instruction : program.getInstructions()) {
            if (instruction instanceof QuoteProgramInstruction quoteProgramInstruction) {
                List<Argument> arguments = quoteProgramInstruction.getFunctionArguments();
                for (Argument argument : arguments) {
                    setArgumentFunction(functionFactory, argument);
                }
            }
            else if (instruction instanceof JumpEqualFunctionInstruction jumpEqualFunctionInstruction) {
                List<Argument> arguments = jumpEqualFunctionInstruction.getFunctionArguments();
                for (Argument argument : arguments) {
                    setArgumentFunction(functionFactory, argument);
                }
            }
        }

        for (Function function : functionFactory.getFunctions()) {
            for (Instruction instruction : function.getInstructions()) {
                if (instruction instanceof QuoteProgramInstruction quoteProgramInstruction) {
                    List<Argument> arguments = quoteProgramInstruction.getFunctionArguments();
                    for (Argument argument : arguments) {
                        setArgumentFunction(functionFactory, argument);
                    }
                }
                else if (instruction instanceof JumpEqualFunctionInstruction jumpEqualFunctionInstruction) {
                    List<Argument> arguments = jumpEqualFunctionInstruction.getFunctionArguments();
                    for (Argument argument : arguments) {
                        setArgumentFunction(functionFactory, argument);
                    }
                }
            }
        }
    }

    private static void setArgumentFunction(FunctionFactory functionFactory, Argument argument) {
        if (argument instanceof FunctionArgument functionArgument) {
            Function function = functionFactory.getFunction(functionArgument.getFunctionName());
            if (function != null) {
                functionArgument.setFunction(function);
            }

            for (Argument arg : functionArgument.getArguments()) {
                setArgumentFunction(functionFactory, arg);
            }
        }
    }

    private static Function SFunctionToFunction(SFunction sFunction) {
        Function function = new Function(sFunction.getName(), sFunction.getUserString());
        for (SInstruction sInstruction : sFunction.getSInstructions().getSInstruction()) {
            function.addInstruction(SInstructionToInstruction(sInstruction));
        }

        return function;
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
            case "INCREASE" -> {
                return new IncreaseInstruction(instructionVariable, instructionLabel);
            }
            case "DECREASE" -> {
                return new DecreaseInstruction(instructionVariable, instructionLabel);
            }
            case "JUMP_NOT_ZERO" -> {
                Label JNZLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                return new JumpNotZeroInstruction(instructionVariable, JNZLabel, instructionLabel);
            }
            case "NEUTRAL" -> {
                return new NeutralInstruction(instructionVariable, instructionLabel);
            }
            case "ZERO_VARIABLE" -> {
                return new ZeroVariableInstruction(instructionVariable, instructionLabel);
            }
            case "GOTO_LABEL" -> {
                Label gotoLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                return new GotoLabelInstruction(gotoLabel, instructionLabel);
            }
            case "ASSIGNMENT" -> {
                Variable assignmentVariable = SVariableToVariable(argumentsList.getFirst().getValue());
                return new AssignmentInstruction(instructionVariable, assignmentVariable, instructionLabel);
            }
            case "CONSTANT_ASSIGNMENT" -> {
                int constantValue = Integer.parseInt(argumentsList.getFirst().getValue());
                return new ConstantAssignmentInstruction(instructionVariable, constantValue, instructionLabel);
            }
            case "JUMP_ZERO" -> {
                Label JZLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                return new JumpZeroInstruction(instructionVariable, JZLabel, instructionLabel);
            }
            case "JUMP_EQUAL_CONSTANT" -> {
                Label JEConstantLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                int constantValue = Integer.parseInt(argumentsList.get(1).getValue());
                return new JumpEqualConstantInstruction(instructionVariable, JEConstantLabel, constantValue, instructionLabel);
            }
            case "JUMP_EQUAL_VARIABLE" -> {
                Label JEVariableLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                Variable variableName = SVariableToVariable(argumentsList.get(1).getValue());
                return new JumpEqualVariableInstruction(instructionVariable, JEVariableLabel, variableName, instructionLabel);
            }
            case "QUOTE" -> {
                String functionName = argumentsList.getFirst().getValue();
                List<String> functionStringArguments = splitArguments(argumentsList.get(1).getValue());
                List<Argument> functionArguments = new ArrayList<>();
                for (String stringArgument : functionStringArguments) {
                    functionArguments.add(createArgument(stringArgument));
                }
                return new QuoteProgramInstruction(instructionVariable, functionName, functionArguments, instructionLabel);
            }
            case "JUMP_EQUAL_FUNCTION" -> {
                Label JEFunctionLabel = SLabelToLabel(argumentsList.getFirst().getValue());
                String functionName = argumentsList.get(1).getValue();
                List<String> functionStringArguments = splitArguments(argumentsList.get(2).getValue());
                List<Argument> functionArguments = new ArrayList<>();
                for (String stringArgument : functionStringArguments) {
                    functionArguments.add(createArgument(stringArgument));
                }
                return new JumpEqualFunctionInstruction(instructionVariable, JEFunctionLabel, functionName, functionArguments, instructionLabel);
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

    private static List<Variable> parseStringVariables(String stringVariables) {
        List<Variable> variables = new ArrayList<>();
        if (stringVariables.isEmpty()) {
            return variables;
        }

        String[] variablesArray = stringVariables.split(",");
        for (String variable : variablesArray) {
            variables.add(SVariableToVariable(variable));
        }

        return variables;
    }

    public static Argument createArgument(String stringArgument) {
        if (!stringArgument.isEmpty() && stringArgument.charAt(0) == '(' && stringArgument.charAt(stringArgument.length() - 1) == ')') {
            List<String> argumentsList = splitArguments(stringArgument.substring(1, stringArgument.length() - 1));
            List<Argument> arguments = new ArrayList<>();
            for (int i = 1; i < argumentsList.size(); i++) {
                arguments.add(createArgument(argumentsList.get(i)));
            }

            return new FunctionArgument(argumentsList.getFirst(), arguments);
        }

        return SVariableToVariable(stringArgument);
    }

    private static List<String> splitArguments(String stringArgument) {
        List<String> arguments = new ArrayList<>();
        StringBuilder currentArgument = new StringBuilder();
        int parenthesisCounter = 0;
        for (int i = 0; i < stringArgument.length(); i++) {
            if (stringArgument.charAt(i) == '(') {
                parenthesisCounter++;
            }
            else if (stringArgument.charAt(i) == ')') {
                parenthesisCounter--;
            }

            if (stringArgument.charAt(i) == ',' && parenthesisCounter == 0) {
                arguments.add(currentArgument.toString());
                currentArgument = new StringBuilder();
            }
            else {
                currentArgument.append(stringArgument.charAt(i));
            }
        }
        arguments.add(currentArgument.toString());

        return arguments;
    }
}


