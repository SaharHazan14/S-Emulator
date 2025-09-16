package application.execution;

import application.ApplicationController;
import dtos.DebugDetails;
import dtos.ExecutionDetails;
import dtos.VariableDetails;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class ExecutionController {
    private ApplicationController applicationController;

    @FXML
    private Label cyclesConsumedLabel;
    private SimpleIntegerProperty cyclesConsumedProperty;

    @FXML
    private ScrollPane inputsScrollPane;

    @FXML
    private TableColumn<Map.Entry<VariableDetails, Long>, String> variableNameTableColumn;

    @FXML
    private TableColumn<Map.Entry<VariableDetails, Long>, Long> variableValueTableColumn;

    @FXML
    private TableView<Map.Entry<VariableDetails, Long>> variablesResultTableView;

    private List<TextField> inputsValues = new ArrayList<>();

    private final ObservableList<Map.Entry<VariableDetails, Long>> entryObservableList = FXCollections.observableArrayList();

    @FXML
    private ToggleButton debugModeToggleButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stepOverButton;

    @FXML
    private Button stopDebuggingButton;

    public ExecutionController() {
        cyclesConsumedProperty = new SimpleIntegerProperty(0);
    }

    @FXML
    public void initialize() {
        variableNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().variable()));
        variableValueTableColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue()));

        variablesResultTableView.setItems(entryObservableList);

        cyclesConsumedLabel.textProperty().bind(Bindings.format("Cycles consumed: %d", cyclesConsumedProperty));
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void runButtonAction(ActionEvent event) {
        variablesResultTableView.getItems().clear();

        Long[] inputs = new Long[inputsValues.size()];

        for (int i = 0; i < inputsValues.size(); i++) {
            try {
                inputs[i] = Long.parseLong(inputsValues.get(i).getText());
            } catch (NumberFormatException e) {
                inputs[i] = 0L;
                inputsValues.get(i).setText("0");
            }
        }

        if (debugModeToggleButton.isSelected()) {
            DebugDetails debugDetails = applicationController.startDebuggingProgram(inputs);
            entryObservableList.addAll(debugDetails.context().variablesContext());
            applicationController.highlightRow(debugDetails.lineIndex());
        }
        else {
            ExecutionDetails executionDetails = applicationController.runProgram(inputs);

            entryObservableList.addAll(executionDetails.variablesContext().variablesContext());
            cyclesConsumedProperty.setValue(executionDetails.cycles());
        }
    }

    public void setInputVariables(List<VariableDetails> variables) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        for (VariableDetails variable : variables) {
            Label label = new Label(variable.variable());
            TextField textField = new TextField();
            inputsValues.add(textField);
            textField.setPrefWidth(50);

            HBox row = new HBox(10, label, textField);
            container.getChildren().add(row);
        }

        inputsScrollPane.setContent(container);
    }

    @FXML
    void resumeButtonAction(ActionEvent event) {

    }

    @FXML
    void stepOverButtonAction(ActionEvent event) {
        DebugDetails debugDetails = applicationController.debuggingStepForward();
        entryObservableList.clear();
        entryObservableList.addAll(debugDetails.context().variablesContext());
        cyclesConsumedProperty.setValue(debugDetails.cycles());
        applicationController.highlightRow(debugDetails.lineIndex());
        if (debugDetails.programEnded()) {
            stepOverButton.setDisable(true);
        }
    }

    @FXML
    void stopDebuggingButtonAction(ActionEvent event) {

    }
}

