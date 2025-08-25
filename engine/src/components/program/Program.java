package components.program;

import components.instruction.Instruction;
import components.label.Label;
import components.variable.Variable;

import java.util.List;
import java.util.Optional; // Add this import

public interface Program {
    String getName();
    List<Variable> getInputVariables();
    List<Label> getLabels();
    List<Instruction> getInstructions();
    Program expand(int degree);

    void addInstruction(Instruction instruction);
    boolean validate();
    int calculateMaxDegree();
    int calculateCyclesNumber();

    // Add this method signature
    Optional<Variable> getVariableByName(String name);
}