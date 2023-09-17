package com.masco.sortingvisualization;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Sorting Visualization");
        stage.setScene(scene);
        stage.show();

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));

        SortingVisualization visualizer = new SortingVisualization(root);
        visualizer.createBars(50, scene.getWidth(), scene.getHeight());
        visualizer.barChart.setMaxWidth(Double.MAX_VALUE);  // Add this line

        Button scrambleBtn = new Button("Scramble Bars");
        scrambleBtn.setOnAction(e -> visualizer.scrambleBars());

        Button bubbleSort = new Button("Bubble Sort");
        bubbleSort.setOnAction(e -> visualizer.bubbleSort());

        Button checkBars = new Button("Check Bars");
        checkBars.setOnAction(e -> visualizer.checkBars());

        styleButton(scrambleBtn, 10, 10);
        styleButton(bubbleSort, 50, 10);
        styleButton(checkBars, 10, 100);

        root.getChildren().add(scrambleBtn);
        root.getChildren().add(bubbleSort);
        root.getChildren().add(checkBars);

        Button insertionSortBtn = new Button("Insertion Sort");
        insertionSortBtn.setOnAction(e -> visualizer.insertionSort());
        styleButton(insertionSortBtn, 50, 100); // Adjusted the y-coordinate to avoid overlap with the previous button
        root.getChildren().add(insertionSortBtn);
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
