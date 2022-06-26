package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Fraction;
import main.SimplexMethode;
import spravki.PreparationTableSpravka;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PreparationTableSimplex implements Initializable {
    @FXML
    HBox forCellFunk;
    @FXML
    Pane forGridPane;
    @FXML
    Button back;
    @FXML
    Button standartSimplex;
    @FXML
    Button artificalSimplex;
    @FXML
    Button save;
    @FXML
    Button load;

    Stage stage;
    Scene firstScene; /*начальный экран*/
    Scene backScene; /*возвращение к предыдущей сцене*/

    int countVariables;
    int countUravnenie;

    GridPane gridPane;

    ArrayList<TextField> cellFunkCoeffs;
    //ArrayList<ArrayList<TextField>> tableCoeffs;
    TextField[][] tableCoeffs;
    Fraction[] cellFunkSimplex;
    Fraction[][] tableSimplex;
    int[] basis = new int[0];


    public PreparationTableSimplex(int countUravnenie, int countVariables, Stage stage, Scene firstScene, Scene backScene) {
        this.stage = stage;
        this.firstScene = firstScene;
        this.backScene = backScene;
        this.countVariables = countVariables;
        this.countUravnenie = countUravnenie;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons();
        int size = 40;
        createCellFuncView(size);
        createTableView(size);

    }

    public void buttons() {

        back.setOnAction(event -> {
            stage.setScene(backScene);
        });

        standartSimplex.setOnAction(event -> {
            try {
                if (!createTable()) {
                    getAlert("неправильно заполнена таблица", "”кажите данные правильно");
                    return;
                }
                if (!createCellFunc()) {
                    getAlert("неправильно заполнена целева€ функци€", "”кажите данные правильно");
                    return;
                }
                countUravnenie = tableSimplex.length;
                countVariables = cellFunkSimplex.length - 1;

                Stage primaryStage = new Stage();
                primaryStage.setTitle("Basis");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/controllers/WantBasis.fxml"));
                WantBasis wantBasis = new WantBasis(countUravnenie, countVariables);
                loader.setController(wantBasis);
                Parent mainView = null;
                try {
                    mainView = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene scene1 = new Scene(mainView);
                primaryStage.setScene(scene1);
                primaryStage.showAndWait();
                /*успешно созданы вещи*/
                System.out.println(wantBasis.isConfirm());
                if (wantBasis.isConfirm()) {
                    basis = wantBasis.getBasis();
                    /*переход к первому шагу симплекс метода*/
                    SimplexMethode simplexMethode;

                    if (basis.length < 1) simplexMethode = new SimplexMethode(tableSimplex, cellFunkSimplex);
                    else simplexMethode = new SimplexMethode(tableSimplex, cellFunkSimplex, basis);

                    //System.out.println(simplexMethode);
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/simplexStepView.fxml"));
                    SimplexStepView simplexStepView = new SimplexStepView(simplexMethode, stage, firstScene, back.getScene()/*backScene*/, 0);

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
            } catch (Exception e) {
                getAlert("данные не совместны", "”кажите данные правильно");
                return;
            }
        });

        artificalSimplex.setOnAction(event -> {
            startArtBasis();
        });
        save.setOnAction(event -> {
            saveIn();
        });
        load.setOnAction(event -> {
            loadIn();
        });

    }

    public void startArtBasis() {
        if (!createTable()) {
            getAlert("неправильно заполнена таблица", "”кажите данные правильно");
            return;
        }
        if (!createCellFunc()) {
            getAlert("неправильно заполнена целева€ функци€", "”кажите данные правильно");
            return;
        }
        if (!(tableSimplex.length < tableSimplex[0].length - 1)) return;
        if (!isAllPol()) {
            getAlert("—вободные коэффициенты после = должны быть положительны", "”кажите данные правильно");
            return;
        }
        SimplexMethode simplexMethode = new SimplexMethode(tableSimplex, cellFunkSimplex, true);
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/simplexStepView.fxml"));
        SimplexStepView simplexStepView = new SimplexStepView(simplexMethode, stage, firstScene, back.getScene()/*backScene*/, 0);
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

    public void createCellFuncView(int size) {
        cellFunkCoeffs = new ArrayList<>();
        for (int i = 0; i < countVariables; i++) {
            TextField textField = new TextField("0");
            textField.setPrefWidth(size);
            cellFunkCoeffs.add(textField);
            forCellFunk.getChildren().add(textField);
            String string = "x" + i + "+";
            //if(i!=countVariables-1) string+="+";
            Label label = new Label(string);
            forCellFunk.getChildren().add(label);
        }
        TextField textField = new TextField("0");
        textField.setPrefWidth(size);
        forCellFunk.getChildren().add(textField);
        cellFunkCoeffs.add(textField);
        forCellFunk.getChildren().add(new Label(" ->min"));
    }

    public void createTableView(int size) {
        //tableCoeffs = new ArrayList<>();
        tableCoeffs = new TextField[countUravnenie][countVariables + 1];
        gridPane = new GridPane();
        for (int i = 0; i < countUravnenie; i++) {
            for (int j = 0; j < countVariables; j++) {
                HBox hBox = new HBox();
                TextField textField = new TextField("0");
                textField.setPrefWidth(size);
                tableCoeffs[i][j] = textField;
                String string = "x" + j;
                if (j != countVariables - 1) string += "+";
                Label label = new Label(string);
                hBox.getChildren().add(textField);
                hBox.getChildren().add(label);
                gridPane.add(hBox, j, i);
            }
            HBox hBox = new HBox();
            Label label = new Label("=");
            TextField textField = new TextField("0");
            textField.setPrefWidth(size);
            tableCoeffs[i][countVariables] = textField;
            hBox.getChildren().add(label);
            hBox.getChildren().add(textField);
            gridPane.add(hBox, countVariables, i);
        }
        forGridPane.getChildren().add(gridPane);

    }

    public boolean createCellFunc() {
        cellFunkSimplex = new Fraction[cellFunkCoeffs.size()];
        int zeroCount = 0;
        for (int i = 0; i < cellFunkCoeffs.size(); i++) {
            String value = cellFunkCoeffs.get(i).getText();
            try {
                Fraction fraction = getFraction(value);
                cellFunkSimplex[i] = fraction;
                if (fraction.getNum() == 0) zeroCount++;
            } catch (Exception e) {
                return false;
            }
        }
        if (zeroCount == cellFunkCoeffs.size()) return false;
        return true;
    }

    public boolean createTable() {
        tableSimplex = new Fraction[tableCoeffs.length][tableCoeffs[0].length];
        for (int i = 0; i < tableCoeffs.length; i++) {
            int zeroCount = 0;
            for (int j = 0; j < tableCoeffs[0].length; j++) {
                String value = tableCoeffs[i][j].getText();
                try {
                    Fraction fraction = getFraction(value);
                    tableSimplex[i][j] = fraction;
                    if (fraction.getNum() == 0) zeroCount++;
                } catch (Exception e) {
                    return false;
                }
            }
            if (zeroCount == tableCoeffs[0].length) return false;
        }
        return true;
    }

    public void saveIn() {
        if (!createTable()) {
            getAlert("неправильно заполнена таблица", "”кажите данные правильно");
            return;
        }
        if (!createCellFunc()) {
            getAlert("неправильно заполнена целева€ функци€", "”кажите данные правильно");
            return;
        }

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Basis");
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/saveFile.fxml"));
        SaveFile saveFile = new SaveFile();
        loader1.setController(saveFile);
        Parent mainView = null;
        try {
            mainView = loader1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene1 = new Scene(mainView);
        primaryStage.setScene(scene1);
        primaryStage.showAndWait();

        if (!saveFile.isConfirm()) return;
        String name = saveFile.getName();


        try (FileWriter writer = new FileWriter("./saves/" + name + ".simplexMethode", false)) {
            for (int i = 0; i < cellFunkSimplex.length; i++) {
                writer.append(cellFunkSimplex[i].toString());
                if (i != cellFunkSimplex.length - 1) writer.append(",");
            }
            writer.append("\n");
            for (int i = 0; i < tableSimplex.length; i++) {
                for (int j = 0; j < tableSimplex[0].length; j++) {
                    writer.append(tableSimplex[i][j].toString());
                    if (j != tableSimplex[0].length - 1) writer.append(",");
                }
                writer.append("\n");
            }

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

    public void loadIn() {
        Stage stageFileChooser = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Simplex Methode", "*.simplexMethode"));  // ExtensionFilter();
        fileChooser.setInitialDirectory(new File("./saves"));
        File file = fileChooser.showOpenDialog(stageFileChooser);
        if (file == null) return;

        String output = "";
        try (FileReader reader = new FileReader(file.getPath())) {
            int c;
            while ((c = reader.read()) != -1) {
                output += (char) c;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        createInFile(output);

    }

    public void createInFile(String output) {
        //System.out.println(output);
        String[] strArr = output.split("\n");
        Fraction[] loadCellFunk = new Fraction[strArr[0].split(",").length];
        Fraction[][] loadTable = new Fraction[strArr.length - 1][strArr[0].split(",").length];
        String[] cellFunkValues = strArr[0].split(",");
        for (int i = 0; i < cellFunkValues.length; i++) {
            if (cellFunkValues[i].matches("^.*/.*$")) {
                String[] tmp = cellFunkValues[i].split("/");
                loadCellFunk[i] = new Fraction(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]));
            } else {
                loadCellFunk[i] = new Fraction(Integer.valueOf(cellFunkValues[i]));
            }
        }
        for (int i = 0; i < loadTable.length; i++) {
            String[] values = strArr[i + 1].split(",");
            for (int j = 0; j < loadTable[0].length; j++) {
                if (values[j].matches("^.*/.*$")) {
                    String[] tmp = values[j].split("/");
                    loadTable[i][j] = new Fraction(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]));
                } else {
                    loadTable[i][j] = new Fraction(Integer.valueOf(values[j]));
                }
            }
        }
        cellFunkSimplex = loadCellFunk;
        tableSimplex = loadTable;

        reWriteView();


    }

    public void reWriteView() {
        //forGridPane.getChildren().add(new Label("test"));
        //forCellFunk.getChildren().clear();
        //gridPane.getChildren().clear();
        int size = 40;
        reWriteTable(size);
        reWriteCellfunk(size);
    }

    public void reWriteTable(int size) {
        gridPane.getChildren().clear();
        tableCoeffs = new TextField[tableSimplex.length][tableSimplex[0].length];
        for (int i = 0; i < tableSimplex.length; i++) {
            for (int j = 0; j < tableSimplex[0].length; j++) {
                HBox hbox = new HBox();
                TextField textField = new TextField(tableSimplex[i][j].toString());
                textField.setPrefWidth(size);
                tableCoeffs[i][j] = textField;
                hbox.getChildren().add(textField);
                gridPane.add(hbox, j, i);
                String str = "x" + j;
                if (j < tableSimplex[0].length - 2) str += "+";
                else if (j == tableSimplex[0].length - 2) str += "=";
                else str = "";
                Label label = new Label(str);
                hbox.getChildren().add(label);
            }
        }
    }

    public void reWriteCellfunk(int size) {
        forCellFunk.getChildren().clear();
        cellFunkCoeffs.clear();
        for (int i = 0; i < cellFunkSimplex.length; i++) {
            TextField textField = new TextField(cellFunkSimplex[i].toString());
            textField.setPrefWidth(size);
            cellFunkCoeffs.add(textField);
            forCellFunk.getChildren().add(textField);
            String str = "x" + i + "+";
            if (i == cellFunkSimplex.length - 1) str = "->min";
            Label label = new Label(str);
            forCellFunk.getChildren().add(label);
        }
    }

    public Fraction getFraction(String str) throws Exception {
        String value = str;
        int mines = 1;
        int numerator = 1;
        int denominator = 1;
        if (value.equals("")) throw new Exception();
        if (value.substring(0, 1).equals("-")) {
            value = value.substring(1, value.length());
            mines = -1;
        }
        if (!value.matches("(^[0-9]+/[0-9]+$)|(^[0-9]+$)|(^[0-9]+\\.[0-9]+$)|(^[0-9]+,[0-9]+$)")) throw new Exception();
        if (value.matches("(^[0-9]+/[0-9]+$)")) {
            String[] tmp = value.split("/");
            numerator = Integer.parseInt(tmp[0]);
            denominator = Integer.parseInt(tmp[1]);
        }
        if (value.matches("(^[0-9]+\\.[0-9]+$)")) {
            String[] tmp = value.split("\\.");
            numerator = Integer.parseInt(tmp[0]) * (int) Math.pow(10, tmp[1].length()) + Integer.parseInt(tmp[1]);
            denominator = (int) Math.pow(10, tmp[1].length());
        }
        if (value.matches("(^[0-9]+,[0-9]+$)")) {
            String[] tmp = value.split(",");
            numerator = Integer.parseInt(tmp[0]) * (int) Math.pow(10, tmp[1].length()) + Integer.parseInt(tmp[1]);
            denominator = (int) Math.pow(10, tmp[1].length());
        }
        if (value.matches("(^[0-9]+$)")) {
            numerator = Integer.parseInt(value);
        }
        if (denominator == 0) throw new Exception();
        return new Fraction(numerator, denominator).multiply(new Fraction(mines));

    }

    public boolean isAllPol() {
        for (int i = 0; i < tableSimplex.length; i++) {
            if (tableSimplex[i][tableSimplex[0].length - 1].getNum() < 0) return false;
        }
        return true;
    }

    public void createGraphicalMethode() {
        try {


            if (!createTable()) {
                getAlert("неправильно заполнена таблица", "”кажите данные правильно");
                return;
            }
            if (!createCellFunc()) {
                getAlert("неправильно заполнена целева€ функци€", "”кажите данные правильно");
                return;
            }
            SimplexMethode simplexMethode = new SimplexMethode(tableSimplex, cellFunkSimplex);
            while (!simplexMethode.isDone()) {
                simplexMethode = simplexMethode.oneStep(-1, -1);
            }
            if (simplexMethode.getSimplexFreeVariables().length != 2) {
                getAlert("кол-во свободных переменных != 2", "”кажите данные правильно");
                return;
            }

            boolean existSol = true;
            int count = 0;
            for (int i = 0; i < simplexMethode.getSimplexCore()[0].length - 1; i++) {
                if (simplexMethode.getSimplexCore()[simplexMethode.getSimplexCore().length - 1][i].getNum() < 0)
                    count++;
            }
            if (count > 0) {
                existSol = false;
            }
            ;

            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/controllers/graphicalMethode.fxml"));
            GraphicalMethode graphicalMethode = new GraphicalMethode(existSol, simplexMethode, stage, back.getScene(), firstScene);
            loader1.setController(graphicalMethode);
            Parent mainView1 = null;
            try {
                mainView1 = loader1.load();
                Scene scene2 = new Scene(mainView1);
                stage.setScene(scene2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            getAlert("данные не совместны", "”кажите данные правильно");
            return;
        }
    }

    public void getSpravka() {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/spravki/PreparationTableSpravka.fxml"));
        //loader1.setController(graphicalMethode);
        PreparationTableSpravka preparationTableSpravka = new PreparationTableSpravka();
        loader1.setController(preparationTableSpravka);
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

    public void getAlert(String head, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ќшибка");
        alert.setHeaderText(head);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
