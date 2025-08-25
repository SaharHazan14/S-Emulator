package components.executor;

import components.instruction.Instruction;
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
        // Step 1: Initialize state for the new run.
        this.variables.clear();
        this.cyclesConsumed = 0;
        initializeAllVariables(program, inputs);

        // Step 2: Build a map of labels for quick jumps.
        Map<String, Integer> labelMap = buildLabelMap(program.getInstructions());

        // Step 3: Main execution loop.
        int programCounter = 0;
        List<Instruction> instructions = program.getInstructions();
        int instructionCount = instructions.size();

        while (programCounter >= 0 && programCounter < instructionCount) {
            Instruction currentInstruction = instructions.get(programCounter);

            this.cyclesConsumed += currentInstruction.getCyclesNumber();
            int nextProgramCounter = programCounter + 1;

            // Differentiate between basic instructions and the special Assignment instruction.
            if (currentInstruction instanceof AssignmentInstruction) {
                handleAssignment((AssignmentInstruction) currentInstruction);
            } else {
                // Handle all basic, non-synthetic instructions
                switch (currentInstruction.getInstructionSemantic()) {
                    case INCREASE:
                        handleIncrease(currentInstruction);
                        break;
                    case DECREASE:
                        handleDecrease(currentInstruction);
                        break;
                    case JUMP_NOT_ZERO:
                        int jumpIndex = handleJump(currentInstruction, labelMap);
                        if (jumpIndex < -1) { // -2 is EXIT
                            nextProgramCounter = -1;
                        } else if (jumpIndex != -1) { // A valid jump
                            nextProgramCounter = jumpIndex;
                        }
                        break;
                    case NEUTRAL:
                        // NO-OP, do nothing.
                        break;
                    // Default case for any other semantics that might not be handled
                    default:
                        break;
                }
            }

            // Check for loop termination
            if (nextProgramCounter >= instructionCount || nextProgramCounter < 0) {
                programCounter = -1; // Exit the loop
            } else {
                programCounter = nextProgramCounter;
            }
        }

        // Step 4: Return the final value of the output variable 'y'.
        return findVariableValue(Variable.OUTPUT);
    }

    /**
     * Initializes all variables to their starting values.
     */
    private void initializeAllVariables(Program program, List<Long> inputs) {
        // Initialize provided input variables (x1, x2, ...).
        List<Variable> inputVars = program.getInputVariables();
        for (int i = 0; i < inputVars.size(); i++) {
            Variable var = inputVars.get(i);
            long value = (i < inputs.size()) ? inputs.get(i) : 0L;
            this.variables.put(var, value);
        }

        // Ensure all other variables used in the program are initialized to 0.
        for (Instruction instruction : program.getInstructions()) {
            Variable var = instruction.getVariable();
            if (var != null && var != Variable.EMPTY) {
                this.variables.putIfAbsent(var, 0L);
            }
            // Special check for AssignmentInstruction's source variable
            if (instruction instanceof AssignmentInstruction) {
                Variable sourceVar = ((AssignmentInstruction) instruction).getAssignedVariable();
                if (sourceVar != null && sourceVar != Variable.EMPTY) {
                    this.variables.putIfAbsent(sourceVar, 0L);
                }
            }
        }
        // Specifically ensure the output variable 'y' is initialized.
        this.variables.putIfAbsent(Variable.OUTPUT, 0L);
    }

    /**
     * Creates a map of label names to their instruction index.
     */
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

    /**
     * Handles a JUMP_NOT_ZERO instruction.
     * Returns the index to jump to, -2 for EXIT, or -1 for no jump.
     */
    private int handleJump(Instruction instruction, Map<String, Integer> labelMap) {
        Variable var = instruction.getVariable();
        long value = this.variables.getOrDefault(var, 0L);

        if (value != 0) {
            String instructionStr = instruction.getStringInstruction();
            String[] parts = instructionStr.split("GOTO ");
            if (parts.length > 1) {
                String targetLabelName = parts[1].trim();
                if ("EXIT".equalsIgnoreCase(targetLabelName)) {
                    return -2; // Signal program termination
                }
                return labelMap.getOrDefault(targetLabelName, -1);
            }
        }
        return -1; // No jump
    }

    /**
     * Handles a synthetic assignment instruction like 'y <- x1' by accessing its fields correctly.
     */
    private void handleAssignment(AssignmentInstruction instruction) {
        // The destination variable (e.g., 'y') is stored in the parent class's 'variable' field.
        Variable destVar = instruction.getVariable();
        // The source variable (e.g., 'x1') is stored in the 'assignedVariable' field.
        Variable sourceVar = instruction.getAssignedVariable();

        if (destVar != null && sourceVar != null) {
            long sourceValue = this.variables.getOrDefault(sourceVar, 0L);
            this.variables.put(destVar, sourceValue);
        }
    }

    /**
     * Helper to get a variable's value from the state map.
     */
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