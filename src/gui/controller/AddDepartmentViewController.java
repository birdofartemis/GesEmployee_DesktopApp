package gui.controller;

import gui.model.db.SQLConnection;
import gui.model.entities.Department;
import gui.model.util.Alerts;
import gui.model.util.Constraints;
import gui.model.util.ViewOperations;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddDepartmentViewController implements Initializable {
    @FXML TextField txtDepartmentName;
    @FXML Button btnUpdate, btnClose;

    private Boolean edited = false;

    private Department newDepartment;

    private final ViewOperations vo = new ViewOperations();
    private SQLConnection connection;

    public void getConnection(SQLConnection connection){
        this.connection = connection;
    }

    public Boolean isEdited(){
        return  edited;
    }

    public void onBtnClose(){
        vo.closeView(btnClose);
    }

    public void onBtnUpdate(){
        if(!txtDepartmentName.getText().isBlank() && connection.insertDepartment(txtDepartmentName.getText())){

            newDepartment = new Department(connection.getLasIdtDepartment(), txtDepartmentName.getText());
            edited = true;
            onBtnClose();
        }
        else{
            Alerts.showAlert("Error", null, "Blank field detected", Alert.AlertType.ERROR);
        }
    }

    public Department returnDepartment(){
        return newDepartment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtDepartmentName, 50);
    }
}
