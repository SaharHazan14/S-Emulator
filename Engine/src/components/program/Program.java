package components.program;

import components.function.Function;
import components.instruction.Instruction;
import components.label.Label;
import components.variable.Variable;
import dtos.ProgramDetails;

import java.io.Serializable;
import java.util.List;

public interface Program extends Serializable {
    String getName();
    List<Variable> getInputVariables();
    List<Variable> getWorkVariables();
    List<Label> getLabels();
    List<Instruction> getInstructions();

    void addInstruction(Instruction instruction);
    void addFunction(Function function);
    int calculateMaxDegree();
    int getNextFreeLabelNumber();
    int getNextFreeWorkVariableNumber();
    Program expand();
    //public ProgramDetails getProgramDetails();

    int calculateBasicInstructionsNumber();
    ProgramDetails getProgramDetails();
    public List<Function> getFunctions();
}
