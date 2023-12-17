package com.example.crud;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class MainController implements Initializable {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private Button clear_butt;

    @FXML
    private Label account_name;

    @FXML
    private Label account_number;

    private String passRes;

    public void setPassRes(String passRes) {
        this.passRes = passRes;
    }

    public void displayInfo() {
        DBConnect connectNow = new DBConnect();
        Connection connectDB = connectNow.getConnection();

        try {
            String username = passRes;

            String query = "SELECT Name, U_code FROM account WHERE Username LIKE ?";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("Name");
                        String uCode = resultSet.getString("U_code");

                        account_name.setText(name);
                        account_number.setText(uCode);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableView<info> student_table;
    @FXML
    private TableColumn<info, Integer> id_col;
    @FXML
    private TableColumn<info, String> name_col;
    @FXML
    private TableColumn<info, String> course_col;
    @FXML
    private TableColumn<info, String> dept_col;
    @FXML
    private TableColumn<info, String> suite_col;
    @FXML
    private ObservableList<info> stu_list;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        maxNext();
        UpdateTable();
        search_user();

        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col.setCellValueFactory(new PropertyValueFactory<>("course"));
        dept_col.setCellValueFactory(new PropertyValueFactory<>("dept"));
        suite_col.setCellValueFactory(new PropertyValueFactory<>("account"));


        stu_list = info.getInfo();
        student_table.setItems(stu_list);

        student_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                add_id.setText(Integer.toString(newSelection.getId()));
                add_name.setText(newSelection.getName());
                add_course.setText(newSelection.getCourse());
                add_dept.setText(newSelection.getDept());
                add_suite.setText(newSelection.getAccount());
            } else{
                maxNext();
                add_name.setText("");
                add_course.setText("");
                add_dept.setText("");
                add_suite.setText("");
            }
        });
    }

    //add_student_start

    @FXML
    private TextField add_course;

    @FXML
    private TextField add_dept;

    @FXML
    private Label add_id;

    @FXML
    private TextField add_name;

    @FXML
    private TextField add_suite;

    @FXML
    private DBConnect connects = new DBConnect();

    @FXML
    private Connection con_pro = connects.getConnection();

    private void maxNext() {
        String query_sql = "SELECT MAX(stu_id) AS maxID FROM students";
        try {
            PreparedStatement autops = con_pro.prepareStatement(query_sql);
            ResultSet maxIdResult = autops.executeQuery();
            if (maxIdResult.next()) {
                int maxId = maxIdResult.getInt("maxID");
                int nextId = maxId + 1;
                add_id.setText(Integer.toString(nextId));
            } else {
                add_id.setText("1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent() {
        String insertSql = "INSERT INTO students(stu_name, stu_course, stu_dept, stu_gsuite) VALUES(?, ?, ?, ?)";
        if (add_name.getText() == null && add_course.getText() == null && add_dept.getText() == null && add_suite.getText() == null) {
            alert.setTitle("NOTICE");
            alert.setHeaderText("CRUDY");
            alert.setContentText("PLEASE FILL ALL INFORMATIONS");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                PreparedStatement ps = con_pro.prepareStatement(insertSql);
                ps.setString(1, add_name.getText());
                ps.setString(2, add_course.getText());
                ps.setString(3, add_dept.getText());
                ps.setString(4, add_suite.getText());

                int rowsAffected = ps.executeUpdate();

                alert.setTitle("NOTICE");
                alert.setHeaderText("CRUDY");
                if (rowsAffected > 0) {
                    alert.setContentText("SUCCESSFULLY ADDED THE STUDENT");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                    add_name.setText("");
                    add_course.setText("");
                    add_dept.setText("");
                    add_suite.setText("");
                    UpdateTable();
                    maxNext();
                    search_user();
                } else {
                    alert.setContentText("FAILED TO ADD THE STUDENT");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();

                    add_name.setText("");
                    add_course.setText("");
                    add_dept.setText("");
                    add_suite.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//add_student_end

    //edit_delete_update
    @FXML
    void getSelected() {
        int index = student_table.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            add_id.setText(id_col.getCellData(index).toString());
            add_name.setText(name_col.getCellData(index));
            add_course.setText(course_col.getCellData(index));
            add_dept.setText(dept_col.getCellData(index));
            add_suite.setText(suite_col.getCellData(index));
        }
    }


    @FXML
    public void Edit() {
        DBConnect connectNow = new DBConnect();
        Connection connectDB = connectNow.getConnection();

        try {
            String value1 = add_id.getText();
            String value2 = add_name.getText();
            String value3 = add_course.getText();
            String value4 = add_dept.getText();
            String value5 = add_suite.getText();

            String sql = "UPDATE students SET stu_name=?, stu_course=?, stu_dept=?, stu_gsuite=? WHERE stu_id=?";
            try (PreparedStatement pst = connectDB.prepareStatement(sql)) {
                pst.setString(1, value2);
                pst.setString(2, value3);
                pst.setString(3, value4);
                pst.setString(4, value5);
                pst.setString(5, value1);

                int rowsAffected = pst.executeUpdate();

                alert.setTitle("NOTICE");
                alert.setHeaderText("CRUDY");

                if (rowsAffected > 0) {
                    alert.setContentText("STUDENT UPDATED SUCCESSFULLY");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();

                    UpdateTable();
                    maxNext();
                    search_user();
                } else {
                    alert.setContentText("FAILED TO UPDATE THE STUDENT");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.setTitle("NOTICE");
            alert.setHeaderText("CRUDY");
            alert.setContentText("FAILED TO UPDATE THE STUDENT");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }


    public void Delete() {
        DBConnect connectNow = new DBConnect();
        Connection connectDB = connectNow.getConnection();

        String sql = "DELETE FROM students WHERE stu_id = ?";
        try {
            PreparedStatement pst = connectDB.prepareStatement(sql);
            pst.setString(1, add_id.getText());
            pst.executeUpdate();

            alert.setTitle("NOTICE");
            alert.setHeaderText("CRUDY");
            alert.setContentText("STUDENT DELETED SUCCESSFULLY");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();

            add_name.setText("");
            add_course.setText("");
            add_dept.setText("");
            add_suite.setText("");

            UpdateTable();
            maxNext();
            search_user();
        } catch (SQLException e) {
            alert.setTitle("NOTICE");
            alert.setHeaderText("CRUDY");
            alert.setContentText("FAILED TO DELETE THE STUDENT");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }



    public void UpdateTable(){
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col.setCellValueFactory(new PropertyValueFactory<>("course"));
        dept_col.setCellValueFactory(new PropertyValueFactory<>("dept"));
        suite_col.setCellValueFactory(new PropertyValueFactory<>("account"));

        stu_list = info.getInfo();
        student_table.setItems(stu_list);
    }

    //search
    ObservableList<info> dataList;

    @FXML
    private TextField search_bar;

    @FXML
    void search_user() {
        dataList = info.getInfo();

        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col.setCellValueFactory(new PropertyValueFactory<>("course"));
        dept_col.setCellValueFactory(new PropertyValueFactory<>("dept"));
        suite_col.setCellValueFactory(new PropertyValueFactory<>("account"));

        student_table.setItems(dataList);

        FilteredList<info> filteredData = new FilteredList<>(dataList, b -> true);

        search_bar.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(info -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            return info.getName().toLowerCase().contains(lowerCaseFilter)
                    || info.getCourse().toLowerCase().contains(lowerCaseFilter)
                    || info.getDept().toLowerCase().contains(lowerCaseFilter)
                    || info.getAccount().toLowerCase().contains(lowerCaseFilter);
        }));

        SortedList<info> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(student_table.comparatorProperty());

        student_table.setItems(sortedData);
        student_table.refresh();
    }

    @FXML
    void clear(ActionEvent e){
        clear_butt.addEventHandler(MouseEvent.MOUSE_CLICKED, EVENT ->{
            maxNext();
            search_user();
            add_name.clear();
            add_course.clear();
            add_dept.clear();
            add_suite.clear();
            search_bar.clear();
        });
    }
}
