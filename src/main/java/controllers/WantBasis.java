package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WantBasis implements Initializable {
    @FXML
    Button back;
    @FXML
    Button confirm;
    @FXML
    HBox forBasis;

    int maxVarCount;
    int varCount;
    private boolean conf;
    private ArrayList<CheckBox> checkBoxes;
    private int[] basis = new int[0];

    public WantBasis(int maxVarCount,int varCount){
        this.maxVarCount=maxVarCount;
        this.varCount = varCount;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conf = false;
        buttons();
        checkBoxes = new ArrayList<>();
        for(int i = 0;i<varCount;i++){
            VBox vBox = new VBox();
            Label label = new Label("x"+i);
            vBox.getChildren().add(label);
            CheckBox checkBox = new CheckBox();
            checkBoxes.add(checkBox);
            vBox.getChildren().add(checkBox);
            forBasis.getChildren().add(vBox);
        }

    }
    public void buttons(){
        back.setOnAction(event -> {
            Stage stage = (Stage) back.getScene().getWindow();
            // do what you have to do
            stage.close();
        });

        confirm.setOnAction(event -> {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for(int i = 0;i<checkBoxes.size();i++){
                if(checkBoxes.get(i).isSelected()) arrayList.add(i);
            }
            if(arrayList.size()>maxVarCount) return;

            basis = new int[arrayList.size()];
            for(int i=0;i<arrayList.size();i++){
                basis[i] = arrayList.get(i);
            }
            conf=true;
            Stage stage = (Stage) back.getScene().getWindow();
            // do what you have to do
            stage.close();


        });
    }
    public boolean isConfirm(){
        return conf;
    }
    public int[] getBasis(){
        return basis;
    }
}
