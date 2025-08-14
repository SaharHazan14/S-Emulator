package components.program;

import components.instruction.Instruction;

import java.util.List;

public interface Program {
    String getName();
    void addInstruction(Instruction instruction);
    List<Instruction> getInstructions();
    boolean validate();
    int calculateMaxDegree();
    int calculateCyclesNumber();
}
