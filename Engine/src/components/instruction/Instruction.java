package components.instruction;

import components.executor.Context;
import components.label.FreeLabelGenerator;
import components.label.Label;
import components.variable.FreeWorkVariableGenerator;
import components.variable.Variable;
import dtos.InstructionDetails;

import java.io.Serializable;
import java.util.List;

public interface Instruction extends Serializable {
    String getName();
    Label execute(Context context);
    int getCyclesNumber();
    int getDegree();
    Label getLabel();
    List<Label> getAllInvolvedLabels();
    // List<Instruction> getInstructions();
    Variable getVariable();
    List<Variable> getAllInvolvedVariables();
    String getStringInstruction();
    List<Instruction> expand(FreeLabelGenerator labelGenerator, FreeWorkVariableGenerator workVariableGenerator);
    int getInstructionNumber();
    void setInstructionNumber(int instructionNumber);
    boolean hasAncientInstruction();
    Instruction getAncientInstruction();
    void setAncientInstruction(Instruction ancientInstruction);
    InstructionSemantic.InstructionType getInstructionType();
    InstructionDetails getInstructionDetails();
}
