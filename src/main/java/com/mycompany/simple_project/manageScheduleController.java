package com.mycompany.simple_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.LichChieu;
import models.Phim;
import models.PhongChieu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class manageScheduleController {
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

    // Tab "Sửa"
    @FXML
    private ComboBox<String> repairMovie, repairRoom, repairTime, repairState;
    @FXML
    private TextField repairRemainSeat;
    @FXML
    private DatePicker repairDate;
    @FXML
    private Button deleteBtn, updateBtn;

    // Tab "Tạo"
    @FXML
    private ComboBox<String> createMovie, createRoom, createTime, createCondition;
    @FXML
    private TextField createRemainSeat;
    @FXML
    private DatePicker createDate;
    @FXML
    private Button createBtn;

    private final ObservableList<LichChieu> data = FXCollections.observableArrayList();
    private static List<LichChieu> lichChieuList = new ArrayList<>();
    private static List<Phim> phimList = new ArrayList<>();
    private static List<PhongChieu> roomList = new ArrayList<>();
    private static List<String> timeList = new ArrayList<>();
    private static List<String> conditionList = new ArrayList<>();

    @FXML
    private void handleSearch(MouseEvent event) throws Exception {
        data.clear();
        LocalDate selectedDate = dateSearch.getValue();
        if (selectedDate != null) {
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
    }

    public void initialize() {
    // Clear tất cả các combo box trước khi thêm dữ liệu mới
    createMovie.getItems().clear();
    createRoom.getItems().clear();
    createTime.getItems().clear();
    createCondition.getItems().clear();
    repairMovie.getItems().clear();
    repairRoom.getItems().clear();
    repairTime.getItems().clear();
    repairState.getItems().clear();

    // Khởi tạo kết nối với database
    ConnectDatabase connect = new ConnectDatabase();
    
    // Lấy danh sách lịch chiếu và thêm vào data
    try {
        lichChieuList = connect.select("lichchieu", null, null, LichChieu.class);
        data.addAll(lichChieuList);
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Lấy danh sách phim và thêm vào ComboBox
    try {
        phimList = connect.select("phim", null, null, Phim.class);
        for (Phim phim : phimList) {
            // Kiểm tra để tránh thêm trùng lặp
            if (!createMovie.getItems().contains(phim.getTenPhim())) {
                createMovie.getItems().add(phim.getTenPhim());
            }
            if (!repairMovie.getItems().contains(phim.getTenPhim())) {
                repairMovie.getItems().add(phim.getTenPhim());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Lấy danh sách phòng chiếu và thêm vào ComboBox
    try {
        roomList = connect.select("phongchieu", null, null, PhongChieu.class);
        for (PhongChieu room : roomList) {
            // Kiểm tra để tránh thêm trùng lặp
            if (!createRoom.getItems().contains(room.getTenPhongChieu())) {
                createRoom.getItems().add(room.getTenPhongChieu());
            }
            if (!repairRoom.getItems().contains(room.getTenPhongChieu())) {
                repairRoom.getItems().add(room.getTenPhongChieu());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Thêm thời gian vào ComboBox
    timeList.add("06:00 - 09:00");
    timeList.add("09:00 - 12:00");
    timeList.add("12:00 - 15:00");
    timeList.add("15:00 - 18:00");
    timeList.add("18:00 - 21:00");
    timeList.add("21:00 - 24:00");
    for (String time : timeList) {
        // Kiểm tra để tránh thêm trùng lặp
        if (!createTime.getItems().contains(time)) {
            createTime.getItems().add(time);
        }
        if (!repairTime.getItems().contains(time)) {
            repairTime.getItems().add(time);
        }
    }

    // Thêm trạng thái vào ComboBox
    conditionList.add("Đang chiếu");
    conditionList.add("Sắp chiếu");
    conditionList.add("Ngừng chiếu");
    for (String condition : conditionList) {
        // Kiểm tra để tránh thêm trùng lặp
        if (!createCondition.getItems().contains(condition)) {
            createCondition.getItems().add(condition);
        }
        if (!repairState.getItems().contains(condition)) {
            repairState.getItems().add(condition);
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
            private final Button editBtn = new Button("Sửa");
            private final Button deleteBtn = new Button("Xóa");
            private final HBox box = new HBox(5, editBtn, deleteBtn);

            {
                // Style cho các nút
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                // Xử lý sự kiện sửa
                editBtn.setOnAction(event -> {
                    LichChieu schedule = getTableView().getItems().get(getIndex());
                    if (schedule != null) {
                        scheduleTabPane.getSelectionModel().select(1);
                        repairMovie.setValue(schedule.getTenPhim());
                        repairRoom.setValue(schedule.getPhongChieu());
                        repairTime.setValue(schedule.getGioChieu());
                        repairRemainSeat.setText(String.valueOf(schedule.getGheTrong()));
                        repairState.setValue(schedule.getTrangThai());
                        repairDate.setValue(schedule.getNgayChieu());
                    }
                });

                // Xử lý sự kiện xóa
                deleteBtn.setOnAction(event -> {
                    LichChieu schedule = getTableView().getItems().get(getIndex());
                    if (schedule != null) {
                        scheduleTabPane.getSelectionModel().select(1);
                        repairMovie.setValue(schedule.getTenPhim());
                        repairRoom.setValue(schedule.getPhongChieu());
                        repairTime.setValue(schedule.getGioChieu());
                        repairRemainSeat.setText(String.valueOf(schedule.getGheTrong()));
                        repairState.setValue(schedule.getTrangThai());
                        repairDate.setValue(schedule.getNgayChieu());
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
private void handleCreate(MouseEvent event) {
        String movie = createMovie.getValue();
        String room = createRoom.getValue();
        String time = createTime.getValue();
        String condition = createCondition.getValue();
        String remainSeat = createRemainSeat.getText();
        LocalDate showDate = createDate.getValue();

        if (movie != null && room != null && time != null && condition != null && showDate != null) {
            LichChieu schedule = new LichChieu(0, movie, room, time, showDate, Integer.parseInt(remainSeat), condition);
            data.add(schedule);
            ConnectDatabase connect = new ConnectDatabase();
            try {
                if (isExistingSchedule(schedule)) {
                    System.out.println("Lịch chiếu đã tồn tại: " + movie);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                connect.insert("lichchieu", schedule);
                System.out.println("Đang tạo bảng ghế cho: " + room + " ngày: " + showDate);
                boolean check = connect.createSeatTableIfNotExists(room, showDate);
                if (check) {
                    System.out.println("Tạo bảng ghế thành công cho phòng: " + room);
                } else {
                    System.out.println("Bảng ghế đã tồn tại hoặc không thể tạo bảng ghế cho phòng: " + room);
                }
                System.out.println("Thêm lịch chiếu thành công: " + movie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
private void handleUpdate() {
        String movie = repairMovie.getValue();
        String room = repairRoom.getValue();
        String time = repairTime.getValue();
        String condition = repairState.getValue();
        String remainSeat = repairRemainSeat.getText();
        LocalDate showDate = repairDate.getValue();

        if (movie != null && room != null && time != null && condition != null && showDate != null) {
            LichChieu schedule = new LichChieu(0, movie, room, time, showDate, Integer.parseInt(remainSeat), condition);
            data.add(schedule);
            ConnectDatabase connect = new ConnectDatabase();
            try {
                connect.update("lichchieu", schedule);
                System.out.println("Cập nhật lịch chiếu thành công: " + movie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
private void handleDelete(MouseEvent event) {
        String movie = repairMovie.getValue();
        String room = repairRoom.getValue();
        String time = repairTime.getValue();
        String condition = repairState.getValue();
        String remainSeat = repairRemainSeat.getText();
        LocalDate showDate = repairDate.getValue();

        if (movie != null && room != null && time != null && condition != null && showDate != null) {
            LichChieu schedule = new LichChieu(0, movie, room, time, showDate, Integer.parseInt(remainSeat), condition);
            data.remove(schedule);
            ConnectDatabase connect = new ConnectDatabase();
            try {
                connect.delete("lichchieu", schedule);
                System.out.println("Xóa lịch chiếu thành công: " + movie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isExistingSchedule(LichChieu schedule) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        List

<LichChieu> listSchedule = connect.select("lichchieu", null, null, LichChieu.class  

);
        for (LichChieu existingSchedule : listSchedule) {
            if (existingSchedule.getTenPhim().equals(schedule.getTenPhim()) &&
                    existingSchedule.getPhongChieu().equals(schedule.getPhongChieu()) &&
                    existingSchedule.getGioChieu().equals(schedule.getGioChieu()) &&
                    existingSchedule.getNgayChieu().equals(schedule.getNgayChieu())) {
                return true;
            }
        }
        return false;
    }
}
