package gui.model.db;

import gui.model.entities.Department;
import gui.model.entities.Employee;
import gui.model.entities.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SQLConnection {
    private Connection connection;

    public SQLConnection(String url) {
        System.out.println("Loading...");
        Properties properties = new Properties();
        try(InputStream inputStream = new FileInputStream("dataDB.properties")){
            properties.load(inputStream);

            connection = DriverManager.getConnection(properties.getProperty(url),
                    properties.getProperty("username"), properties.getProperty("password"));

            System.out.println("Connected to Data Base!");

        } catch (IOException e) {
            System.out.println("Error! Could not find file dataDB.properties");
        } catch (SQLException e) {
            System.out.println("Error! Could not establish connection to Data Base");
        }
    }

    public Boolean loginQuery(User user){
        try{
            PreparedStatement ps;

            ps = connection.prepareStatement("SELECT * FROM userdata " +
                    "WHERE userName = ? AND userPassword = ?");

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserPassword());

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet selectAllEmployeesQuery(){
        try{
            Statement statement = connection.createStatement();

            return statement.executeQuery(
                    "SELECT employeedata.id, employeedata.Name, employeedata.age, employeedata.role, " +
                            "employeedata.salary, employeedata.task, departmentdata.id, departmentdata.departmentName " +
                            "FROM employeedata " +
                            "INNER JOIN departmentdata " +
                            "ON employeedata.departmentId = departmentdata.id\n" +
                            "ORDER BY employeedata.id");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectAllDepartmentsQuery(){
        try{
            Statement statement = connection.createStatement();

            return statement.executeQuery("SELECT * FROM departmentdata");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean updateTask(Integer id, String task){
        String sql = "UPDATE employeedata " +
                "SET task = ?\n" +
                "WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, task);
            ps.setInt(2, id);

            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean updateSalary(Integer id, Double salary){
        String sql = "UPDATE employeedata\n" +
                "SET salary = ? " +
                "WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    public Boolean updateEmployee(Integer id, Integer age, String role, Department dep){
        String sql = "UPDATE employeedata " +
                "SET age = ?, role = ?, departmentId = ?\n" +
                "WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, age);
            ps.setString(2, role);
            ps.setInt(3, dep.getId());
            ps.setInt(4, id);

            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean insertEmployee(Employee newEmployee){
        String sql = "INSERT INTO employeedata(Name, age, role, salary, departmentId, task) VALUES\n" +
                "(?, ?, ?, ?, ?, null)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newEmployee.getName());
            ps.setInt(2, newEmployee.getAge());
            ps.setString(3, newEmployee.getRole());
            ps.setDouble(4, newEmployee.getSalary());
            ps.setInt(5, newEmployee.getDepartment().getId());

            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Boolean deleteEmployee(Integer id){
        String sql = "DELETE FROM employeedata\n" +
                "WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public Boolean insertDepartment(String name){
        String sql = "INSERT INTO departmentdata(departmentName) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);

            int lines = ps.executeUpdate();

            return (lines == 1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public Integer getLastIdEmployee(){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs;

            rs = statement.executeQuery("SELECT * FROM employeedata " +
                    "ORDER BY employeedata.id DESC\n" +
                    "LIMIT 1");
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Integer getLasIdtDepartment(){
        try{
            Statement statement = connection.createStatement();
            ResultSet rs;

            rs = statement.executeQuery("SELECT * FROM departmentdata " +
                    "ORDER BY departmentdata.id DESC\n" +
                    "LIMIT 1");
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
