package application.programview;

import application.ApplicationController;
import application.instructionhistory.InstructionHistoryController;
import components.instruction.Instruction;
import dtos.InstructionDetails;
import dtos.ProgramDetails;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProgramViewController {

    private ApplicationController applicationController;

    @FXML
    private Label currentDegreeLabel;

    private final IntegerProperty currentDegreeProperty;
    private final IntegerProperty maxDegreeProperty;

    @FXML
    private TableView<InstructionDetails> instructionsTableView;

    private final ObservableList<InstructionDetails> instructionList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<InstructionDetails, Integer> instructionNumberTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> instructionTypeTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> instructionLabelTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> instructionDataTableColumn;

    @FXML
    private TableColumn<InstructionDetails, Integer> instructionCyclesTableColumn;

    @FXML
    private Label summaryLineLabel;

    private final IntegerProperty basicInstructionsProperty;
    private final IntegerProperty syntheticInstructionsProperty;

    @FXML
    private ComboBox<Integer> selectedDegreeComboBox;

    public ProgramViewController() {
        currentDegreeProperty = new SimpleIntegerProperty();
        maxDegreeProperty = new SimpleIntegerProperty();

        basicInstructionsProperty = new SimpleIntegerProperty();
        syntheticInstructionsProperty = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        initializeTable();

        selectedDegreeComboBox.setPromptText("Select Degree");

        currentDegreeProperty.setValue(0);
        currentDegreeLabel.textProperty().bind(Bindings.format("%d / %d", currentDegreeProperty,  maxDegreeProperty));

        summaryLineLabel.textProperty().bind(Bindings.format("Basic: %d, Synthetic: %d", basicInstructionsProperty, syntheticInstructionsProperty));
        selectedDegreeComboBox.disableProperty().bind(maxDegreeProperty.isEqualTo(0));
    }

    private void initializeTable() {
        instructionNumberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().ordinalNumber()).asObject());

        instructionTypeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().type().name().toLowerCase()));

        instructionLabelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().label().label()));

        instructionDataTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().instructionContent()));

        instructionCyclesTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().cycles()).asObject());

        instructionsTableView.setItems(instructionList);

        instructionsTableView.setRowFactory(tv -> {
            TableRow<InstructionDetails> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    InstructionDetails clickedRow = row.getItem();
                    applicationController.handleRowDoubleClick(clickedRow);
                }
            });
            return row;
        });
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void selectedDegreeComboBoxAction(ActionEvent event) {
        int selectedDegree = selectedDegreeComboBox.getValue();
        applicationController.expandProgram(selectedDegree);
    }

    public void initializeProgram(ProgramDetails programDetails) {
        instructionsTableView.getItems().clear();
        instructionList.addAll(programDetails.instructions());

        currentDegreeProperty.set(0);
        maxDegreeProperty.set(programDetails.maxDegree());

        selectedDegreeComboBox.getItems().clear();
        for (int i = 0; i <= programDetails.maxDegree(); i++) {
            selectedDegreeComboBox.getItems().add(i);
        }

        basicInstructionsProperty.setValue(programDetails.basicInstructionsNumber());
        syntheticInstructionsProperty.setValue(programDetails.instructions().size() - basicInstructionsProperty.getValue());

        applicationController.displayInputVariables(programDetails.inputVariables());
    }

    public void updateProgram(ProgramDetails programDetails, int selectedDegree) {
        instructionsTableView.getItems().clear();
        instructionList.addAll(programDetails.instructions());

        currentDegreeProperty.setValue(selectedDegree);

        basicInstructionsProperty.setValue(programDetails.basicInstructionsNumber());
        syntheticInstructionsProperty.setValue(programDetails.instructions().size() - basicInstructionsProperty.getValue());

    }

}
