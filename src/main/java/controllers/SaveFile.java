package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveFile {
    @FXML
    TextField fileName;

    private boolean conf;
    private String name;

    public SaveFile(){
        conf = false;
    }
    public void back(){
        Stage stage = (Stage) fileName.getScene().getWindow();
        stage.close();
    }

    public void confirm(){
        if(!fileName.getText().equals("")){
            conf = true;
            name = fileName.getText();
            back();
        }
    }

    public boolean isConfirm(){
        return conf;
    }

    public String getName(){
        return name;
    }
}
