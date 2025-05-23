package com.mycompany.simple_project;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.LichChieu;
import models.VeXemPhim;

public class adminHomePageController {

    @FXML private Label scheduleLabel, movieLabel, ticketLabel;
    @FXML private TableView<LichChieu> scheduleTable;
    @FXML private TableColumn<LichChieu, String> colMovie, colRoom, colTime, colCondition;
    @FXML private TableColumn<LichChieu, Integer> colRemainSeat;
    // Đã loại bỏ colAction vì không dùng nữa

    private final ObservableList<LichChieu> data = FXCollections.observableArrayList();

    public void initialize() {
        loadTodaySchedule();
        initializeTable();
        setSoLichChieu();
        setSoPhim();
        try {
            setSoVe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTodaySchedule() {
        data.clear();
        LocalDate today = LocalDate.now();

        try {
            ConnectDatabase connect = new ConnectDatabase();
            List<LichChieu> lichChieuList = connect.select("lichchieu", null, null, LichChieu.class);
            for (LichChieu lichChieu : lichChieuList) {
                if (today.equals(lichChieu.getNgayChieu())) {
                    data.add(lichChieu);
                    scheduleTable.setItems(data);
                    initializeTable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTable() {
        colMovie.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("phongChieu"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("gioChieu"));
        colRemainSeat.setCellValueFactory(new PropertyValueFactory<>("gheTrong"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        scheduleTable.setItems(data);
        scheduleTable.setPlaceholder(new Label("Không có lịch chiếu nào"));
    }

    @FXML
    private void setSoLichChieu() {
        int soLichChieu = data.size();
        scheduleLabel.setText(String.valueOf(soLichChieu));
    }

    @FXML
    private void setSoPhim() {
        int soPhim = 0;
        HashSet<String> uniqueMovies = new HashSet<>();
        for (LichChieu lichChieu : data) {
            uniqueMovies.add(lichChieu.getTenPhim());
        }
        soPhim = uniqueMovies.size();
        movieLabel.setText(String.valueOf(soPhim));
    }

    @FXML
    private void setSoVe() throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        List<VeXemPhim> veXemPhimList = connect.select("vexemphim", null, null, VeXemPhim.class);
        int soVe = 0;
        for (VeXemPhim veXemPhim : veXemPhimList) {
            if (veXemPhim.getThoiGianDat().equals(LocalDate.now())) {
                soVe++;
            }
        }
        ticketLabel.setText(String.valueOf(soVe));
    }
}
