package gui.model.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
    public static void showAlert(String title, String header, String content, AlertType type, Boolean wait){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        if(wait){
            alert.showAndWait();
        }
        else{
            alert.show();
        }
    }

    public static void showAlert(String title, String header, String content, AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
