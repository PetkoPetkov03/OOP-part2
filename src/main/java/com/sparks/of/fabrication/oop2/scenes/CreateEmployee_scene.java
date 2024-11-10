package com.sparks.of.fabrication.oop2.scenes;

import com.sparks.of.fabrication.oop2.Singleton;
import com.sparks.of.fabrication.oop2.utils.EntityManagerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.List;

public class CreateEmployee_scene {

    @FXML private TextField idEmployeeField;
    @FXML private TextField nameField;
    @FXML private ComboBox<?> roleComboBox;

    private EntityManagerWrapper entityManager = Singleton.getInstance(EntityManagerWrapper.class);


}
