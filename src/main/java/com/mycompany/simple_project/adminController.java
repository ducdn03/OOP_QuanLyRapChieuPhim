package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class adminController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        loadView("adminHomePage");
    }

    // Phương thức chung để load các view
    private void loadView(String fxmlFile) {
        try {
            if (contentArea == null) {
                throw new IllegalStateException("contentArea chưa được khởi tạo!");
            }
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile + ".fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            // Xử lý lỗi tại đây (ví dụ: hiển thị thông báo)
        }
    }

    @FXML
    private void handleMovies() {
        loadView("manageMovie");
    }

    @FXML
    private void handleHome() {
        loadView("adminHomePage");
    }

    @FXML
    private void handleCustomers() {
        loadView("adminManageCustomer");
    }

    @FXML
    private void handleStaff() {
        loadView("adminManageEmployer");
    }

    @FXML
    private void handleSchedule() {
        loadView("manageSchedule");
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("loginForm");
    }
}
