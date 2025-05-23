package com.mycompany.simple_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

import interfaces.DataReceiver;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private static Stage stage; // Thêm biến tham chiếu đến Stage

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage; // Lưu lại reference đến Stage
        
        // Khởi tạo Scene với root null và kích thước mặc định
        scene = new Scene(loadFXML("loginForm"));
        
        stage.setScene(scene);
        stage.sizeToScene(); // Tự động điều chỉnh kích thước Stage theo root
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        
        // Cập nhật kích thước Stage theo root mới
        stage.sizeToScene();
        
        // Căn giữa màn hình nếu cần
        stage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static <T> void setRootWithData(String fxml, T data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        
        Object controller = fxmlLoader.getController();
        if (controller instanceof DataReceiver) {
            @SuppressWarnings("unchecked")
            DataReceiver<T> dataReceiver = (DataReceiver<T>) controller;
            dataReceiver.setData(data);
        }
        else {
            throw new IllegalArgumentException("Controller does not implement dataReceiver interface");
        }
        scene.setRoot(root);
        stage.sizeToScene(); // Tự động điều chỉnh kích thước Stage theo root
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}