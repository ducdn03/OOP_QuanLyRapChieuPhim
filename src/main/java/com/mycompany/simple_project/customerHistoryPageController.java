package com.mycompany.simple_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.KhachHang;
import models.VeXemPhim;

import java.time.LocalDate;
import interfaces.DataReceiver;
import java.util.List;

public class customerHistoryPageController implements DataReceiver<KhachHang> {

    @FXML
    private TableView<VeXemPhim> scheduleTable;

    @FXML
    private TableColumn<VeXemPhim, String> colMovie;

    @FXML
    private TableColumn<VeXemPhim, String> colRoom;

    @FXML
    private TableColumn<VeXemPhim, LocalDate> colBookTime;

    @FXML
    private TableColumn<VeXemPhim, LocalDate> colDate;

    @FXML
    private TableColumn<VeXemPhim, String> colScheduleTime;

    @FXML
    private TableColumn<VeXemPhim, String> colSeat;

    private ObservableList<VeXemPhim> data;

    private static KhachHang khachHang;
    private List<VeXemPhim> veXemPhimList;

    @Override
    public void setData(KhachHang kh) {
        this.khachHang = kh;
        try {
            loadData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Gán property cho từng cột
        colMovie.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("tenPhongChieu"));
        colBookTime.setCellValueFactory(new PropertyValueFactory<>("thoiGianDat"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("ngayChieu"));
        colScheduleTime.setCellValueFactory(new PropertyValueFactory<>("suatChieu"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("viTriGhe"));

        // Khởi tạo danh sách
        data = FXCollections.observableArrayList();
    }

    private void loadData() throws Exception {
        if (khachHang == null) return; // tránh null pointer
        ConnectDatabase connect = new ConnectDatabase();
        veXemPhimList = connect.select("vexemphim", null, null, VeXemPhim.class);
        for (VeXemPhim ve : veXemPhimList) {
            if (ve.getMaKhachHang() == khachHang.getMaKhachHang()) {
                data.add(ve);
            }
        }
        scheduleTable.setItems(data);
    }
}
