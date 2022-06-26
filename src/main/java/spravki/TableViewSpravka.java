package spravki;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TableViewSpravka {
    @FXML
    Button button;


    public void back(){
        Stage stage = (Stage) button.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
