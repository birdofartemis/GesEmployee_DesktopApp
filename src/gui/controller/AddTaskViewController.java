package gui.controller;

import gui.model.db.SQLConnection;
import gui.model.entities.Employee;
import gui.model.util.Alerts;
import gui.model.util.Constraints;
import gui.model.util.ViewOperations;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskViewController implements Initializable {
    @FXML Label nameEmployee;
    @FXML TextField txtTask;
    @FXML Button btnClose, btnUpdate;

    private Boolean edited = false;

    private Employee editedEmployee;
    private SQLConnection connection;
    private final ViewOperations vo = new ViewOperations();

    public void getConnection(SQLConnection connection){
        this.connection = connection;
    }

    public Boolean isEdited(){
        return  edited;
    }

    public void getEmployee(Employee employee){
        this.editedEmployee = employee;
    }

    public void setNameEmployee(Employee employee){
        nameEmployee.setText(employee.getName());
    }

    public Employee getEditedEmployee(){
        return editedEmployee;
    }

    public void onBtnUpdate() {
        if (txtTask.getText().isBlank()) {
            txtTask.setText("None");
        }
            if (connection.updateTask(editedEmployee.getId(), txtTask.getText())) {
                editedEmployee.setTask(txtTask.getText());
                Alerts.showAlert("Success!", null, "Task was added", Alert.AlertType.INFORMATION, true);
                edited = true;
                onBtnClose();
                }
        else {
            Alerts.showAlert("Error", null, "Could not edit task", Alert.AlertType.ERROR);
        }
    }

    public void onBtnClose(){
        vo.closeView(btnClose);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtTask, 50);
    }
}
