package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import interfaces.DataReceiver;
import models.KhachHang;

public class customerController implements DataReceiver<KhachHang> {
    private static KhachHang khachHang;
    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        loadView("customerHomePage");
    }

    @Override    
    public void setData(KhachHang kh){
        this.khachHang = kh;
        System.out.println("Khach hang: " + khachHang.getHoTen());
    }

    // Phương thức chung để load các view
    private void loadView(String fxmlFile) {
        try {
            if (contentArea == null) {
                throw new IllegalStateException("contentArea chưa được khởi tạo!");
            }
            // Sửa đoạn này trong loadView():
            Node view = FXMLLoader.load(getClass().getResource("/com/mycompany/simple_project/" + fxmlFile + ".fxml"));
            contentArea.getChildren().setAll(view);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            // Xử lý lỗi tại đây (ví dụ: hiển thị thông báo)
        }
    }

    private <T> void loadViewWithData(String fxmlFile, T data) {
    try {
        if (contentArea == null) {
            throw new IllegalStateException("contentArea chưa được khởi tạo!");
        }

        String fullPath = "/com/mycompany/simple_project/" + fxmlFile + ".fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fullPath));
        Node view = loader.load();

        Object controller = loader.getController();
        if (controller instanceof DataReceiver<?>) {
            @SuppressWarnings("unchecked")
            DataReceiver<T> dataReceiver = (DataReceiver<T>) controller;
            dataReceiver.setData(data);
            System.out.println("Data set successfully: " + data);
        }
        contentArea.getChildren().setAll(view);
    } catch (IOException | IllegalStateException e) {
        System.out.println("loi");
        e.printStackTrace();
    }
}


    @FXML
    private void handleBookSeat() {
        loadViewWithData("customerBookingPage", khachHang);
    }

    @FXML
    private void handleHome() {
        loadView("customerHomePage");
    }

    @FXML void handleChangeInfo() {
        loadViewWithData("customerChangeInfo", khachHang);
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("loginForm");
    }

    @FXML
    private void handleHistory() {
        loadViewWithData("customerHistoryPage", khachHang);
    }
}