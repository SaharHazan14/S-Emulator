package application.settings;

import application.ApplicationController;
import application.theme.AppTheme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SettingsController {
    private ApplicationController applicationController;

    @FXML
    private ComboBox<AppTheme> themeComboBox;

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    private void initialize() {
        initializeThemeComboBox();
    }

    private void initializeThemeComboBox() {
        themeComboBox.getItems().addAll(AppTheme.values());
        themeComboBox.setValue(AppTheme.LIGHT_GREEN);
    }

    @FXML
    void themeComboBoxAction(ActionEvent event) {
        applicationController.setAppTheme(themeComboBox.getValue());
    }
}

