package application.execution;

import application.ApplicationController;
import components.instruction.Instruction;
import components.variable.Variable;
import dtos.ExecutionDetails;
import javafx.beans.property.SimpleLongProperty;
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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecutionController {
    private ApplicationController applicationController;

    @FXML
    private Label cyclesConsumedLabel;

    @FXML
    private ScrollPane inputsScrollPane;

    @FXML
    private TableColumn<Map.Entry<Variable, Long>, String> variableNameTableColumn;

    @FXML
    private TableColumn<Map.Entry<Variable, Long>, Long> variableValueTableColumn;

    @FXML
    private TableView<Map.Entry<Variable, Long>> variablesResultTableView;

    private List<TextField> inputsValues = new ArrayList<>();

    private final ObservableList<Map.Entry<Variable, Long>> entryObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        variableNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().getStringVariable()));
        variableValueTableColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue()));

        variablesResultTableView.setItems(entryObservableList);
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void runButtonAction(ActionEvent event) {
        variablesResultTableView.getItems().clear();

        Long[] inputs = new Long[inputsValues.size()];

        for (int i = 0; i < inputsValues.size(); i++) {
            inputs[i] = Long.parseLong(inputsValues.get(i).getText());
            // ADD CHECK FOR LONG VALUES ONLY
        }

        ExecutionDetails executionDetails = applicationController.runProgram(inputs);

        // ADD ITEMS TO TABLE
        entryObservableList.addAll(executionDetails.variables().getVariables().entrySet());

    }

    public void setInputVariables(List<Variable> variables) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        for (Variable variable : variables) {
            Label label = new Label(variable.getStringVariable());
            TextField textField = new TextField();
            inputsValues.add(textField);
            textField.setPrefWidth(50);

            HBox row = new HBox(10, label, textField);
            container.getChildren().add(row);
        }

        inputsScrollPane.setContent(container);
    }

}

