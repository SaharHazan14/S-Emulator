package application.statistics.variablestable;

import dtos.VariableDetails;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class VariablesTableController {

    @FXML
    private TableColumn<Map.Entry<VariableDetails, Long>, Long> valueTableColumn;

    @FXML
    private TableColumn<Map.Entry<VariableDetails, Long>, String> variableTableColumn;

    @FXML
    private TableView<Map.Entry<VariableDetails, Long>> variablesTableView;

    private final ObservableList<Map.Entry<VariableDetails, Long>> entryObservableList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        variableTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKey().variable()));
        valueTableColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue()));
        variablesTableView.setItems(entryObservableList);
    }

    public void initializeTable(List<Map.Entry<VariableDetails, Long>> variablesList) {
        variablesTableView.getItems().clear();
        variablesTableView.getItems().addAll(variablesList);
    }

}

