package application.theme;

import javafx.scene.Scene;

public class ThemeManager {
    private final Scene scene;

    public ThemeManager(Scene scene) {
        this.scene = scene;
    }

    public void applyTheme(AppTheme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(theme.getPath()).toExternalForm());
    }
}

