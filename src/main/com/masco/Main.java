/*

          /\/\
         /    \
        / /\/\ \
        \/    \/

     Author: Made by Masco

     Github: https://github.com/m4sc0

 */

package main.com.masco;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/mainWindow.fxml"));
        primaryStage.setTitle("Sorting Visualization");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
