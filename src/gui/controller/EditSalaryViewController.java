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

public class EditSalaryViewController implements Initializable {
    @FXML
    Label nameEmployee;
    @FXML
    TextField txtSalary;
    @FXML
    Button btnClose, btnUpdate;

    private Employee employee;
    private SQLConnection connection;
    private final ViewOperations vo = new ViewOperations();

    private Boolean edited = false;

    public Boolean isEdited(){
        return  edited;
    }

    public void getConnection(SQLConnection connection){
        this.connection = connection;
    }

    public void getEmployee(Employee employee){
        this.employee = employee;
    }

    public void setNameEmployee(Employee employee){
        nameEmployee.setText(employee.getName());
        txtSalary.setText(employee.getSalary().toString());
    }

    public Employee getEditedEmployee(){
        return employee;
    }

    public void onBtnUpdate(){
        if(!txtSalary.getText().isBlank()
                && connection.updateSalary(employee.getId(), Double.parseDouble(txtSalary.getText()))) {
            employee.setSalary(Double.parseDouble(txtSalary.getText()));

            Alerts.showAlert("Success!", null, "Salary was updated", Alert.AlertType.INFORMATION, true);
            edited = true;
            onBtnClose();
        }
        else{
            Alerts.showAlert("Error", null, "Could not edit task", Alert.AlertType.ERROR);
        }
    }

    public void onBtnClose(){
        vo.closeView(btnClose);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldDouble(txtSalary);
    }
}
