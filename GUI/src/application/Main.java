package application;
import application.theme.AppTheme;
import application.theme.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
//        Scene scene = new Scene(root);
//
//        stage.setTitle("S-Emulator");
//        stage.setScene(scene);
//        stage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("application.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ThemeManager themeManager = new ThemeManager(scene);
        themeManager.applyTheme(AppTheme.LIGHT_GREEN);
        ApplicationController controller = loader.getController();
        controller.setThemeManager(themeManager);

        stage.setTitle("S-Emulator");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
