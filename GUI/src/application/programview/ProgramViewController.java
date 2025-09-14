package application.programview;

import application.ApplicationController;
import application.instructionhistory.InstructionHistoryController;
import components.instruction.Instruction;
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
    private IntegerProperty currentDegreeProperty;
    private IntegerProperty maxDegreeProperty;

    @FXML
    private TableColumn<Instruction, Integer> instructionCyclesTableColumn;

    @FXML
    private TableColumn<Instruction, String> instructionDataTableColumn;

    @FXML
    private TableColumn<Instruction, String> instructionLabelTableColumn;

    @FXML
    private TableColumn<Instruction, Integer> instructionNumberTableColumn;

    @FXML
    private TableColumn<Instruction, String> instructionTypeTableColumn;

    @FXML
    private TableView<Instruction> instructionsTableView;

    private final ObservableList<Instruction> instructionList = FXCollections.observableArrayList();

    @FXML
    private Label summaryLineLabel;
    private IntegerProperty basicInstructionsProperty;
    private IntegerProperty syntheticInstructionsProperty;

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
        instructionNumberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getInstructionNumber()).asObject());

        instructionTypeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInstructionType().name().toLowerCase()));

        instructionLabelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLabel().getStringLabel()));

        instructionDataTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStringInstruction()));

        instructionCyclesTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCyclesNumber()).asObject());

        instructionsTableView.setItems(instructionList);

        //
        selectedDegreeComboBox.setPromptText("Select Degree");

        // update properties
        currentDegreeProperty.setValue(0);
        currentDegreeLabel.textProperty().bind(Bindings.format("%d / %d", currentDegreeProperty,  maxDegreeProperty));

        summaryLineLabel.textProperty().bind(Bindings.format("Basic: %d, Synthetic: %d", basicInstructionsProperty, syntheticInstructionsProperty));
        selectedDegreeComboBox.disableProperty().bind(maxDegreeProperty.isEqualTo(0));

        instructionsTableView.setRowFactory(tv -> {
            TableRow<Instruction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Instruction clickedRow = row.getItem();
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
