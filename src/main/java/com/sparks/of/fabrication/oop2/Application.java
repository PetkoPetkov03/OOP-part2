package com.sparks.of.fabrication.oop2;

import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import com.sparks.of.fabrication.oop2.utils.Env;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static final Env env = new Env();
    private static final EntityManagerWrapper entityManagerWrapper = new EntityManagerWrapper(env);
    private static final Logger log = LogManager.getLogger(Application.class);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("build.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private static void exit() {
        boolean exit = entityManagerWrapper.cleanUp();

        if(exit) {
            log.info("Application exited successfully");
            System.exit(0);
        }
        else {
            log.info("Application exited unsuccessfully");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch();
        exit();
    }
}