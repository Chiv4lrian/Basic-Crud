package com.example.crud;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;

@SuppressWarnings("ALL")
public class LoginForm {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private DBConnect connects = new DBConnect();

    @FXML
    private Connection con_pro = connects.getConnection();

    @FXML
    private Button register;

    @FXML
    private TextField add_code;

    @FXML
    private Pane pane_reg;

    @FXML
    private TextField add_id;

    @FXML
    private TextField add_name;

    @FXML
    private TextField add_pass;

    @FXML
    private TextField add_user;

    @FXML
    private Button back_to_login;

    @FXML
    private Button clear_acc;

    @FXML
    private Label notif;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField user;

    public void login_act(ActionEvent event) {
        if (user.getText().isBlank() && pass.getText().isBlank()) {
            notif.setVisible(true);
            notif.setText("Don't Leave it Blank");
        } else if (user.getText().isBlank() || pass.getText().isBlank()) {
            if (user.getText().isBlank()) {
                notif.setVisible(true);
                notif.setText("Enter Your Username");
            } else {
                notif.setVisible(true);
                notif.setText("Enter Your Password");
            }
        } else {
            try {
                if (validatelogin()) {
                    String passRes = user.getText();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("main_app.fxml"));
                    Parent root = loader.load();
                    MainController con2 = loader.getController();
                    con2.setPassRes(passRes);
                    con2.displayInfo();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(true);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validatelogin() {
        DBConnect connectNow = new DBConnect();
        Connection connectDB = connectNow.getConnection();

        String verifylogin = "SELECT count(1), U_code, Name, Username FROM account WHERE Username = '" + user.getText() + "' AND Password = '" + pass.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet query_result = statement.executeQuery(verifylogin);

            while (query_result.next()) {
                if (query_result.getInt(1) == 1) {
                    return true;
                } else {
                    MainApp.stage1.close();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addAccount() {
        String in_sql = "INSERT INTO account(id, U_code, Name, Username, Password) VALUES(?, ?, ?, ?, ?)";
        if (add_id.getText() == null && add_code.getText() == null && add_name.getText() == null && add_user.getText() == null && add_pass.getText() == null) {
            alert.setTitle("NOTICE");
            alert.setHeaderText("CRUDY");
            alert.setContentText("PLEASE FILL ALL INFORMATIONS");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                PreparedStatement ps = con_pro.prepareStatement(in_sql);
                ps.setString(1, add_id.getText());
                ps.setString(2, add_code.getText());
                ps.setString(3, add_name.getText());
                ps.setString(4, add_user.getText());
                ps.setString(5,add_pass.getText());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    alert.setTitle("NOTICE");
                    alert.setHeaderText("CRUDY");
                    alert.setContentText("SUCCESSFULLY ADDED THE ACCOUNT");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                    add_id.setText("");
                    add_code.setText("");
                    add_name.setText("");
                    add_user.setText("");
                    add_pass.setText("");
                } else {
                    alert.setTitle("NOTICE");
                    alert.setHeaderText("CRUDY");
                    alert.setContentText("FAILED TO ADD THE ACCOUNT");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();

                    add_id.setText("");
                    add_code.setText("");
                    add_name.setText("");
                    add_user.setText("");
                    add_pass.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void clear(ActionEvent e){
        clear_acc.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            add_id.setText("");
            add_code.setText("");
            add_name.setText("");
            add_user.setText("");
            add_pass.setText("");
        });
    }

    @FXML
    void back_back(ActionEvent e){
        back_to_login.addEventHandler(MouseEvent.MOUSE_CLICKED, EVENT ->{pane_reg.setVisible(false);});
        register.addEventHandler(MouseEvent.MOUSE_CLICKED, EVENT ->{pane_reg.setVisible(true);});
    }
}
