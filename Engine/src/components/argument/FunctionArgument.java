package components.argument;

import components.executor.Context;
import components.executor.ProgramExecutor;
import components.function.Function;
import components.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class FunctionArgument implements Argument{
    private final String functionName;
    private final List<Argument> arguments;

    private Function function;

    public FunctionArgument(String functionName, List<Argument> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public Long evaluate(Context context) {
        ProgramExecutor programExecutor = new ProgramExecutor(function);
        Long[] inputs = new Long[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            inputs[i] = arguments.get(i).evaluate(context);
        }

        return programExecutor.run(inputs);
    }

    @Override
    public String getStringArgument() {
        StringBuilder argumentsString = new StringBuilder();
        for (Argument argument : this.arguments) {
            argumentsString.append(",").append(argument.getStringArgument());
        }

        return String.format("(%s%s)", function.getUserString(), argumentsString);
    }

    public List<Variable> getVariable() {
        List<Variable> variables = new ArrayList<>();

        for (Argument argument : this.arguments) {
            if (argument instanceof Variable) {
                variables.add((Variable) argument);
            }
            else {
                FunctionArgument functionArgument = (FunctionArgument) argument;
                variables.addAll(functionArgument.getVariable());
            }
        }

        return variables;
    }

    public int calculateFunctionMaxDegree() {
        if (isPureVariables(arguments)) {
            return function.calculateMaxDegree() + 1;
        }

        int maxDegree = 0;
        for (Argument argument : arguments) {
            if (argument instanceof FunctionArgument functionArgument) {
                maxDegree = Math.max(maxDegree, functionArgument.calculateFunctionMaxDegree());
            }
        }

        return maxDegree + 1;
    }

    private boolean isPureVariables(List<Argument> arguments) {
        for (Argument argument : arguments) {
            if (!(argument instanceof Variable)) {
                return false;
            }
        }

        return true;
    }
}
