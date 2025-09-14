package application.instructionhistory;

import application.ApplicationController;
import components.instruction.Instruction;
import dtos.InstructionDetails;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Collections;

public class InstructionHistoryController {

    private ApplicationController applicationController;

    @FXML
    private TableView<InstructionDetails> instructionTableView;

    @FXML
    private TableColumn<InstructionDetails, Integer> cyclesTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> instructionDataTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> labelTableColumn;

    @FXML
    private TableColumn<InstructionDetails, Integer> numberTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> typeTableColumn;

    private final ObservableList<InstructionDetails> instructionList = FXCollections.observableArrayList();

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    private void initialize() {
        numberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().ordinalNumber()).asObject());

        typeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().type().name().toLowerCase()));

        labelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().label().label()));

        instructionDataTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().instructionContent()));

        cyclesTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().cycles()).asObject());

        instructionTableView.setItems(instructionList);
    }

    public void showHistory(InstructionDetails instruction) {
        instructionTableView.getItems().clear();
        instructionList.addAll(instruction.ancientInstructionsList());
    }

}

