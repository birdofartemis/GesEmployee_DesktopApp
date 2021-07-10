package gui.controller;

import gui.model.db.SQLConnection;
import gui.model.entities.Department;
import gui.model.entities.Employee;
import gui.model.util.Alerts;
import gui.model.util.Constraints;
import gui.model.util.ViewOperations;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditEmployeeViewController implements Initializable {
    @FXML Label nameEmployee;
    @FXML TextField txtAge, txtRole;
    @FXML ComboBox<Department> cbDepartment;
    @FXML Button btnClose, btnUpdate;

    private Boolean edited = false;
    private ObservableList<Department> departments;

    private Employee employee;
    private SQLConnection connection;
    private final ViewOperations vo = new ViewOperations();

    private Boolean areFieldsBlank(){
        return (txtAge.getText().isBlank() || txtRole.getText().isBlank() || cbDepartment.getValue() == null);
    }

    public Boolean isEdited(){
        return  edited;
    }

    public void getEmployee(Employee employee){
        this.employee = employee;
    }

    public void getConnection(SQLConnection connection){
        this.connection = connection;
    }

    public void setNameEmployee(Employee employee){
        nameEmployee.setText(employee.getName());
        txtRole.setText(employee.getRole());
        txtAge.setText(employee.getAge().toString());
        cbDepartment.setValue(employee.getDepartment());
    }

    public void getDepartmentsList(ObservableList<Department> departments){
        this.departments = departments;

    }

    public void setDepartments(){
        cbDepartment.setItems(departments);
    }
    public Employee getEditedEmployee(){
        return employee;
    }

    public void onBtnUpdate(){
        if(!areFieldsBlank() && connection.updateEmployee(employee.getId(), Integer.parseInt(txtAge.getText()), txtRole.getText(), cbDepartment.getValue())) {

            employee.setAge(Integer.parseInt(txtAge.getText()));
            employee.setRole(txtRole.getText());
            employee.setDepartment(cbDepartment.getValue());
            Alerts.showAlert("Success!", null, "Employee was updated", Alert.AlertType.INFORMATION, true);
            edited = true;
            onBtnClose();

            } else {
                Alerts.showAlert("Error", null, "Could not edit employee", Alert.AlertType.ERROR);
            }
    }

    public void onBtnClose(){
        vo.closeView(btnClose);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtRole, 50);
        Constraints.setTextFieldInteger(txtAge);
        Constraints.setTextFieldMaxLength(txtAge, 2);

    }
}
