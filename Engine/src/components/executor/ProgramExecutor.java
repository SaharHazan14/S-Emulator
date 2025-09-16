package components.executor;

import components.instruction.Instruction;
import components.label.FixedLabel;
import components.label.Label;
import components.program.Program;
import components.variable.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramExecutor implements Executor {
    private final Program program;
    private Context context;
    private int cyclesNumber;

    public ProgramExecutor(Program program) {
        this.program = program;
    }

    @Override
    public Long run(Long... input) {
        context = new StandardContext();
        initializeInputVariables(context, input);
        initializeWorkVariables();

        Map<Label, Integer> labelToIndex = new HashMap<>();
        List<Instruction> instructions = program.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            Label label = instructions.get(i).getLabel();
            if (label != null && label != FixedLabel.EMPTY) {
                labelToIndex.put(label, i);
            }
        }

        int instructionIndex = 0;
        Label nextInstructionLabel = FixedLabel.EMPTY;

        while (nextInstructionLabel != FixedLabel.EXIT && instructionIndex < instructions.size()) {
            Instruction currentInstruction = instructions.get(instructionIndex);
            nextInstructionLabel = currentInstruction.execute(context);
            cyclesNumber += currentInstruction.getCyclesNumber();

            if (nextInstructionLabel == FixedLabel.EMPTY) {
                instructionIndex++;
            } else if (nextInstructionLabel != FixedLabel.EXIT) {
                instructionIndex = labelToIndex.getOrDefault(nextInstructionLabel, instructions.size());
            }
        }

        return context.getVariableValue(Variable.OUTPUT);
    }

    @Override
    public Context getVariablesContext() {
        return context;
    }

    private void initializeInputVariables(Context context, Long... input) {
        List<Variable> inputVariables = program.getInputVariables();
        int i = 0;

        while (i < inputVariables.size() && i < input.length) {
            context.updateVariableValue(inputVariables.get(i), input[i]);
            i++;
        }

        while (i < inputVariables.size()) {
            context.updateVariableValue(inputVariables.get(i), 0L);
            i++;
        }
    }

    private void initializeWorkVariables() {
        for (Variable var : program.getWorkVariables()) {
            context.updateVariableValue(var, 0L);
        }
    }

    public int getCyclesNumber() {
        return cyclesNumber;
    }
}
