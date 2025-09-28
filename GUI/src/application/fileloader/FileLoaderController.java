package application.fileloader;

import application.ApplicationController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;

import java.io.File;

import static java.lang.Thread.sleep;

public class FileLoaderController {

    private ApplicationController applicationController;

    @FXML
    private Label currentPathLabel;
    private SimpleStringProperty currentPathProperty;

    @FXML
    private Label fileLoadingStatusLabel;
    private SimpleStringProperty fileLoadingStatusProperty;

    @FXML
    private ProgressBar loadFileTaskProgressBar;

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
        loadFileTask(selectedFile);
    }

    void loadFileTask(File file) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    sleep(30); // simulate work
                    updateProgress(i, steps);
                    updateMessage("Step " + i + " of " + steps);
                }

                return null;
            }
        };

        loadFileTaskProgressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            try {
                applicationController.loadProgram(file);
                fileLoadingStatusProperty.set("File has been loaded successfully.");
            } catch (RuntimeException e) {
                fileLoadingStatusProperty.set("File couldn't be loaded: " + e.getCause().getMessage() + ".");
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
