package application;

import application.execution.ExecutionController;
import application.fileloader.FileLoaderController;
import application.instructionhistory.InstructionHistoryController;
import application.programview.ProgramViewController;
import components.engine.Engine;
import components.engine.StandardEngine;
import dtos.DebugDetails;
import dtos.ExecutionDetails;
import dtos.InstructionDetails;
import dtos.VariableDetails;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class ApplicationController {

    private final Engine engine = new StandardEngine();

    @FXML
    private VBox fileLoaderComponent;

    @FXML
    private FileLoaderController fileLoaderComponentController;

    @FXML
    private VBox programViewComponent;

    @FXML
    private ProgramViewController programViewComponentController;

    @FXML
    private HBox instructionHistoryComponent;

    @FXML
    private InstructionHistoryController instructionHistoryComponentController;

    @FXML
    private VBox executionComponent;

    @FXML
    private ExecutionController executionComponentController;

    private int programDegree;

    public ApplicationController() {
    }

    @FXML
    public void initialize() {
        if (fileLoaderComponentController != null) {
            fileLoaderComponentController.setApplicationController(this);
        }

        if (programViewComponentController != null) {
            programViewComponentController.setApplicationController(this);
        }

        if (instructionHistoryComponentController != null) {
            instructionHistoryComponentController.setApplicationController(this);
        }

        if (executionComponentController != null) {
            executionComponentController.setApplicationController(this);
        }
    }

    public void loadProgram(File programFile) {
        engine.loadProgramFromFile(programFile);
        programViewComponentController.initializeProgram(engine.getProgramDetails());
    }

    public void expandProgram(int expansionDegree) {
        programViewComponentController.updateProgram(engine.expandProgram(expansionDegree), expansionDegree);
        programDegree = expansionDegree;
    }

    public void handleRowDoubleClick(InstructionDetails instruction) {
        instructionHistoryComponentController.showHistory(instruction);
    }

    public void displayInputVariables(List<VariableDetails> variables) {
        executionComponentController.setInputVariables(variables);
    }

    public ExecutionDetails runProgram(Long... inputs) {
        return engine.runProgram(programDegree, inputs);
    }

    public DebugDetails startDebuggingProgram(Long... inputs)
    {
        return engine.debugProgram(programDegree, inputs);
    }

    public DebugDetails debuggingStepForward() {
        return engine.debugStepForward();
    }

    public void highlightRow(int index) {
        programViewComponentController.highlightRow(index);
    }

}
