package components.engine;

import components.executor.Executor;
import components.executor.ProgramExecutor;
import components.program.JaxbConversion;
import components.jaxb.generated.SInstruction;
import components.jaxb.generated.SInstructionArgument;
import components.jaxb.generated.SProgram;
import components.program.Program;
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
    private List<RunHistoryDetails> runHistory;

    public StandardEngine() {
        this.runHistory = new ArrayList<>();
    }

    @Override
    public boolean isProgramLoaded() {
        return programLoaded;
    }

    @Override
    public void loadProgramFromFile(File file) {
        SProgram sProgram = parseXmlFile(file);
        try {
            jumpLabelsAreValid(sProgram);
            program = JaxbConversion.SProgramToProgram(sProgram);
            programLoaded = true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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


    @Override
    public ProgramDetails getProgramDetails() {
        return new ProgramDetails(program.getName(), program.getInputVariables(),
                program.getLabels(), program.getInstructions());
    }

    @Override
    public ProgramDetails getExpandedProgramDetails(int degree) {
        if (!programLoaded) {
            throw new IllegalStateException("No program loaded.");
        }
        Program expandedProgram = program.expand(degree);
        return new ProgramDetails(expandedProgram.getName(), expandedProgram.getInputVariables(),
                expandedProgram.getLabels(), expandedProgram.getInstructions());
    }

    @Override
    public int getMaxDegree() {
        if(!programLoaded) {
            return 0;
        }
        return program.calculateMaxDegree();
    }


    @Override
    public RunHistoryDetails runProgram(int degree, List<Long> inputs) {
        Program programToRun = program.expand(degree);
        Executor executor = new ProgramExecutor();
        long outputY = executor.run(programToRun, inputs);

        RunHistoryDetails details = new RunHistoryDetails(
                runHistory.size() + 1,
                degree,
                inputs,
                outputY,
                executor.getCyclesConsumed()
        );

        runHistory.add(details);
        return details;
    }

    @Override
    public List<RunHistoryDetails> getRunHistory() {
        return runHistory;
    }
}