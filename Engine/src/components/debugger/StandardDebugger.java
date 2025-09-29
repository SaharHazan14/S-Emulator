package components.debugger;

import components.executor.Context;
import components.executor.StandardContext;
import components.instruction.Instruction;
import components.label.FixedLabel;
import components.label.Label;
import components.program.Program;
import components.variable.Variable;
import dtos.DebugDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDebugger implements Debugger {
    private Program program;
    private Context context;
    private int cyclesNumber;
    private int lineIndex;
    private Map<Label, Integer> labelToIndex;
    private List<DebugDetails> steps;
    private int stepPointer;

    public StandardDebugger(Program program) {
        this.program = program;
        steps = new ArrayList<>();
    }

    @Override
    public DebugDetails initializeDebugger(Long... inputs) {
        context = new StandardContext();

        // Initialize context
        context.updateVariableValue(Variable.OUTPUT, 0L);

        List<Variable> inputVariables = program.getInputVariables();
        int i = 0;

        while (i < inputVariables.size() && i < inputs.length) {
            context.updateVariableValue(inputVariables.get(i), inputs[i]);
            i++;
        }

        while (i < inputVariables.size()) {
            context.updateVariableValue(inputVariables.get(i), 0L);
            i++;
        }

        for (Variable var : program.getWorkVariables()) {
            context.updateVariableValue(var, 0L);
        }

        // Initialize labels map
        labelToIndex = new HashMap<>();
        List<Instruction> instructions = program.getInstructions();
        for (int j = 0; j < instructions.size(); j++) {
            Label label = instructions.get(j).getLabel();
            if (label != null && label != FixedLabel.EMPTY) {
                labelToIndex.put(label, j);
            }
        }

        DebugDetails debugDetails = new DebugDetails(context.getContextDetails(),  cyclesNumber, lineIndex, false, false, null);
        steps.add(debugDetails);

        return debugDetails;
    }

    @Override
    public DebugDetails stepForward() {
        DebugDetails debugDetails;

        if (stepPointer >= steps.size() - 1) {
            boolean programEnded = false;
            Instruction currentInstruction = program.getInstructions().get(lineIndex);
            Label nextLabel = currentInstruction.execute(context);
            cyclesNumber += currentInstruction.getCyclesNumber();
            if (nextLabel == FixedLabel.EXIT) {
                lineIndex = program.getInstructions().size();
            } else if (nextLabel == FixedLabel.EMPTY) {
                lineIndex++;
            } else {
                lineIndex = labelToIndex.get(nextLabel);
            }

            if (lineIndex >= program.getInstructions().size()) {
                programEnded = true;
            }

            debugDetails = new DebugDetails(context.getContextDetails(), cyclesNumber, lineIndex, programEnded, true, currentInstruction.getVariable().getVariableDetails());
            steps.add(debugDetails);
        }
        else {
            debugDetails = steps.get(stepPointer + 1);
        }

        stepPointer++;

        return debugDetails;
    }

    @Override
    public DebugDetails stepBackward() {
        return steps.get(--stepPointer);
    }

    @Override
    public DebugDetails resume() {
        DebugDetails debugDetails;
        do {
            debugDetails = stepForward();
        } while (!debugDetails.programEnded());

        return debugDetails;
    }
}
