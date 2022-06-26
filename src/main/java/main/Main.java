package main;

import controllers.PreparationSimplex;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("property");
        startScene(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void startScene(Stage stage) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 1000, 800);
        Button graphicalBtn = new Button("Graphica");
        Button simplexBtn = new Button("Simplex methode");

        graphicalBtn.setOnAction(event -> {
            /*leter*/
        });

        simplexBtn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/controllers/preparationSimplex.fxml"));
            PreparationSimplex preparationSimplex = new PreparationSimplex(stage, scene);
            loader.setController(preparationSimplex);
            try {
                Parent mainView = loader.load();
                Scene scene1 = new Scene(mainView);
                stage.setScene(scene1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //hBox.getChildren().add(graphicalBtn);
        hBox.getChildren().add(simplexBtn);


        stage.setScene(scene);
    }

}
