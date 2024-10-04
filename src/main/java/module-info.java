module com.sparks.of.fabrication.oop2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.sparks.of.fabrication.oop2 to javafx.fxml;
    exports com.sparks.of.fabrication.oop2;
}