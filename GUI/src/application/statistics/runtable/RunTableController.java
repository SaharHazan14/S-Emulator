package application.statistics.runtable;

import application.statistics.StatisticsController;
import dtos.RunHistoryDetails;
import dtos.VariableDetails;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class RunTableController {
    private StatisticsController statisticsController;

    @FXML
    private TableView<RunHistoryDetails> runHistoryTableView;

    @FXML
    private TableColumn<RunHistoryDetails, Integer> cyclesTableColumn;

    @FXML
    private TableColumn<RunHistoryDetails, Integer> degreeTableColumn;

    @FXML
    private TableColumn<RunHistoryDetails, String> inputTableColumn;

    @FXML
    private TableColumn<RunHistoryDetails, Integer> numberTableColumn;

    @FXML
    private TableColumn<RunHistoryDetails, Long> outputTableColumn;

    private final ObservableList<RunHistoryDetails> runHistoryList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        numberTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().runNumber()).asObject());
        degreeTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().expansionDegree()).asObject());
        inputTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().inputs().toString()));
        outputTableColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().context().variablesContext().getFirst().getValue()).asObject());
        cyclesTableColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().cyclesNumber()).asObject());
        runHistoryTableView.setItems(runHistoryList);
    }

    public void setStatisticsController(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }

    public void initializeTable(List<RunHistoryDetails> runHistoryDetails) {
        runHistoryList.clear();
        runHistoryList.addAll(runHistoryDetails);
    }

}
