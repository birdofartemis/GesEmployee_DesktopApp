package gui.controller;

import gui.model.db.SQLConnection;
import gui.model.entities.User;
import gui.model.util.Alerts;
import gui.model.util.Constraints;
import gui.model.util.ViewOperations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    private final SQLConnection connection = new SQLConnection("url1");

    @FXML TextField txtUserName;
    @FXML PasswordField pfUserPassword;
    @FXML Button btnLogin;

    private Boolean isBlankLogin(){
        return (txtUserName.getText().isBlank() || pfUserPassword.getText().isBlank());
    }


    public void onBtnLogin(ActionEvent e) throws IOException {
        if(!isBlankLogin() && connection.loginQuery(new User(txtUserName.getText(), pfUserPassword.getText()))) {
            ViewOperations vo = new ViewOperations();

            Parent root = FXMLLoader.load(getClass().getResource("../resources/view/employeeListView.fxml"));
            vo.changeScene(root, e);
            }
            else{
                Alerts.showAlert("Error", null, "Incorrect username or password", Alert.AlertType.ERROR);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtUserName, 50);
        Constraints.setTextFieldMaxLength(pfUserPassword, 50);
    }
}
