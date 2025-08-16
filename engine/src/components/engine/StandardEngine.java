package components.engine;

import components.instruction.Instruction;
import components.label.Label;
import components.program.Program;
import components.variable.Variable;

import java.util.List;

public class StandardEngine implements Engine {
    private Program program;

    public StandardEngine(Program program) {
        this.program = program;
    }

    @Override
    public String displayProgram() {
        String newLine = System.lineSeparator();
        StringBuilder stream = new StringBuilder();
        List<Instruction> instructions = program.getInstructions();
        int index = 1;

        stream.append(program.getName()).append(newLine);

        for (Variable variable : program.getInputVariables()) {
            stream.append(variable.getStringVariable()).append(" ");
        }
        stream.append(newLine);

        for (Label label : program.getLabels()) {
            stream.append(label.getStringLabel()).append(" ");
        }
        stream.append(newLine);

        for  (Instruction instruction : instructions) {
            stream.append("#").append(index).append(" ").append(instruction.getStringInstruction()).append(newLine);
            index++;
        }

        return stream.toString();
    }
}
