module com.masco.sortingvisualization {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    exports main;
    opens main to javafx.fxml;
    exports main.com.masco;
    opens main.com.masco to javafx.fxml;
}