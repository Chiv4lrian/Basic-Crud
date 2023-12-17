package com.example.crud;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static Stage stage1;
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setTitle("Crudyy");
            Image icon_app = new Image("JBFCF-f8098eac.png");
            stage.getIcons().add(icon_app);
            stage.setScene(scene);
            stage.show();
            stage1 = stage;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
