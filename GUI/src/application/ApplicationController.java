package application;

import application.execution.ExecutionController;
import application.fileloader.FileLoaderController;
import application.programview.ProgramViewController;
import application.statistics.StatisticsController;
import components.engine.Engine;
import components.engine.StandardEngine;
import dtos.*;
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
    private ExecutionController executionComponentController;

    @FXML
    private VBox statisticsComponent;

    @FXML
    private StatisticsController statisticsComponentController;

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

        if (executionComponentController != null) {
            executionComponentController.setApplicationController(this);
        }

        if (statisticsComponentController != null) {
            statisticsComponentController.setApplicationController(this);
        }
    }

    public void loadProgram(File programFile) {
        engine.loadProgramFromFile(programFile);
        programViewComponentController.loadNewProgram(engine.getProgramDetails());

        showProgramStatistics();
    }

    public void expandProgram(int expansionDegree) {
        programViewComponentController.updateProgram(engine.expandProgram(expansionDegree), expansionDegree);
        programDegree = expansionDegree;
    }

    public void setCurrentProgram(String programName) {
        engine.setCurrentProgram(programName);
        programViewComponentController.displayNewProgram(engine.getProgramDetails());
        showProgramStatistics();
    }

    public void handleRowDoubleClick(InstructionDetails instruction) {
        programViewComponentController.loadInstructionHistory(instruction);
    }

    public void displayInputVariables(List<VariableDetails> variables) {
        executionComponentController.setInputVariables(variables);
    }

    public ExecutionDetails runProgram(Long... inputs) {
        ExecutionDetails executionDetails = engine.runProgram(programDegree, inputs);
        showProgramStatistics();
        return executionDetails;
    }

    public DebugDetails startDebuggingProgram(Long... inputs)
    {
        return engine.debugProgram(programDegree, inputs);
    }

    public DebugDetails debuggingStepForward() {
        return engine.debugStepForward();
    }

    public DebugDetails debuggingResume() {
        return engine.debugResume();
    }

    public void highlightRow(int index) {
        programViewComponentController.highlightProgramInstruction(index);
    }

    public void unhighlight() {
        programViewComponentController.unhighlightInstructions();
    }

    public void showProgramStatistics() {
        statisticsComponentController.showProgramHistory(engine.getStatistics());
    }

    public void insertInputs(List<String> inputs) {
        executionComponentController.insertInputs(inputs);
    }

    public void setNewRun() {
        executionComponentController.setNewRun();
    }

}