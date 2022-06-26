package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Fraction;
import main.SimplexMethode;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class SolutionView implements Initializable {
    @FXML
    Label points;
    @FXML
    Label functionSol;

    Stage stage;
    Scene scene;
    Scene backScene;
    SimplexMethode simplexMethode;


    public SolutionView(SimplexMethode simplexMethode, Stage stage, Scene scene, Scene backScene) {
        this.scene = scene;
        this.stage = stage;
        this.backScene = backScene;
        this.simplexMethode = simplexMethode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (existsSolution() && !simplexMethode.isArtBasis()) {
            points.setText(getPoints());
            functionSol.setText(getSolution());
        } else {
            points.setText("no solution");
            functionSol.setText("no solution");
        }

    }

    public String getPoints() {
        ArrayList<Integer> tmpBasis = new ArrayList<>();
        for (int i = 0; i < simplexMethode.getSimplexBasis().length; i++) {
            tmpBasis.add(simplexMethode.getSimplexBasis()[i]);
        }
        ArrayList<Fraction> tmpPoints = new ArrayList<>(Collections.nCopies(
                simplexMethode.getSimplexBasis().length + simplexMethode.getSimplexFreeVariables().length, new Fraction(0)));
        for (int i = 0; i < simplexMethode.getSimplexBasis().length; i++) {
            tmpPoints.set(simplexMethode.getSimplexBasis()[i], simplexMethode.getSimplexCore()[i][simplexMethode.getSimplexCore()[0].length - 1]);
        }
        String str = "(";
        for (Fraction fraction : tmpPoints) {
            str += fraction.toString() + ", ";
        }
        str = str.substring(0, str.length() - 2);
        str += ")";
        return str;
    }

    public String getSolution() {
        return simplexMethode.getSimplexCore()[simplexMethode.getSimplexCore().length - 1][simplexMethode.getSimplexCore()[0].length - 1].multiply(new Fraction(-1)).toString();
    }

    public boolean existsSolution() {
        int count = 0;
        for (int i = 0; i < simplexMethode.getSimplexCore()[0].length - 1; i++) {
            if (simplexMethode.getSimplexCore()[simplexMethode.getSimplexCore().length - 1][i].getNum() < 0) count++;
        }
        if (count > 0) return false;
        return true;
    }

    public void inFirstPage() {
        stage.setScene(scene);
    }

    public void backWindow() {
        stage.setScene(backScene);
    }


}
