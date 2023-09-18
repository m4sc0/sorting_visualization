/*

          /\/\
         /    \
        / /\/\ \
        \/    \/

     Author: Made by Masco

     Github: https://github.com/m4sc0

*/

package main.com.masco;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    @FXML
    private BorderPane displayBorderPane;
    @FXML
    private Slider arraySizeSlider, delaySlider;
    @FXML
    private Label statusLabel, arraySizeSliderLabel, delaySliderLabel, randomizeBtn, bubbleSortBtn, sortingAlgorithmLabel;

    private int TOTAL_NUMBER_OF_BARS;
    private int DELAY_TIME;

    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series series;
    private BarChart barChart;
    private String CURRENT_SORTING;
    private final String BACKGROUND_THEME = "-fx-background-color: #4B0082;",
            MAIN_THEME = "-fx-background-color: #777777;",
            DEFAULT_TEXT_COLOR = "-fx-text-fill: #bbbbbb;",
            TEXT_COLOR_BRIGHT_GREEN = "-fx-text-fill: #5e5e5e;",
            SELECTED_BARS_COLOR_FILL = "-fx-background-color: #02fa02;",
            BARS_DISABLE_COLOR = "-fx-background-color: #464646;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

        TOTAL_NUMBER_OF_BARS = (int) arraySizeSlider.getValue();
        arraySizeSliderLabel.setText(Integer.toString(TOTAL_NUMBER_OF_BARS));
        arraySizeSlider.valueProperty().addListener(e -> {
            TOTAL_NUMBER_OF_BARS = (int) arraySizeSlider.getValue();
            arraySizeSliderLabel.setText(Integer.toString(TOTAL_NUMBER_OF_BARS));
            randomize();
        });
        randomize();
        randomizeBtn.setOnMouseClicked(e -> randomize());

        DELAY_TIME = (int) delaySlider.getValue();
        delaySliderLabel.setText((int) delaySlider.getValue() + " ms");
        delaySlider.valueProperty().addListener(e -> {
            DELAY_TIME = (int) delaySlider.getValue();
            delaySliderLabel.setText((int) delaySlider.getValue() + " ms");
        });

        bubbleSortBtn.setOnMouseClicked(e -> bubbleSort());

        randomizeBtn.setTooltip(new Tooltip("Reset the array and Randomize with random values"));
        bubbleSortBtn.setTooltip(new Tooltip("\tWorst case\t\t\tBest Case\nTime Complexity: O(n²)\tTime Complexity: O(n)"));
    }

    private void delay() {
        try {
            Thread.sleep(DELAY_TIME);
        } catch (Exception e) {}
    }

    private void randomize() {
        series = new XYChart.Series();
        barChart = new BarChart(xAxis, yAxis);
        for (int i = 0; i < TOTAL_NUMBER_OF_BARS; i++) {
            series.getData().add(new XYChart.Data(Integer.toString(i), new Random().nextInt(TOTAL_NUMBER_OF_BARS) + 1));
        }
        barChart.getData().add(series);
        xAxis.setOpacity(0);
        yAxis.setOpacity(0);

        displayBorderPane.setCenter(barChart);

        for (int i = 0; i < TOTAL_NUMBER_OF_BARS; i++)
            ((XYChart.Data<Object, Object>) series.getData().get(i)).getNode().setStyle(MAIN_THEME);

        barChart.setBarGap(0);
        barChart.setCategoryGap(3);

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);
        barChart.setHorizontalZeroLineVisible(false);
        barChart.setVerticalZeroLineVisible(false);

        statusLabel.setText("UNSORTED");
        statusLabel.setStyle(DEFAULT_TEXT_COLOR);

        sortingAlgorithmLabel.setText("NONE");
        sortingAlgorithmLabel.setStyle(DEFAULT_TEXT_COLOR);
    }

    private void setAllDisable(boolean b) {
        bubbleSortBtn.setDisable(b);
        randomizeBtn.setDisable(b);
        arraySizeSlider.setDisable(b);
    }

    private void changeStyleEffect(int index, String style) {
        Platform.runLater(() -> {
            try {
                ((XYChart.Data) series.getData().get(index)).getNode().setStyle(style);
            } catch (Exception e) {}
        });
    }

    private void changeStyleEffect(int index, String style, String borderColor) {
        Platform.runLater(() -> {
            try {
                ((XYChart.Data) series.getData().get(index)).getNode().setStyle(style + borderColor);
            } catch (Exception e) {}
        });
    }

    private void changeStyleEffect(int index1, String style1, int index2, String style2) {
        Platform.runLater(() -> {
            try {
                ((XYChart.Data) series.getData().get(index1)).getNode().setStyle(style1);
                ((XYChart.Data) series.getData().get(index2)).getNode().setStyle(style2);
            } catch (Exception e) {}
        });
    }

    private void changeStyleEffect(int index1, String style1, String borderColor1, int index2, String style2, String borderColor2) {
        Platform.runLater(() -> {
            try {
                ((XYChart.Data) series.getData().get(index1)).getNode().setStyle(style1 + borderColor1);
                ((XYChart.Data) series.getData().get(index2)).getNode().setStyle(style2 + borderColor2);
            } catch (Exception e) {}
        });
    }

    private void barsDisableEffect() {
        for (int i = 0; i < TOTAL_NUMBER_OF_BARS; i++) {
            ((XYChart.Data) series.getData().get(i)).getNode().setStyle(BARS_DISABLE_COLOR);
        }
    }

    private void barsDisableEffect(int i, int j) {
        Platform.runLater(() -> {
            for (int x = i; x < j; x++) {
                ((XYChart.Data) series.getData().get(x)).getNode().setStyle(BARS_DISABLE_COLOR);
            }
        });
    }

    private void isArraySorted(boolean b) {
        Platform.runLater(() -> {
            if (b) {
                statusLabel.setText("SORTED");
                statusLabel.setStyle(TEXT_COLOR_BRIGHT_GREEN);
            } else {
                statusLabel.setText("SORTING...");
                statusLabel.setStyle(DEFAULT_TEXT_COLOR);
            }
        });
    }

    private void selectSortingAlgorithm() {
        Platform.runLater(() -> {
                    sortingAlgorithmLabel.setText(CURRENT_SORTING);
                    sortingAlgorithmLabel.setStyle(TEXT_COLOR_BRIGHT_GREEN);
                }
        );
    }

    // BUBBLE SORT
    private void bubbleSort() {
        setAllDisable(true);
        CURRENT_SORTING = "BUBBLE SORT";
        selectSortingAlgorithm();
        isArraySorted(false);
        new Thread(() -> {
            barsDisableEffect();
            boolean flag;
            for (int i = 0; i < TOTAL_NUMBER_OF_BARS - 1; i++) {
                flag = false;
                for (int j = 0; j < TOTAL_NUMBER_OF_BARS - i - 1; j++) {
                    changeStyleEffect(j, SELECTED_BARS_COLOR_FILL, j + 1, SELECTED_BARS_COLOR_FILL);
                    delay();
                    if ((int) ((XYChart.Data) series.getData().get(j)).getYValue() > (int) ((XYChart.Data) series.getData().get(j + 1)).getYValue()) {
                        CountDownLatch latch = new CountDownLatch(1);
                        int finalJ = j;
                        Platform.runLater(() -> {
                            ParallelTransition pt = swapAnimation(finalJ, finalJ + 1);
                            pt.setOnFinished(e -> latch.countDown());
                            pt.play();
                        });
                        try {
                            latch.await();
                        } catch (Exception e) {
                        }
                        flag = true;
                    }
                    changeStyleEffect(j, MAIN_THEME, j + 1, MAIN_THEME);
                }
                if (!flag) break;
            }
            setAllDisable(false);
            isArraySorted(true);
        }).start();
    }

    private ParallelTransition swapAnimation(int d1, int d2) {
        // get the precise location of the node in X axis
        double a1 = ((XYChart.Data) series.getData().get(d1)).getNode().getParent().localToParent(((XYChart.Data) series.getData().get(d1)).getNode().getBoundsInParent()).getMinX();
        double a2 = ((XYChart.Data) series.getData().get(d1)).getNode().getParent().localToParent(((XYChart.Data) series.getData().get(d2)).getNode().getBoundsInParent()).getMinX();

        // if any swap occur then we get the location of our node where it is swapped
        double translated1 = ((XYChart.Data) series.getData().get(d1)).getNode().getTranslateX();
        double translated2 = ((XYChart.Data) series.getData().get(d2)).getNode().getTranslateX();

        TranslateTransition t1 = new TranslateTransition(Duration.millis(DELAY_TIME), ((XYChart.Data) series.getData().get(d1)).getNode());
        t1.setByX(a2 - a1);
        TranslateTransition t2 = new TranslateTransition(Duration.millis(DELAY_TIME), ((XYChart.Data) series.getData().get(d2)).getNode());
        t2.setByX(a1 - a2);
        ParallelTransition pt = new ParallelTransition(t1, t2);
        // ParallelTransition will run t1 and t2 in parallel
        pt.statusProperty().addListener((e, old, curr) -> {
            if (old == Animation.Status.RUNNING) {
                ((XYChart.Data) series.getData().get(d2)).getNode().setTranslateX(translated1);
                ((XYChart.Data) series.getData().get(d1)).getNode().setTranslateX(translated2);

                int temp = (int) ((XYChart.Data) series.getData().get(d2)).getYValue();
                ((XYChart.Data) series.getData().get(d2)).setYValue(((XYChart.Data) series.getData().get(d1)).getYValue());
                ((XYChart.Data) series.getData().get(d1)).setYValue(temp);
            }
        });
        return pt;
    }
}