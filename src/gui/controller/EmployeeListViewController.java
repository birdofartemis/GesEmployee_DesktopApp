package gui.controller;

import gui.model.db.SQLConnection;
import gui.model.entities.Department;
import gui.model.entities.Employee;
import gui.model.util.ViewOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeListViewController implements Initializable {
    @FXML TableView<Employee> tvEmployees;
    @FXML TableColumn<Employee, String> tcName, tcRole, tcTask;
    @FXML TableColumn<Employee, Integer> tcAge;
    @FXML TableColumn<Employee, Double> tcSalary;
    @FXML TableColumn<Employee, Department> tcDepartment;
    @FXML Button btnClose, btnEditSalary, btnEditEmployee, btnAddTask, btnNextPage, btnBackPage;
    @FXML TextField txtSearch;

    private ObservableList<Employee> employees;
    private ObservableList<Department> departments;
    private SQLConnection connection;
    private final ViewOperations vo = new ViewOperations();

    private void showEmployeeList() {
        connection = new SQLConnection("url");
        ResultSet resultSet = connection.selectAllEmployeesQuery();

        try {
            while (resultSet.next()) {
                employees.add(
                        new Employee(
                                resultSet.getInt(1),
                                resultSet.getInt(3),
                                resultSet.getDouble(5),
                                resultSet.getString(2),
                                resultSet.getString(4),
                                resultSet.getString(6),
                                new Department(
                                        resultSet.getInt(7),
                                        resultSet.getString(8)
                                )
                        )
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setDepartmentList() {
        connection = new SQLConnection("url");
        ResultSet resultSetDep = connection.selectAllDepartmentsQuery();


        try {
            while (resultSetDep.next()) {
                departments.add(
                        new Department(
                                resultSetDep.getInt(1),
                                resultSetDep.getString(2)
                        )
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onClickTvEmployees() {
        btnEditSalary.setDisable(false);
        btnAddTask.setDisable(false);
        btnEditEmployee.setDisable(false);
    }

    public void onBtnClose() {
        vo.closeView(btnClose);
    }

    public void onBtnEditSalary() {
        try {
            Employee emp = tvEmployees.getSelectionModel().getSelectedItem();

            if(emp != null) {
                //Prepare new view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/editSalaryView.fxml"));
                Parent root = loader.load();

                //This line ables to access method from other controller
                EditSalaryViewController controller = loader.getController();
                controller.getConnection(connection);
                controller.getEmployee(emp);
                controller.setNameEmployee(emp);

                //Throw new view
                vo.showView(root);

                if (controller.isEdited()) {
                    Employee changed = controller.getEditedEmployee();
                    emp.setSalary(changed.getSalary());
                    tvEmployees.refresh();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBtnAddTask() {
        try {
            Employee emp = tvEmployees.getSelectionModel().getSelectedItem();

            if(emp != null) {
                //Prepare new view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/addTaskView.fxml"));
                Parent root = loader.load();

                //This line ables to access method from other controller
                AddTaskViewController controller = loader.getController();
                controller.getConnection(connection);
                controller.getEmployee(emp);
                controller.setNameEmployee(emp);

                //Throw new view
                vo.showView(root);

                if (controller.isEdited()) {
                    Employee changed = controller.getEditedEmployee();
                    emp.setTask(changed.getTask());
                    tvEmployees.refresh();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBtnEditEmployee() {
        try {
            Employee emp = tvEmployees.getSelectionModel().getSelectedItem();

            if(emp != null) {
                //Prepare new view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/editEmployeeView.fxml"));
                Parent root = loader.load();

                //This line ables to access method from other controller
                EditEmployeeViewController controller = loader.getController();
                controller.getConnection(connection);
                controller.getEmployee(emp);
                controller.getDepartmentsList(departments);
                controller.setNameEmployee(emp);
                controller.setDepartments();

                //Throw new view
                vo.showView(root);

                if (controller.isEdited()) {
                    Employee changed = controller.getEditedEmployee();
                    emp.setTask(changed.getTask());
                    tvEmployees.refresh();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTxtSearch(){
        if(txtSearch.getText().isBlank()){
            tvEmployees.setItems(employees);
        }
        else{
            ObservableList<Employee> filteredList;
            filteredList = FXCollections.observableArrayList();

            for(Employee emp : employees){
                if(emp.getName().toLowerCase().contains(txtSearch.getText().toLowerCase())){
                    filteredList.add(emp);
                }
            }
            tvEmployees.setItems(filteredList);
        }
    }
    public void onBtnBackPage(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/loginView.fxml"));

        vo.changeScene(root, e);

    }

    public void onBtnNextPagePage(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/insertEmployeeView.fxml"));
        Parent root = loader.load();

        InsertEmployeeViewController controller = loader.getController();
        controller.getEmployeesList(employees);
        controller.getDepartmentsList(departments);
        controller.setTvEmployee();
        controller.setCbDepartment();

        //Throw new view
        vo.changeScene(root, e);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAddTask.setDisable(true);
        btnEditEmployee.setDisable(true);
        btnEditSalary.setDisable(true);

        employees = FXCollections.observableArrayList();
        departments = FXCollections.observableArrayList();

        showEmployeeList();
        setDepartmentList();

        tvEmployees.setItems(employees);

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tcRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        tcSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tcTask.setCellValueFactory(new PropertyValueFactory<>("task"));
    }
}
