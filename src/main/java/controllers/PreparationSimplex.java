package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PreparationSimplex implements Initializable {
    @FXML
    Button back;
    @FXML
    Button nextSimplex;
    @FXML
    Spinner<Integer> sizeX;
    @FXML
    Spinner<Integer> sizeY;
    Stage stage;
    Scene firstScene;


    public PreparationSimplex(Stage stage, Scene scene) {
        this.stage = stage;
        this.firstScene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactoryX = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 1);
        SpinnerValueFactory<Integer> valueFactoryY = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 1);
        sizeX.setValueFactory(valueFactoryX);
        sizeY.setValueFactory(valueFactoryY);

        back.setOnAction(event -> {
            stage.setScene(firstScene);
        });

        nextSimplex.setOnAction(event -> {
            if (sizeX.getValue() <= sizeY.getValue()) {
                /*uslovie*/
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/controllers/preparationTableSimplex.fxml"));
                PreparationTableSimplex preparationTableSimplex = new PreparationTableSimplex(sizeX.getValue(), sizeY.getValue(), stage, firstScene, sizeX.getScene());
                //PreparationSimplex preparationSimplex = new PreparationSimplex(stage,scene);
                loader.setController(preparationTableSimplex);
                try {
                    Parent mainView = loader.load();
                    Scene scene1 = new Scene(mainView);
                    stage.setScene(scene1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Неправильно указаны размеры");
                alert.setContentText("Пожалуйста укажите размеры правильно");

                alert.showAndWait();
            }
        });

    }
}
