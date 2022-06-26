package spravki;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PreparationTableSpravka {
    @FXML
    Button button;
    public PreparationTableSpravka(){}

    public void back(){
        Stage stage = (Stage) button.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
