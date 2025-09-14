package application.instructionhistory;

import application.ApplicationController;
import components.instruction.Instruction;
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
    private TableView<Instruction> instructionTableView;

    @FXML
    private TableColumn<Instruction, Integer> cyclesTableColumn;

    @FXML
    private TableColumn<Instruction, String> instructionDataTableColumn;

    @FXML
    private TableColumn<Instruction, String> labelTableColumn;

    @FXML
    private TableColumn<Instruction, Integer> numberTableColumn;

    @FXML
    private TableColumn<Instruction, String> typeTableColumn;

    private final ObservableList<Instruction> instructionList = FXCollections.observableArrayList();

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    private void initialize() {
        numberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getInstructionNumber()).asObject());

        typeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getInstructionType().name().toLowerCase()));

        labelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLabel().getStringLabel()));

        instructionDataTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStringInstruction()));

        cyclesTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCyclesNumber()).asObject());

        instructionTableView.setItems(instructionList);
    }

    public void showHistory(Instruction instruction) {
        instructionTableView.getItems().clear();
        Instruction currentInstruction = instruction;
        while (currentInstruction.hasAncientInstruction()) {
            instructionList.add(instruction.getAncientInstruction());
            currentInstruction = currentInstruction.getAncientInstruction();
        }

        Collections.reverse(instructionList);
    }

}

