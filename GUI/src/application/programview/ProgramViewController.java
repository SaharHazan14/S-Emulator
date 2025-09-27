package application.programview;

import application.ApplicationController;
import application.programview.instructionstable.InstructionsTableController;
import components.variable.Variable;
import dtos.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProgramViewController {

    private ApplicationController applicationController;

    @FXML
    private TableView<InstructionDetails> programInstructionsComponent;

    @FXML
    private InstructionsTableController programInstructionsComponentController;

    @FXML
    private TableView<InstructionDetails> instructionHistoryComponent;

    @FXML
    private InstructionsTableController instructionHistoryComponentController;

    @FXML
    private Label currentDegreeLabel;

    private final IntegerProperty currentDegreeProperty;
    private final IntegerProperty maxDegreeProperty;

    @FXML
    private Label summaryLineLabel;

    private final IntegerProperty basicInstructionsProperty;
    private final IntegerProperty syntheticInstructionsProperty;

    @FXML
    private ComboBox<Integer> selectedDegreeComboBox;

    @FXML
    private ComboBox<String> highlightComboBox;

    @FXML
    private ComboBox<String> chooseProgramComboBox;

    private final SimpleBooleanProperty noProgramLoadedProperty;

    public ProgramViewController() {
        currentDegreeProperty = new SimpleIntegerProperty();
        maxDegreeProperty = new SimpleIntegerProperty();

        basicInstructionsProperty = new SimpleIntegerProperty();
        syntheticInstructionsProperty = new SimpleIntegerProperty();

        noProgramLoadedProperty = new SimpleBooleanProperty(true);
    }

    @FXML
    private void initialize() {
        if (programInstructionsComponentController != null) {
            programInstructionsComponentController.setProgramViewController(this);
        }

        if (instructionHistoryComponentController != null) {
            instructionHistoryComponentController.setProgramViewController(this);
        }

        programInstructionsComponent.setRowFactory(tv -> {
            TableRow<InstructionDetails> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    InstructionDetails instructionDetails = row.getItem();
                    loadInstructionHistory(instructionDetails);
                }
            });
            return row;
        });

        selectedDegreeComboBox.setPromptText("Degree");
        highlightComboBox.setPromptText("Highlight");
        chooseProgramComboBox.setPromptText("Program");

        currentDegreeProperty.setValue(0);
        currentDegreeLabel.textProperty().bind(Bindings.format("%d / %d", currentDegreeProperty,  maxDegreeProperty));

        summaryLineLabel.textProperty().bind(Bindings.format("Instructions Summery: Basic - %d / Synthetic - %d", basicInstructionsProperty, syntheticInstructionsProperty));
        selectedDegreeComboBox.disableProperty().bind(maxDegreeProperty.isEqualTo(0));
        highlightComboBox.disableProperty().bind(noProgramLoadedProperty);
        chooseProgramComboBox.disableProperty().bind(noProgramLoadedProperty);
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void selectedDegreeComboBoxAction(ActionEvent event) {
        if (selectedDegreeComboBox.getValue() != null) {
            applicationController.expandProgram(selectedDegreeComboBox.getValue());
        }
    }

    @FXML
    void highlightComboBoxAction(ActionEvent event) {
        if (highlightComboBox.getValue() != null) {
            programInstructionsComponentController.highlightSign(highlightComboBox.getValue());
        }
    }

    @FXML
    void chooseProgramComboBoxAction(ActionEvent event) {
        if (chooseProgramComboBox.getValue() != null) {
            applicationController.setCurrentProgram(chooseProgramComboBox.getValue());
        }
    }

    public void loadNewProgram(ProgramDetails programDetails) {
        programInstructionsComponentController.initializeTable(programDetails.instructions());

        initializeSelectedDegreeComboBox(programDetails.maxDegree());
        initializeHighlightComboBox(programDetails);
        initializeChooseProgramComboBox(programDetails);
        currentDegreeProperty.set(0);
        maxDegreeProperty.set(programDetails.maxDegree());
        basicInstructionsProperty.setValue(programDetails.basicInstructionsNumber());
        syntheticInstructionsProperty.setValue(programDetails.instructions().size() - basicInstructionsProperty.getValue());
        noProgramLoadedProperty.setValue(false);
        applicationController.displayInputVariables(programDetails.inputVariables());
    }

    public void displayNewProgram(ProgramDetails programDetails) {
        programInstructionsComponentController.initializeTable(programDetails.instructions());

        initializeSelectedDegreeComboBox(programDetails.maxDegree());
        initializeHighlightComboBox(programDetails);
        currentDegreeProperty.set(0);
        maxDegreeProperty.set(programDetails.maxDegree());
        basicInstructionsProperty.setValue(programDetails.basicInstructionsNumber());
        syntheticInstructionsProperty.setValue(programDetails.instructions().size() - basicInstructionsProperty.getValue());
        noProgramLoadedProperty.setValue(false);
        applicationController.displayInputVariables(programDetails.inputVariables());
    }

    private void initializeSelectedDegreeComboBox(int max) {
        selectedDegreeComboBox.setPromptText("Degree");
        selectedDegreeComboBox.getItems().clear();
        for (int i = 0; i <= max; i++) {
            selectedDegreeComboBox.getItems().add(i);
        }
    }

    private void initializeHighlightComboBox(ProgramDetails programDetails) {
        highlightComboBox.setPromptText("Highlight");
        highlightComboBox.getItems().clear();

        for (LabelDetails label : programDetails.labels()) {
            highlightComboBox.getItems().add(label.label());
        }

        highlightComboBox.getItems().add(Variable.OUTPUT.getStringVariable());

        for (VariableDetails xVar : programDetails.inputVariables()) {
            highlightComboBox.getItems().add(xVar.variable());
        }
        for (VariableDetails zVar : programDetails.workVariables()) {
            highlightComboBox.getItems().add(zVar.variable());
        }
    }

    private void initializeChooseProgramComboBox(ProgramDetails programDetails) {
        chooseProgramComboBox.setPromptText("Program");
        chooseProgramComboBox.getItems().clear();

        chooseProgramComboBox.getItems().add(programDetails.name());
        for (FunctionDetails function: programDetails.functions()) {
            chooseProgramComboBox.getItems().add(function.userString());
        }
    }

    public void updateProgram(ProgramDetails programDetails, int selectedDegree) {
        programInstructionsComponentController.initializeTable(programDetails.instructions());
        initializeHighlightComboBox(programDetails);
        currentDegreeProperty.setValue(selectedDegree);

        basicInstructionsProperty.setValue(programDetails.basicInstructionsNumber());
        syntheticInstructionsProperty.setValue(programDetails.instructions().size() - basicInstructionsProperty.getValue());
    }

    public void loadInstructionHistory(InstructionDetails instruction) {
        instructionHistoryComponentController.initializeTable(instruction.ancientInstructionsList());
    }

    public void highlightProgramInstruction(int index) {
        programInstructionsComponentController.highlightInstructionLine(index);
    }

    public void unhighlightInstructions() {
        programInstructionsComponentController.unhighlight();
    }
}
