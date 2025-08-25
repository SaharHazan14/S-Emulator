package components.executor;

import components.instruction.Instruction;
import components.instruction.implementations.basic.JumpNotZeroInstruction;
import components.instruction.implementations.synthetic.AssignmentInstruction;
import components.label.Label;
import components.program.Program;
import components.variable.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramExecutor implements Executor {

    private Map<Variable, Long> variables;
    private long cyclesConsumed;

    public ProgramExecutor() {
        this.variables = new HashMap<>();
        this.cyclesConsumed = 0;
    }

    @Override
    public Long run(Program program, List<Long> inputs) {
        this.variables.clear();
        this.cyclesConsumed = 0;
        initializeAllVariables(program, inputs);

        Map<String, Integer> labelMap = buildLabelMap(program.getInstructions());

        int programCounter = 0;
        List<Instruction> instructions = program.getInstructions();
        int instructionCount = instructions.size();

        while (programCounter >= 0 && programCounter < instructionCount) {
            Instruction currentInstruction = instructions.get(programCounter);

            this.cyclesConsumed += currentInstruction.getCyclesNumber();
            int nextProgramCounter = programCounter + 1;

            if (currentInstruction instanceof AssignmentInstruction) {
                handleAssignment((AssignmentInstruction) currentInstruction);
            } else if (currentInstruction instanceof JumpNotZeroInstruction) {
                int jumpIndex = handleJump((JumpNotZeroInstruction) currentInstruction, labelMap);
                if (jumpIndex < -1) { // -2 signals EXIT
                    nextProgramCounter = -1;
                } else if (jumpIndex != -1) { // A valid jump occurred
                    nextProgramCounter = jumpIndex;
                }
            } else {
                switch (currentInstruction.getInstructionSemantic()) {
                    case INCREASE:
                        handleIncrease(currentInstruction);
                        break;
                    case DECREASE:
                        handleDecrease(currentInstruction);
                        break;
                    case NEUTRAL:
                        break;
                    default:
                        break;
                }
            }

            if (nextProgramCounter >= instructionCount || nextProgramCounter < 0) {
                programCounter = -1;
            } else {
                programCounter = nextProgramCounter;
            }
        }
        return findVariableValue(Variable.OUTPUT);
    }

    private void initializeAllVariables(Program program, List<Long> inputs) {
        List<Variable> inputVars = program.getInputVariables();
        for (int i = 0; i < inputVars.size(); i++) {
            Variable var = inputVars.get(i);
            long value = (i < inputs.size()) ? inputs.get(i) : 0L;
            this.variables.put(var, value);
        }

        for (Instruction instruction : program.getInstructions()) {
            Variable var = instruction.getVariable();
            if (var != null && var != Variable.EMPTY) {
                this.variables.putIfAbsent(var, 0L);
            }
            if (instruction instanceof AssignmentInstruction) {
                Variable sourceVar = ((AssignmentInstruction) instruction).getAssignedVariable();
                if (sourceVar != null && sourceVar != Variable.EMPTY) {
                    this.variables.putIfAbsent(sourceVar, 0L);
                }
            }
        }
        this.variables.putIfAbsent(Variable.OUTPUT, 0L);
    }

    private Map<String, Integer> buildLabelMap(List<Instruction> instructions) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getLabel() != null && !instruction.getLabel().getStringLabel().isEmpty()) {
                map.putIfAbsent(instruction.getLabel().getStringLabel(), i);
            }
        }
        return map;
    }

    private void handleIncrease(Instruction instruction) {
        Variable var = instruction.getVariable();
        this.variables.merge(var, 1L, Long::sum);
    }

    private void handleDecrease(Instruction instruction) {
        Variable var = instruction.getVariable();
        long currentValue = this.variables.getOrDefault(var, 0L);
        if (currentValue > 0) {
            this.variables.put(var, currentValue - 1);
        }
    }

    private int handleJump(JumpNotZeroInstruction instruction, Map<String, Integer> labelMap) {
        Variable var = instruction.getVariable();
        long value = this.variables.getOrDefault(var, 0L);

        if (value != 0) {
            Label targetLabel = instruction.getTargetLabel();
            if (targetLabel != null) {
                String targetLabelName = targetLabel.getStringLabel();
                if ("EXIT".equalsIgnoreCase(targetLabelName)) {
                    return -2;
                }
                return labelMap.getOrDefault(targetLabelName, -1);
            }
        }
        return -1;
    }

    private void handleAssignment(AssignmentInstruction instruction) {
        Variable destVar = instruction.getVariable();
        Variable sourceVar = instruction.getAssignedVariable();

        if (destVar != null && sourceVar != null) {
            long sourceValue = this.variables.getOrDefault(sourceVar, 0L);
            this.variables.put(destVar, sourceValue);
        }
    }

    private Long findVariableValue(Variable varToFind) {
        return this.variables.getOrDefault(varToFind, 0L);
    }

    @Override
    public Map<Variable, Long> getVariablesStates() {
        return this.variables;
    }

    @Override
    public long getCyclesConsumed() {
        return this.cyclesConsumed;
    }
}