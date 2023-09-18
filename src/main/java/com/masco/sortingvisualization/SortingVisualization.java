package com.masco.sortingvisualization;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SortingVisualization {
    public AnchorPane root;
    public BarChart<String, Number> barChart;
    public XYChart.Series<String, Number> series;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;

    public SortingVisualization(AnchorPane root) {
        this.root = root;
        this.xAxis = new CategoryAxis();
        this.yAxis = new NumberAxis();
        this.yAxis.setOpacity(0);
        this.barChart = new BarChart<>(xAxis, yAxis);
        this.series = new XYChart.Series<>();
    }

    public void createBars(int numberOfBars, double sceneWidth, double sceneHeight) {
        Platform.runLater(() -> {
            double barWidth = sceneWidth / numberOfBars;
            double minHeight = 20;
            double maxHeight = sceneHeight - 100;
            double heightIncrement = (maxHeight - minHeight) / (numberOfBars - 1);

            for (int i = 0; i < numberOfBars; i++) {
                double height = minHeight + i * heightIncrement;

                XYChart.Data<String, Number> bar = new XYChart.Data<>(Integer.toString(i), height);
                series.getData().add(bar);
            }

            barChart.getData().add(series);
            addLayoutBoundsListener(numberOfBars);
            addScenePropertyListener(numberOfBars);

            AnchorPane.setBottomAnchor(barChart, 0.0);
            AnchorPane.setLeftAnchor(barChart, 0.0);
            AnchorPane.setRightAnchor(barChart, 0.0);

            root.getChildren().add(barChart);
            styleBarChart();
        });
    }

    private void addLayoutBoundsListener(int numberOfBars) {
        barChart.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            styleBarColor(numberOfBars);
        });
    }

    private void addScenePropertyListener(int numberOfBars) {
        ChangeListener<Scene> sceneChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                styleBarColor(numberOfBars);
            }
        };

        barChart.sceneProperty().addListener(sceneChangeListener);
        barChart.sceneProperty().removeListener(sceneChangeListener);
    }


    private void styleBarColor(int numberOfBars) {
        for (int i = 0; i < numberOfBars; i++) {
            Node bar = ((XYChart.Data) series.getData().get(i)).getNode();
            if (bar != null) {
                bar.setStyle("-fx-background-color: #FFFFFF;");
            }
        }
    }

    private void styleBarChart() {
        Platform.runLater(() -> {
            barChart.setBarGap(0);
            barChart.setCategoryGap(3);
            barChart.setLegendVisible(false);
            barChart.setAnimated(false);
            barChart.setHorizontalGridLinesVisible(false);
            barChart.setVerticalGridLinesVisible(false);
            barChart.setHorizontalZeroLineVisible(false);
            barChart.setVerticalZeroLineVisible(false);
            barChart.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: black;");
            barChart.lookup(".axis:horizontal").setVisible(false); // hides the horizontal axis
            barChart.lookup(".axis:vertical").setVisible(false);   // hides the vertical axis
            barChart.lookup(".axis:horizontal").setOpacity(0); // makes the horizontal axis transparent
            barChart.lookup(".axis:vertical").setOpacity(0);   // makes the vertical axis transparent
            xAxis.setTickLabelsVisible(false);
            xAxis.setTickMarkVisible(false);
            yAxis.setTickLabelsVisible(false);
            yAxis.setTickMarkVisible(false);
            yAxis.setSide(Side.LEFT);
            root.setPadding(new Insets(0));
        });
    }


    public void scrambleBars() {
        Platform.runLater(() -> {
            List<Number> heights = new ArrayList<>();
            for (XYChart.Data<String, Number> data : series.getData()) {
                heights.add(data.getYValue());
            }

            Collections.shuffle(heights);

            for (int i = 0; i < series.getData().size(); i++) {
                series.getData().get(i).setYValue(heights.get(i));
            }

            // Re-style the bars after scrambling
            styleBarColor(series.getData().size());
        });
    }

    private int wait = 1;

    public void bubbleSort() {
        sort(() -> {
            int n = series.getData().size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    compareAndSwitch(j, j + 1);
                }
            }
        });
    }

    public void sort(SortingAlgorithm algorithm) {
        new Thread(() -> {
            try {
                algorithm.sort();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            checkBars();
        }).start();
    }

    @FunctionalInterface
    interface SortingAlgorithm {
        void sort() throws InterruptedException;
    }


    public void switchBars(int i1, int i2) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1); // a synchronization helper

        Platform.runLater(() -> {
            // Get the two bars you want to switch
            XYChart.Data<String, Number> bar1 = series.getData().get(i1);
            XYChart.Data<String, Number> bar2 = series.getData().get(i2);

            // Temporarily set their colors to green
            bar1.getNode().setStyle("-fx-background-color: #00FF00;");
            bar2.getNode().setStyle("-fx-background-color: #00FF00;");

            // Use a separate thread to introduce the delay, then switch the bars and reset their colors
            new Thread(() -> {
                try {
                    Thread.sleep(wait);

                    // Swap their heights
                    Number tempHeight = bar1.getYValue();
                    bar1.setYValue(bar2.getYValue());
                    bar2.setYValue(tempHeight);

                    // Reset their colors to white on the JavaFX application thread
                    Platform.runLater(() -> {
                        bar1.getNode().setStyle("-fx-background-color: #FFFFFF;");
                        bar2.getNode().setStyle("-fx-background-color: #FFFFFF;");
                        latch.countDown();  // release the latch after the operation completes
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        latch.await();
    }

    public void checkBars() {
        new Thread(() -> {
            for (int i = 0; i < series.getData().size() - 1; i++) {
                XYChart.Data<String, Number> bar1 = series.getData().get(i);
                XYChart.Data<String, Number> bar2 = series.getData().get(i + 1);
                if (bar1.getYValue().doubleValue() < bar2.getYValue().doubleValue()) {
                    Platform.runLater(() -> {
                        bar1.getNode().setStyle("-fx-background-color: #00ff00");
                        bar2.getNode().setStyle("-fx-background-color: #00ff00");
                    });
                } else {
                    paintBars(Color.RED);
                    return;
                }
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            paintBars(new Color(0, 1, 0, 1));
        }).start();
    }

    public void paintBars(Color color) {
        String hexColor = color.toString().replace("0x", "#").substring(0, 7);
        String finalHexColor = hexColor;
        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                series.getData().get(i).getNode().setStyle("-fx-background-color: " + finalHexColor + ";");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hexColor = "FFF";
        String finalHexColor1 = hexColor;
        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                series.getData().get(i).getNode().setStyle("-fx-background-color: " + finalHexColor1 + ";");
            }
        });
    }

    public double getBarHeight(int index) {
        XYChart.Data<String, Number> barData = series.getData().get(index);
        return barData.getYValue().doubleValue();
    }

    private boolean isGreater(int i, int j) {
        return getBarHeight(i) > getBarHeight(j);
    }

    private void setColor(int index, Color color) {
        String hexColor = color.toString().replace("0x", "#").substring(0, 7);
        series.getData().get(index).getNode().setStyle("-fx-background-color: " + hexColor + ";");
    }

    private void compareAndSwitch(int i, int j) throws InterruptedException {
        if (isGreater(i, j)) {
            switchBars(i, j);
            Thread.sleep(wait);
        } else {
            setColor(i, Color.WHITE);
            setColor(j, Color.WHITE);
        }
    }

}
