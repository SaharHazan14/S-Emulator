package components.program;

import components.instruction.Instruction;
import components.label.Label;
import components.variable.Variable;

import java.util.List;

public interface Program {
    String getName();
    List<Variable> getInputVariables();
    List<Label> getLabels();
    List<Instruction> getInstructions();

    void addInstruction(Instruction instruction);
    boolean validate();
    int calculateMaxDegree();
    int calculateCyclesNumber();

    Program expand(int degree);
}
