package components.engine;

import components.jaxb.generated.SInstruction;
import components.jaxb.generated.SInstructionArgument;
import components.jaxb.generated.SProgram;
import components.program.Program;
import dtos.ProgramDetails;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class StandardEngine implements Engine {
    final static String JAXB_XML_PACKAGE_NAME = "components.jaxb.generated";

    private Program program;
    private boolean programLoaded = false;

    public boolean isProgramLoaded() {
        return programLoaded;
    }

    @Override
    public void loadProgramFromFile(File file) {
        SProgram sProgram = parseXmlFile(file);
        try {
            jumpLabelsAreValid(sProgram);
            // parse to Program
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
}
