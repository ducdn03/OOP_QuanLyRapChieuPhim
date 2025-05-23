package com.mycompany.simple_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import models.Ghe;
import models.KhachHang;
import models.LichChieu;
import models.Phim;
import models.PhongChieu;
import models.VeXemPhim;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.DataReceiver;

public class customerBookingPageController implements DataReceiver<KhachHang> {
    @FXML
    private TabPane scheduleTabPane;

    // Tab "Lịch chiếu"
    @FXML
    private DatePicker dateSearch;
    @FXML
    private TableView<LichChieu> scheduleTable;
    @FXML
    private TableColumn<LichChieu, String> colMovie, colRoom, colTime, colCondition;
    @FXML
    private TableColumn<LichChieu, Integer> colRemainSeat;
    @FXML
    private TableColumn<LichChieu, Void> colAction;

    @FXML
    private GridPane seatGridPane;

    @FXML
    private Label bookedSeat;
    
    private static List<Ghe> gheDaChon = new ArrayList<>();
    private final ObservableList<LichChieu> data = FXCollections.observableArrayList();
    private static LichChieu lichChieuDaChon = new LichChieu();
    private static KhachHang khachHang;

    @Override
    public void setData(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void initialize() throws Exception {
        dateSearch.setValue(LocalDate.now());
        LocalDate selectedDate = dateSearch.getValue();
        data.clear();
        ConnectDatabase connect = new ConnectDatabase();
        List<LichChieu> scheduleList = connect.select("lichchieu", null, null, LichChieu.class);
        for (LichChieu schedule : scheduleList) {
            if (schedule.getNgayChieu().equals(selectedDate)) {
                data.add(schedule);
                scheduleTable.setItems(data);
                initializeTable();
            } else {
                scheduleTable.setPlaceholder(new Label("Không có lịch chiếu nào"));
            }
        }
    }

    @FXML
    private void handleSearch(MouseEvent event) throws Exception {
        LocalDate selectedDate = dateSearch.getValue();
        data.clear();
        ConnectDatabase connect = new ConnectDatabase();
        List<LichChieu> scheduleList = connect.select("lichchieu", null, null, LichChieu.class);
        for (LichChieu schedule : scheduleList) {
            if (schedule.getNgayChieu().equals(selectedDate)) {
                data.add(schedule);
                scheduleTable.setItems(data);
                initializeTable();
            } else {
                scheduleTable.setPlaceholder(new Label("Không có lịch chiếu nào"));
            }
        }
    }


    @FXML
    private void initializeTable() {
        colMovie.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("phongChieu"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("gioChieu"));
        colRemainSeat.setCellValueFactory(new PropertyValueFactory<>("gheTrong"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        scheduleTable.setItems(data);
        colAction.setCellFactory(param -> new TableCell<LichChieu, Void>() {
            private final Button bookingBtn = new Button("Đặt vé");
            private final HBox box = new HBox(5, bookingBtn);

            {
                // Style cho các nút
                bookingBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                // Xử lý sự kiện sửa
                bookingBtn.setOnAction(event -> {
                    LichChieu schedule = getTableView().getItems().get(getIndex());
                    if (schedule != null) {
                        scheduleTabPane.getSelectionModel().select(1);
                        try {
                            initializeSeatMatrix(schedule);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
        scheduleTable.setItems(data);
        scheduleTable.setPlaceholder(new Label("Không có lịch chiếu nào"));
    }

    @FXML
    private void initializeSeatMatrix(LichChieu schedule) throws Exception {
        lichChieuDaChon = schedule;
        seatGridPane.getChildren().clear(); // Xoá các ghế cũ

        String tableName = schedule.getPhongChieu().toLowerCase().replace(" ", "_") + "_" +
                schedule.getNgayChieu().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        List<Ghe> seatList = new ConnectDatabase().select(tableName, null, null, Ghe.class);
        List<Ghe> handleSeatList = new ArrayList<>();
        int suatchieu = 0;
        switch (schedule.getGioChieu()) {
            case "06:00 - 09:00":
                suatchieu = 1;
                break;
            case "09:00 - 12:00":
                suatchieu = 2;
                break;
            case "12:00 - 15:00":
                suatchieu = 3;
                break;
            case "15:00 - 18:00":
                suatchieu = 4;
                break;
            case "18:00 - 21:00":
                suatchieu = 5;
                break;
            case "21:00 - 24:00":
                suatchieu = 6;
                break;
            default:
                break;
        }
        for (Ghe seat : seatList) {
            if (seat.getSuatChieu() == suatchieu) {
                handleSeatList.add(seat);
            }
        }
        handleSeatGridPane();
        // Danh sách lưu ghế đã chọn
        List<Ghe> selectedSeats = new ArrayList<>();

        for (Ghe seat : handleSeatList) {
            String seatName = seat.getHang() + String.valueOf(seat.getCot());
            Button seatButton = new Button(seatName);
            seatButton.setPrefWidth(40);
            seatButton.setPrefHeight(40);

            // Gán màu theo trạng thái ghế
            if (seat.getTrangThai() == 1) {
            seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            seatButton.setDisable(true); // Ghế đã đặt thì disable
            seatButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Ghế " + seatName + " đã được đặt!");
                alert.showAndWait();
            });
            } else {
            seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            seatButton.setOnAction(e -> {
                if (selectedSeats.contains(seat)) {
                selectedSeats.remove(seat);
                seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                } else {
                selectedSeats.add(seat);
                seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                }

                // Hiển thị danh sách ghế đã chọn
                StringBuilder sb = new StringBuilder();
                for (Ghe g : selectedSeats) {
                sb.append(g.getHang()).append(g.getCot()).append(", ");
                }
                if (!selectedSeats.isEmpty()) {
                sb.setLength(sb.length() - 2); // Xoá dấu ", " cuối
                }
                bookedSeat.setText(sb.toString());
            });
            }

            // Thêm vào GridPane (hàng A-J -> 10 hàng, cột 1-15)
            int row = seat.getHang().charAt(0) - 'A'; // Chuyển đổi từ ký tự thành chỉ số hàng
            int col = seat.getCot() - 1;
            if (row >= 0 && row < 10 && col >= 0 && col < 15) { // Đảm bảo chỉ số hợp lệ
            seatGridPane.add(seatButton, col, row);
            }
        }

        // Lưu danh sách ghế được chọn để dùng cho nút Confirm
        this.gheDaChon = selectedSeats;

    }

    @FXML
    private void handleSeatGridPane() {
        // Xoá ràng buộc cũ nếu có
        seatGridPane.getColumnConstraints().clear();
        seatGridPane.getRowConstraints().clear();

        // Tạo 15 cột
        for (int i = 0; i < 15; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(40);
            col.setMinWidth(40);
            seatGridPane.getColumnConstraints().add(col);
        }

        // Tạo 10 hàng
        for (int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(40);
            row.setMinHeight(40);
            seatGridPane.getRowConstraints().add(row);
        }
        // Căn giữa GridPane
        seatGridPane.setStyle("-fx-alignment: center;");
    }

    @FXML
    private void handleBooking() throws Exception {
        if (gheDaChon.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ghế trước khi đặt vé!");
            alert.showAndWait();
        } else {
            // Lấy thông tin lịch chiếu
            LichChieu lichChieu = lichChieuDaChon;
            ConnectDatabase connect = new ConnectDatabase();
            String tableName = lichChieu.getPhongChieu().toLowerCase().replace(" ", "_") + "_" +
                lichChieu.getNgayChieu().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
            List<Ghe> seatList = connect.select(tableName, null, null, Ghe.class);
            // Cập nhật trạng thái ghế đã đặt
            for (Ghe seat : seatList) {
                for (Ghe selectedSeat : gheDaChon) {
                    if (seat.getHang().equals(selectedSeat.getHang()) && seat.getCot() == selectedSeat.getCot()) {
                        seat.setTrangThai(1);
                        seat.setMaKhachHang(khachHang.getMaKhachHang());
                        connect.update(tableName, seat);
                    }
                }
            }
            // Update Lịch chiếu
            lichChieu.setGheTrong(lichChieu.getGheTrong() - gheDaChon.size());
            connect.update("lichchieu", lichChieu);

            // Tạo vé xem phim
            VeXemPhim veXemPhim = new VeXemPhim();
            veXemPhim.setMaKhachHang(khachHang.getMaKhachHang());
            veXemPhim.setTenPhongChieu(lichChieu.getPhongChieu());
            veXemPhim.setThoiGianDat(LocalDate.now());
            StringBuilder sb = new StringBuilder();
            for (Ghe selectedSeat : gheDaChon) {
                sb.append(selectedSeat.getHang()).append(selectedSeat.getCot()).append(", ");
            }
            if (!gheDaChon.isEmpty()) {
                sb.setLength(sb.length() - 2); // Xoá dấu ", " cuối
            }
            veXemPhim.setViTriGhe(sb.toString());
            veXemPhim.setTenPhim(lichChieu.getTenPhim());
            veXemPhim.setNgayChieu(lichChieu.getNgayChieu());
            veXemPhim.setSuatChieu(lichChieu.getGioChieu());
            // Lưu vé vào cơ sở dữ liệu
            connect.insert("vexemphim", veXemPhim);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đặt vé thành công!");
            alert.showAndWait();
        }
    }
}
