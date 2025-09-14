package application.fileloader;

import application.ApplicationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;

import java.io.File;

public class FileLoaderController {

    private ApplicationController applicationController;

    @FXML
    private Label currentPathLabel;
    private SimpleStringProperty currentPathProperty;

    @FXML
    private Label fileLoadingStatusLabel;
    private SimpleStringProperty fileLoadingStatusProperty;

    @FXML
    private ProgressIndicator loadFileTaskProgressIndicator;

    public FileLoaderController()
    {
        currentPathProperty = new SimpleStringProperty();
        fileLoadingStatusProperty = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        currentPathLabel.textProperty().bind(currentPathProperty);
        fileLoadingStatusLabel.textProperty().bind(fileLoadingStatusProperty);
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    void loadFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        currentPathProperty.set(absolutePath);
        try {
            applicationController.loadProgram(selectedFile);
            fileLoadingStatusProperty.set("File has been loaded successfully.");
        } catch (RuntimeException e) {
            fileLoadingStatusProperty.set("File couldn't be loaded: " + e.getCause().getMessage() + ".");
        }
    }

}
