package application.programview.instructionstable;

import application.programview.ProgramViewController;
import components.instruction.InstructionSemantic;
import dtos.InstructionDetails;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.List;

public class InstructionsTableController {
    ProgramViewController programViewController;

    @FXML
    private TableView<InstructionDetails> instructionsTableView;

    @FXML
    private TableColumn<InstructionDetails, String> cyclesTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> instructionTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> labelTableColumn;

    @FXML
    private TableColumn<InstructionDetails, Integer> numberTableColumn;

    @FXML
    private TableColumn<InstructionDetails, String> typeTableColumn;

    private final ObservableList<InstructionDetails> instructionsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        numberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().ordinalNumber()).asObject());
        typeTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().type().getInstructionTypeChar())));
        labelTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().label().label()));
        instructionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().instructionContent()));
        cyclesTableColumn.setCellValueFactory(cellData -> {
            String str = String.valueOf(cellData.getValue().type().getCyclesNumber());
            if (cellData.getValue().type() == InstructionSemantic.JUMP_EQUAL_FUNCTION ||
            cellData.getValue().type() == InstructionSemantic.QUOTE) {
                str = str + "+";
            }
            return new SimpleStringProperty(str);
        });

        instructionsTableView.setItems(instructionsList);
    }

    public void setProgramViewController(ProgramViewController programViewController) {
        this.programViewController = programViewController;
    }

    public void initializeTable(List<InstructionDetails> instructions) {
        instructionsList.clear();
        instructionsList.addAll(instructions);
    }

    public void highlightInstructionLine(int index) {
        instructionsTableView.getSelectionModel().select(index);
        instructionsTableView.scrollTo(index);
    }

    public void highlightSign(String sign) {
        instructionsTableView.getSelectionModel().clearSelection();
        instructionsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (InstructionDetails instruction : instructionsList) {
            String[] cells = instruction.instructionContent().split(" ");
            if (instruction.label().label().equals(sign) || Arrays.asList(cells).contains(sign)) {
                instructionsTableView.getSelectionModel().select(instruction);
            }
        }
    }

    public void unhighlight() {
        instructionsTableView.getSelectionModel().clearSelection();
    }
}



