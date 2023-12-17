package com.example.crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class info {
    private int id;
    private String name;
    private String course;
    private String department;
    private String gsuite_acc;

    public info(int id, String name, String course, String department, String gsuite_acc) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.department = department;
        this.gsuite_acc = gsuite_acc;
    }

    public static ObservableList<info> getInfo() {
        ObservableList<info> infoList = FXCollections.observableArrayList();

        DBConnect connect = new DBConnect();
        Connection connection = connect.getConnection();

        if (connection != null) {
            String query = "SELECT stu_id, stu_name, stu_course, stu_dept, stu_gsuite FROM students";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("stu_id");
                    String name = resultSet.getString("stu_name");
                    String course = resultSet.getString("stu_course");
                    String department = resultSet.getString("stu_dept");
                    String acc = resultSet.getString("stu_gsuite");
                    infoList.add(new info(id, name, course, department, acc));
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("Failed to establish a database connection.");
        }
        return infoList;
    }

    // Getters for the attributes

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getDept() {
        return department;
    }

    public String getAccount() {
        return gsuite_acc;
    }
}
