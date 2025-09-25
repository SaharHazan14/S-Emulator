package components.instruction.implementations.synthetic;

import components.executor.Context;
import components.function.Function;
import components.instruction.AbstractInstruction;
import components.instruction.Instruction;
import components.instruction.InstructionSemantic;
import components.label.FixedLabel;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.StandardVariable;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class JumpEqualFunctionInstruction extends AbstractInstruction {
    private final Label JEFunctionLabel;
    private final String functionName;
    private final List<Variable> functionArguments;

    private int functionCyclesNumber;
    private Function function;

    public JumpEqualFunctionInstruction(Variable variable, Label JEFunctionLabel, String functionName, List<Variable> functionArguments) {
        this(variable, JEFunctionLabel, functionName, functionArguments, FixedLabel.EMPTY);
    }

    public JumpEqualFunctionInstruction(Variable variable, Label JEFunctionLabel, String functionName, List<Variable> functionArguments, Label label) {
        super(InstructionSemantic.JUMP_EQUAL_FUNCTION, variable, label);
        this.JEFunctionLabel = JEFunctionLabel;
        this.functionName = functionName;
        this.functionArguments = functionArguments;
    }

    public Label getJEFunctionLabel() {
        return JEFunctionLabel;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Function getFunction() {
        return function;
    }

    public List<Variable> getFunctionArguments() {
        return functionArguments;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public String getStringInstruction() {
        String variable = this.getVariable().getStringVariable();
        String function = this.function.getUserString();
        StringBuilder functionString = new StringBuilder();

        functionString.append(function).append("(");
        int i;
        String argument;
        for (i = 0; i < functionArguments.size() - 1; i++) {
            argument = functionArguments.get(i).getStringVariable();
            functionString.append(argument).append(",");
        }
        if (i > 0) {
            argument = functionArguments.get(i).getStringVariable();
            functionString.append(argument);
        }
        functionString.append(")");

        return String.format("IF %s = %s GOTO %s", variable, functionString.toString(), JEFunctionLabel.getStringLabel());
    }

    @Override
    public Label execute(Context context) {
        if (function != null) {
            Variable functionResult = new StandardVariable(StandardVariable.VariableType.WORK, 0);
            context.updateVariableValue(functionResult, 0L);

            QuoteProgramInstruction quote = new QuoteProgramInstruction(functionResult, functionName, functionArguments);
            quote.setFunction(function);
            quote.execute(context);
            functionCyclesNumber = quote.getCyclesNumber() - 5;

            long value = context.getVariableValue(functionResult);
            if (value == context.getVariableValue(getVariable())) {
                return JEFunctionLabel;
            }

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
            Variable v = this.getVariable();
            Variable z1 = workVariableGenerator.getNextFreeWorkVariable();
            Label l = this.getLabel();

            QuoteProgramInstruction quoteProgramInstruction = new QuoteProgramInstruction(z1, functionName, functionArguments, l);
            quoteProgramInstruction.setFunction(function);
            instructions.add(quoteProgramInstruction);

            instructions.add(new JumpEqualVariableInstruction(v, JEFunctionLabel, z1));

            for (Instruction instruction : instructions) {
                instruction.setAncientInstruction(this);
            }

            return instructions;
        }
        else {
            throw new NullPointerException("Function is null");
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
