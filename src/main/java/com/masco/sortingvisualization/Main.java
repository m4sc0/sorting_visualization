package com.masco.sortingvisualization;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Sorting Visualization");
        stage.setScene(scene);
        stage.show();

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));

        SortingVisualization visualizer = new SortingVisualization(root);
        visualizer.createBars(100, scene.getWidth(), scene.getHeight());
        visualizer.barChart.setMaxWidth(Double.MAX_VALUE);

        Button scrambleBtn = new Button("Scramble Bars");
        scrambleBtn.setOnAction(e -> visualizer.scrambleBars());

        Button bubbleSort = new Button("Bubble Sort");
        bubbleSort.setOnAction(e -> visualizer.bubbleSort());

        Button checkBars = new Button("Check Bars");
        checkBars.setOnAction(e -> visualizer.checkBars());

        styleButton(scrambleBtn, 10, 10);
        styleButton(checkBars, 10, 60);
        styleButton(bubbleSort, 10, 110);

        root.getChildren().addAll(scrambleBtn, bubbleSort, checkBars);
    }

    public void styleButton(Button btn, int x, int y) {
        Platform.runLater(() -> {
            btn.setLayoutX(x);
            btn.setLayoutY(y);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
