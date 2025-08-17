package components.engine;

import components.program.Program;
import dtos.ProgramDetails;

public class StandardEngine implements Engine {
    private Program program;

    public StandardEngine(Program program) {
        this.program = program;
    }

    @Override
    public ProgramDetails getProgramDetails() {
        return new ProgramDetails(program.getName(), program.getInputVariables(),
                program.getLabels(), program.getInstructions());
    }
}
