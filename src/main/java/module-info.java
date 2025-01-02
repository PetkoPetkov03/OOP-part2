module com.sparks.of.fabrication.oop2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.dotenv;
    requires org.apache.logging.log4j;
    requires jakarta.persistence;
    requires org.slf4j;
    requires annotations;
    requires static lombok;

    requires org.hibernate.orm.core;
    requires spring.security.crypto;
    requires spring.core;

    opens com.sparks.of.fabrication.oop2.models to org.hibernate.orm.core, javafx.base;

    opens com.sparks.of.fabrication.oop2 to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.arrivalGoods to javafx.fxml, javafx.base;
    opens com.sparks.of.fabrication.oop2.scenes.checkout to javafx.fxml, javafx.base;
    opens com.sparks.of.fabrication.oop2.scenes.invoices to javafx.fxml, javafx.base;
    opens com.sparks.of.fabrication.oop2.scenes.inventory to javafx.fxml, javafx.base;
    opens com.sparks.of.fabrication.oop2.scenes.employeeLogs to javafx.fxml, javafx.base;
    opens com.sparks.of.fabrication.oop2.scenes.nomenclature to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.createEmployee to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.ccheckout to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.notification to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.statistic to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.scenes.transaction to javafx.fxml;
    opens com.sparks.of.fabrication.oop2.utils to javafx.fxml;
    exports com.sparks.of.fabrication.oop2;
    exports com.sparks.of.fabrication.oop2.utils;

}