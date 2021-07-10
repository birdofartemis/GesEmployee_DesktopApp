package gui.controller;


import gui.model.db.SQLConnection;
import gui.model.entities.Department;
import gui.model.entities.Employee;
import gui.model.util.Alerts;
import gui.model.util.Constraints;
import gui.model.util.ViewOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertEmployeeViewController implements Initializable {
    private ObservableList<Employee> employeesList;
    private ObservableList<Department> departmentsList;
    private final ViewOperations vo = new ViewOperations();
    private final SQLConnection connection = new SQLConnection("url");

    public void getEmployeesList(ObservableList<Employee> employeesData){
        employeesList = employeesData;
    }

    public void getDepartmentsList(ObservableList<Department> departmentsData){
        departmentsList = departmentsData;
    }

    public void setTvEmployee(){
        tvEmployee.setItems(employeesList);
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
    }

    public void setCbDepartment(){
        cbDepartment.setItems(departmentsList);
        cbDepartment.getItems().add(departmentsList.size(), new Department(
               null, "(Add new Department)"));
    }
    @FXML TableView<Employee> tvEmployee;
    @FXML TableColumn<Employee, String> tcName;
    @FXML TableColumn<Employee, Department> tcDepartment;

    @FXML TextField txtName, txtAge, txtRole, txtSalary;
    @FXML ComboBox<Department> cbDepartment;

    @FXML Button btnAddEmployee, btnRemoveEmployee, btnClose, btnBackPage;

    @FXML TextField txtSearch;

    private Boolean isEmployeeNull(){
        return (txtAge.getText().isBlank() || txtName.getText().isBlank() || txtSalary.getText().isBlank() ||
                txtRole.getText().isBlank() || cbDepartment.getValue() == null);
    }

    public void onTxtSearch(){
        if(txtSearch.getText().isBlank()){
            tvEmployee.setItems(employeesList);
        }
        else{
            ObservableList<Employee> filteredList;
            filteredList = FXCollections.observableArrayList();

            for(Employee emp : employeesList){
                if(emp.getName().toLowerCase().contains(txtSearch.getText().toLowerCase())){
                    filteredList.add(emp);
                }
            }
            tvEmployee.setItems(filteredList);
        }
    }

    public void onBtnAddEmployee(){
        if(!isEmployeeNull()) {
            Employee newEmployee = new Employee(null,
                    Integer.parseInt(txtAge.getText()),
                    Double.parseDouble(txtSalary.getText()),
                    txtName.getText(),
                    txtRole.getText(),
                    null,
                    cbDepartment.getValue());

            if (connection.insertEmployee(newEmployee)) {
                newEmployee.setId(connection.getLastIdEmployee());
                employeesList.add(newEmployee);
                tvEmployee.refresh();

                Alerts.showAlert("Success!", null, "Employee was added", Alert.AlertType.INFORMATION, true);
            }
        }
        else{
            Alerts.showAlert("Error", null, "All fields must be filled", Alert.AlertType.WARNING);
        }
    }
    public void onBtnRemoveEmployee(){
        Employee employeeDeleted = tvEmployee.getSelectionModel().getSelectedItem();

        if(employeeDeleted != null && connection.deleteEmployee(employeeDeleted.getId())){
            employeesList.remove(employeeDeleted);
            Alerts.showAlert("Success!", null, "Employee was deleted", Alert.AlertType.INFORMATION, true);
            tvEmployee.refresh();
        }
        else{
            Alerts.showAlert("Error", null, "Can't delete this employee", Alert.AlertType.ERROR);
        }
    }

    public void onClickAddNewDepartment() throws IOException {
        if(cbDepartment.getValue().getDepartmentName().equals("(Add new Department)")){
            //Prepare new view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/addDepartmentView.fxml"));
            Parent root = loader.load();

            AddDepartmentViewController controller = loader.getController();
            controller.getConnection(connection);
            //Throw new view
            vo.showView(root);

            if(controller.isEdited()) {
                Department newDepartment = controller.returnDepartment();

                if (!departmentsList.contains(newDepartment)) {
                    departmentsList.add(departmentsList.size() - 1, newDepartment);
                    cbDepartment.getItems().removeAll();
                    cbDepartment.setItems(departmentsList);
                    Alerts.showAlert("Success!", null, "Department was added", Alert.AlertType.INFORMATION, true);
                } else {
                    Alerts.showAlert("Error", null, "This department already exists", Alert.AlertType.ERROR);
                }
            }
        }
    }

    public void onBtnClose(){
        vo.closeView(btnClose);
    }

    public void onBtnBackPage(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/employeeListView.fxml"));
        Parent root = loader.load();
        //Throw new view
        vo.changeScene(root, e);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtName, 50);
        Constraints.setTextFieldMaxLength(txtRole, 50);
        Constraints.setTextFieldInteger(txtAge);
        Constraints.setTextFieldMaxLength(txtAge, 2);
        Constraints.setTextFieldDouble(txtSalary);
    }
}
