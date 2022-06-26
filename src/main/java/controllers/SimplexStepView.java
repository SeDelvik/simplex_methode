package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Fraction;
import main.SimplexMethode;
import spravki.PreparationTableSpravka;
import spravki.TableViewSpravka;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SimplexStepView implements Initializable {
    @FXML
    Button back;
    @FXML
    Button inFirstPage;
    @FXML
    Button quickCount;
    @FXML
    Button nextStep;
    @FXML
    Label title;
    @FXML
    Pane forTable;

    SimplexMethode simplexMethode;
    Stage stage;
    Scene firstScene;
    Scene backScene;
    int step;

    public SimplexStepView(SimplexMethode simplexMethode, Stage stage, Scene firstScene, Scene backScene, int step) {
        this.simplexMethode = simplexMethode;
        this.stage = stage;
        this.firstScene = firstScene;
        this.backScene = backScene;
        this.step = step;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (simplexMethode.isArtBasis()) title.setText("Artificial basis: step " + step);
        else title.setText("Simplex methode: step " + step);
        /*GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 1; i <= simplexMethode.getSimplexFreeVariables().length; i++) {
            Label label = new Label("x" + simplexMethode.getSimplexFreeVariables()[i - 1]);
            gridPane.add(label, i, 0);
            GridPane.setMargin(label, new Insets(10));
        }
        for (int i = 1; i <= simplexMethode.getSimplexBasis().length; i++) {
            Label label = new Label("x" + simplexMethode.getSimplexBasis()[i - 1]);
            gridPane.add(label, 0, i);
            GridPane.setMargin(label, new Insets(10));
        }

        for (int i = 0; i < simplexMethode.getSimplexCore().length; i++) {
            for (int j = 0; j < simplexMethode.getSimplexCore()[0].length; j++) {
                if (*//*simplexMethode.getSimplexCore()[i][j].getNum()>0 &&*//* simplexMethode.isMinimalInCol(i, j) && j < simplexMethode.getSimplexCore()[0].length - 1 && i < simplexMethode.getSimplexCore().length - 1) {
                    Button button = new Button(simplexMethode.getSimplexCore()[i][j].toString());
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        newStep(finalI, finalJ);

                        System.out.println(simplexMethode.getSimplexCore()[finalI][finalJ]);
                    });

                    gridPane.add(button, j + 1, i + 1);
                    GridPane.setMargin(button, new Insets(10));
                } else {
                    Label label = new Label(simplexMethode.getSimplexCore()[i][j].toString());
                    gridPane.add(label, j + 1, i + 1);
                    GridPane.setMargin(label, new Insets(10));
                }
            }
        }
        forTable.getChildren().add(gridPane);*/

        createTable();

        back.setOnAction(event -> {
            stage.setScene(backScene);
        });
        inFirstPage.setOnAction(event -> {
            stage.setScene(firstScene);
        });
        nextStep.setOnAction(event -> {
            newStep(-1, -1);
        });
        quickCount.setOnAction(event -> {
            endOfcounting();
        });

    }

    public void newStep(int row, int col) {
        try {
            if (simplexMethode.isArtBasis()) {
                if (simplexMethode.isArtificiallyBasisDone()) {
                    if(artificallyBasisErr()){
                        createSolution(simplexMethode);
                    }
                    SimplexMethode simplexMethode1 = simplexMethode.switchToNormalSimplexMethode();
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/simplexStepView.fxml"));
                    SimplexStepView simplexStepView = new SimplexStepView(simplexMethode1, stage, firstScene, back.getScene()/*backScene*/, 0);
                    loader1.setController(simplexStepView);
                    Parent mainView1 = null;
                    try {
                        mainView1 = loader1.load();
                        Scene scene2 = new Scene(mainView1);
                        stage.setScene(scene2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    SimplexMethode simplexMethode1 = simplexMethode.artifficallyBasisNextStep(row, col);
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/simplexStepView.fxml"));
                    SimplexStepView simplexStepView = new SimplexStepView(simplexMethode1, stage, firstScene, back.getScene()/*backScene*/, step + 1);
                    loader1.setController(simplexStepView);
                    Parent mainView1 = null;
                    try {
                        mainView1 = loader1.load();
                        Scene scene2 = new Scene(mainView1);
                        stage.setScene(scene2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (simplexMethode.isDone()) {
                    System.out.println("конец");
                    createSolution(simplexMethode);
                } else {
                    SimplexMethode secondStep = simplexMethode.oneStep(row, col);
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/simplexStepView.fxml"));
                    SimplexStepView simplexStepView = new SimplexStepView(secondStep, stage, firstScene, back.getScene()/*backScene*/, step + 1);
                    loader1.setController(simplexStepView);
                    Parent mainView1 = null;
                    try {
                        mainView1 = loader1.load();
                        Scene scene2 = new Scene(mainView1);
                        stage.setScene(scene2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            createSolution(simplexMethode);
        }
    }

    public void endOfcounting() {
        if(simplexMethode.isArtBasis()){
            while(!simplexMethode.isArtificiallyBasisDone()){
                simplexMethode = simplexMethode.artifficallyBasisNextStep(-1,-1);
            }
            simplexMethode = simplexMethode.switchToNormalSimplexMethode();
        }

        while (!simplexMethode.isDone()) {
            simplexMethode = simplexMethode.oneStep(-1, -1);
        }
        createSolution(simplexMethode);
    }

    public void createSolution(SimplexMethode step) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/solutionView.fxml"));
        SolutionView solutionView = new SolutionView(step, stage, firstScene,title.getScene());
        loader1.setController(solutionView);
        Parent mainView1 = null;
        try {
            mainView1 = loader1.load();
            Scene scene2 = new Scene(mainView1);
            stage.setScene(scene2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(){
        forTable.getChildren().clear();
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        for (int i = 1; i <= simplexMethode.getSimplexFreeVariables().length; i++) {
            Label label = new Label("x" + simplexMethode.getSimplexFreeVariables()[i - 1]);
            gridPane.add(label, i, 0);
            GridPane.setMargin(label, new Insets(10));
        }
        for (int i = 1; i <= simplexMethode.getSimplexBasis().length; i++) {
            Label label = new Label("x" + simplexMethode.getSimplexBasis()[i - 1]);
            gridPane.add(label, 0, i);
            GridPane.setMargin(label, new Insets(10));
        }

        for (int i = 0; i < simplexMethode.getSimplexCore().length; i++) {
            for (int j = 0; j < simplexMethode.getSimplexCore()[0].length; j++) {
                if (/*simplexMethode.getSimplexCore()[i][j].getNum()>0 &&*/ simplexMethode.isMinimalInCol(i, j) && j < simplexMethode.getSimplexCore()[0].length - 1 && i < simplexMethode.getSimplexCore().length - 1) {
                    Button button = new Button(simplexMethode.getSimplexCore()[i][j].toString());
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(event -> {
                        newStep(finalI, finalJ);

                        System.out.println(simplexMethode.getSimplexCore()[finalI][finalJ]);
                    });

                    gridPane.add(button, j + 1, i + 1);
                    GridPane.setMargin(button, new Insets(10));
                } else {
                    Label label = new Label(simplexMethode.getSimplexCore()[i][j].toString());
                    gridPane.add(label, j + 1, i + 1);
                    GridPane.setMargin(label, new Insets(10));
                }
            }
        }
        forTable.getChildren().add(gridPane);
    }
    public void changeViewFraction(){
        Fraction.setSimpleFraction(!Fraction.getSimpleFraction());
        createTable();
    }
    public void getSpravka(){
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/spravki/TableViewSpravka.fxml"));
        //loader1.setController(graphicalMethode);
        TableViewSpravka tableViewSpravka = new TableViewSpravka();
        loader1.setController(tableViewSpravka);
        Parent mainView1 = null;
        Stage stage1 = new Stage();
        try {
            mainView1 = loader1.load();
            Scene scene2 = new Scene(mainView1);
            stage1.setScene(scene2);
            stage1.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean artificallyBasisErr(){
        int[] pos = simplexMethode.findArtificallyBasisPosition();
        if(pos[0]<0||pos[1]<0) return true;
        return false;
    }
}
