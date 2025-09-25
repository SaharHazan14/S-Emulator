package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.executor.ProgramExecutor;
import components.function.Function;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.instruction.implementations.basic.DecreaseInstruction;
import components.instruction.implementations.basic.IncreaseInstruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.basic.NeutralInstruction;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuoteProgramInstruction extends AbstractInstruction {
    private final String functionName;
    private final List<Variable> functionArguments;

    private Function function;
    private int functionCyclesNumber;

    public QuoteProgramInstruction(Variable variable, String functionName, List<Variable> functionArguments) {
        this(variable, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public QuoteProgramInstruction(Variable variable, String functionName, List<Variable> functionArguments, Label label) {
        super(InstructionSemantic.QUOTE, variable, label);
        this.functionName = functionName;
        this.functionArguments = functionArguments;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Variable> getFunctionArguments() {
        return functionArguments;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();

        StringBuilder arguments = new StringBuilder();
        for (Variable argument : functionArguments) {
            arguments.append(",").append(argument.getStringVariable());
        }

        return String.format("%s <- (%s%s)",  variable, function.getUserString(), arguments);
    }

    @Override
    public Label execute(Context context) {
        if (function != null) {
            ProgramExecutor programExecutor = new ProgramExecutor(function);
            Long[] inputs = new Long[functionArguments.size()];
            for (int i = 0; i < functionArguments.size(); i++) {
                inputs[i] = context.getVariableValue(functionArguments.get(i));
            }

            Long result = programExecutor.run(inputs);
            functionCyclesNumber = programExecutor.getCyclesNumber();
            context.updateVariableValue(this.getVariable(), result);

            return FixedLabel.EMPTY;
        }
        else {
            throw new NullPointerException("Function is null");
        }
    }

    @Override
    public List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator) {
        if (function != null) {
            List<Instruction> instructions = new ArrayList<>();
            Map<Variable, Variable> variablesMap = mapFunctionVarsToProgramVars(workVariableGenerator);
            Map<Label, Label> labelsMap = mapFunctionLabelsToProgramLabels(labelGenerator);
            Label thisInstructionLabel = this.getLabel();

            instructions.add(new NeutralInstruction(Variable.OUTPUT, thisInstructionLabel));

            for (int i = 0; i < functionArguments.size(); i++) {
                Variable z = variablesMap.get(function.getInputVariables().get(i));
                instructions.add(new AssignmentInstruction(z, functionArguments.get(i)));
            }

            for (Instruction functionInstruction : function.getInstructions()) {
                instructions.add(getAlternativeInstruction(functionInstruction, variablesMap, labelsMap));
            }

            instructions.add(new AssignmentInstruction(this.getVariable(),
                    variablesMap.get(Variable.OUTPUT),
                    labelsMap.get(FixedLabel.EXIT)));

            for (Instruction instruction : instructions) {
                instruction.setAncientInstruction(this);
            }

            return instructions;
        }
        else {
            throw new NullPointerException("Function is null");
        }
    }

    private Map<Variable, Variable> mapFunctionVarsToProgramVars(FreeWorkVariableGenerator workVariableGenerator) {
        Map<Variable, Variable> resultMap = new HashMap<>();

        for (Variable functionInputVar : function.getInputVariables()) {
            Variable programFreeWorkVar = workVariableGenerator.getNextFreeWorkVariable();
            resultMap.put(functionInputVar, programFreeWorkVar);
        }

        for (Variable functionWorkVar : function.getWorkVariables()) {
            Variable programFreeWorkVar = workVariableGenerator.getNextFreeWorkVariable();
            resultMap.put(functionWorkVar, programFreeWorkVar);
        }

        resultMap.put(Variable.OUTPUT, workVariableGenerator.getNextFreeWorkVariable());
        resultMap.put(Variable.EMPTY, Variable.EMPTY);

        return resultMap;
    }

    private Map<Label, Label> mapFunctionLabelsToProgramLabels(FreeLabelGenerator labelGenerator) {
        Map<Label, Label> resultMap = new HashMap<>();

        for (Label functionLabel : function.getLabels()) {
            Label programLabel = labelGenerator.getNextFreeLabel();
            resultMap.put(functionLabel, programLabel);
        }

        if (!resultMap.containsKey(FixedLabel.EXIT)) {
            resultMap.put(FixedLabel.EXIT, labelGenerator.getNextFreeLabel());
        }

        resultMap.put(FixedLabel.EMPTY, FixedLabel.EMPTY);

        return resultMap;
    }

    private Instruction getAlternativeInstruction(Instruction originalInstruction, Map<Variable, Variable> variablesMap, Map<Label, Label> labelsMap) {
        Variable alternativeVariable = variablesMap.get(originalInstruction.getVariable());
        Label alternativeLabel = labelsMap.get(originalInstruction.getLabel());

        switch (originalInstruction) {
            case IncreaseInstruction increaseInstruction -> {
                return new IncreaseInstruction(alternativeVariable, alternativeLabel);
            }
            case DecreaseInstruction decreaseInstruction -> {
                return new DecreaseInstruction(alternativeVariable, alternativeLabel);
            }
            case JumpNotZeroInstruction jumpNotZeroInstruction -> {
                return new JumpNotZeroInstruction(alternativeVariable, labelsMap.get(jumpNotZeroInstruction.getJNZLabel()), alternativeLabel);
            }
            case NeutralInstruction neutralInstruction -> {
                return new NeutralInstruction(alternativeVariable, alternativeLabel);
            }
            case ZeroVariableInstruction zeroVariableInstruction -> {
                return new ZeroVariableInstruction(alternativeVariable, alternativeLabel);
            }
            case GotoLabelInstruction gotoLabelInstruction -> {
                return new GotoLabelInstruction(labelsMap.get(gotoLabelInstruction.getGotoLabel()), alternativeLabel);
            }
            case AssignmentInstruction assignmentInstruction -> {
                return new AssignmentInstruction(alternativeVariable, variablesMap.get(assignmentInstruction.getAssignedVariable()), alternativeLabel);
            }
            case ConstantAssignmentInstruction constantAssignmentInstruction -> {
                return new ConstantAssignmentInstruction(alternativeVariable, constantAssignmentInstruction.getConstantValue(), alternativeLabel);
            }
            case JumpZeroInstruction jumpZeroInstruction -> {
                return new JumpZeroInstruction(alternativeVariable, labelsMap.get(jumpZeroInstruction.getJZLabel()), alternativeLabel);
            }
            case JumpEqualConstantInstruction jumpEqualConstantInstruction -> {
                return new JumpEqualConstantInstruction(alternativeVariable, labelsMap.get(jumpEqualConstantInstruction.getJEConstantLabel()),
                        jumpEqualConstantInstruction.getConstantValue(), alternativeLabel);
            }
            case JumpEqualVariableInstruction jumpEqualVariableInstruction -> {
                return new JumpEqualVariableInstruction(alternativeVariable, labelsMap.get(jumpEqualVariableInstruction.getJEVariableLabel()),
                        variablesMap.get(jumpEqualVariableInstruction.getVariableName()), alternativeLabel);
            }
            case QuoteProgramInstruction quoteProgramInstruction -> {
                QuoteProgramInstruction result = new QuoteProgramInstruction(alternativeVariable, quoteProgramInstruction.getFunctionName(),
                        quoteProgramInstruction.getFunctionArguments(), alternativeLabel);
                result.setFunction(quoteProgramInstruction.getFunction());
                return result;
            }
            case JumpEqualFunctionInstruction jumpEqualFunctionInstruction -> {
                JumpEqualFunctionInstruction result = new JumpEqualFunctionInstruction(alternativeVariable, labelsMap.get(jumpEqualFunctionInstruction.getJEFunctionLabel()),
                        jumpEqualFunctionInstruction.getFunctionName(), jumpEqualFunctionInstruction.getFunctionArguments(), alternativeLabel);
                result.setFunction(jumpEqualFunctionInstruction.getFunction());
                return result;
            }
            default -> {
            return null;
            }
        }
    }

    @Override
    public int getCyclesNumber() {
        return super.getCyclesNumber() + functionCyclesNumber;
    }

    @Override
    public int getDegree() {
        return super.getDegree() + function.calculateMaxDegree();
    }
}
