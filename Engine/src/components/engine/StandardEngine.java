package components.engine;

import components.executor.ProgramExecutor;
import components.jaxb.generated.SInstruction;
import components.jaxb.generated.SInstructionArgument;
import components.jaxb.generated.SProgram;
import components.program.JaxbConversion;
import components.program.Program;
import dtos.ExecutionDetails;
import dtos.ProgramDetails;
import dtos.RunHistoryDetails;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StandardEngine implements Engine {
    final static String JAXB_XML_PACKAGE_NAME = "components.jaxb.generated";

    private Program program;
    private boolean programLoaded = false;
    private int runNumber;
    List<RunHistoryDetails> runHistoryDetails = new ArrayList<>();

    // 1. Load File
    @Override
    public void loadProgramFromFile(File file) {
        SProgram sProgram = parseXmlFile(file);
        try {
            jumpLabelsAreValid(sProgram);
            program = JaxbConversion.SProgramToProgram(sProgram);
            programLoaded = true;

            runNumber = 0;
            runHistoryDetails = new ArrayList<>();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isProgramLoaded() {
        return programLoaded;
    }

    private SProgram parseXmlFile(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (SProgram) unmarshaller.unmarshal(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void jumpLabelsAreValid(SProgram program) {
        Set<String> instructionLabels = new HashSet<>();
        Set<String> jumpLabels = new HashSet<>();

        for (SInstruction instruction : program.getSInstructions().getSInstruction())
        {
            if (instruction.getSLabel() != null)
            {
                instructionLabels.add(instruction.getSLabel());
            }

            if (instruction.getName().contains("JUMP") || instruction.getName().contains("GOTO"))
            {
                for (SInstructionArgument argument : instruction.getSInstructionArguments().getSInstructionArgument())
                {
                    if (argument.getName().contains("Label"))
                    {
                        jumpLabels.add(argument.getValue());
                    }
                }
            }
        }

        for (String jumpLabel : jumpLabels)
        {
            if (!instructionLabels.contains(jumpLabel) && !jumpLabel.equals("EXIT"))
            {
                throw new RuntimeException("Can't jump to label " + jumpLabel);
            }
        }
    }

    // 2. Show Program
    @Override
    public ProgramDetails getProgramDetails() {
        return new ProgramDetails(program.getName(), program.getInputVariables(), program.getWorkVariables(), program.getLabels(), program.getInstructions(), program.calculateMaxDegree(), program.calculateBasicInstructionsNumber());
    }

    // 3. Expand Program
    @Override
    public ProgramDetails expandProgram(int expansionDegree) {
        Program expandedProgram = program;

        for (int i = 0; i < expansionDegree; i++) {
            expandedProgram = expandedProgram.expand();
        }

        return new ProgramDetails(expandedProgram.getName(), expandedProgram.getInputVariables(), expandedProgram.getWorkVariables(), expandedProgram.getLabels(), expandedProgram.getInstructions(), expandedProgram.calculateMaxDegree(), expandedProgram.calculateBasicInstructionsNumber());
    }

    @Override
    public int getProgramMaxDegree() {
        return program.calculateMaxDegree();
    }

    // 4. Run Program
    @Override
    public ExecutionDetails runProgram(int expansionDegree, Long... input) {
        Program runningProgram = program;

        for (int i = 0; i < expansionDegree; i++) {
            runningProgram = runningProgram.expand();
        }

        ProgramExecutor programExecutor = new ProgramExecutor(runningProgram);
        Long y = programExecutor.run(input);

        runHistoryDetails.add(new RunHistoryDetails(++runNumber, expansionDegree, List.of(input), y, programExecutor.getCyclesNumber()));

        return new ExecutionDetails(new ProgramDetails(runningProgram.getName(), runningProgram.getInputVariables(), runningProgram.getWorkVariables(),
                runningProgram.getLabels(), runningProgram.getInstructions(), runningProgram.calculateMaxDegree(), runningProgram.calculateBasicInstructionsNumber()), programExecutor.getVariablesContext(), programExecutor.getCyclesNumber());
    }

    // 5. Show Statistics
    @Override
    public List<RunHistoryDetails> getStatistics() {
        return runHistoryDetails;
    }

    @Override
    public boolean isRunning() {
        return runNumber > 0;
    }
}
