package application.statistics;

import application.ApplicationController;
import application.statistics.runtable.RunTableController;
import dtos.RunHistoryDetails;
import dtos.VariableDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsController {
    private ApplicationController applicationController;

    @FXML
    private TableView<RunHistoryDetails> runHistoryComponent;

    @FXML
    private RunTableController runHistoryComponentController;

    @FXML
    private Button showAllButton;

    @FXML
    private Button rerunButton;

    private final ObservableList<Map.Entry<VariableDetails, Long>> entryObservableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (runHistoryComponentController != null) {
            runHistoryComponentController.setStatisticsController(this);

            showAllButton.disableProperty().bind(runHistoryComponent.getSelectionModel().selectedItemProperty().isNull());
            rerunButton.disableProperty().bind(runHistoryComponent.getSelectionModel().selectedItemProperty().isNull());
        }
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public void showProgramHistory(List<RunHistoryDetails> runHistoryDetails) {
        runHistoryComponentController.initializeTable(runHistoryDetails);
    }

    @FXML
    void showAllButtonAction(ActionEvent event) {
        try {
            RunHistoryDetails runHistoryDetails = runHistoryComponent.getSelectionModel().getSelectedItem();

            Stage popup = new Stage();
            String title = "Run " + runHistoryDetails.runNumber() + " variables context";
            popup.setTitle(title);
            TableView<Map.Entry<VariableDetails, Long>> variablesTableView = FXMLLoader.load(getClass().getResource("variablestable/variablesTable.fxml"));
            variablesTableView.setItems(entryObservableList);
            entryObservableList.clear();
            entryObservableList.addAll(runHistoryDetails.context().variablesContext());

            Scene scene = new Scene(variablesTableView);
            popup.setScene(scene);
            popup.show();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void rerunButtonAction(ActionEvent event) {
        RunHistoryDetails runHistoryDetails = runHistoryComponent.getSelectionModel().getSelectedItem();
        applicationController.expandProgram(runHistoryDetails.expansionDegree());
        applicationController.setNewRun();
        List<String> inputs = new ArrayList<>();
        for (Long input : runHistoryDetails.inputs()) {
            inputs.add(input.toString());
        }

        applicationController.insertInputs(inputs);
    }
}
