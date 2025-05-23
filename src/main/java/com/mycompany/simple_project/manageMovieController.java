package com.mycompany.simple_project;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Phim;

public class manageMovieController {
    // Thanh tìm kiếm
    @FXML private TextField searchLineEdit;
    @FXML private ComboBox<String> typeSearchCombobox;
    @FXML private FontAwesomeIcon searchBtn;

    // Tab Danh sách phim
    @FXML private ImageView listMoviePoster;
    @FXML private Hyperlink listMovieTrailerLink;
    @FXML private Button listPreviousMovieBtn, listNextMovieBtn;
    
    // Các label thông tin phim
    @FXML private Label listMovieName, listMovieGenre, listMovieOrigin, 
            listMovieDate, listMovieDuration;
    @FXML private TextArea listMovieDescrip;
    @FXML private Button listUpdateMovieBtn;

    // Tab Sửa
    @FXML private TextField repairMovieOrigin, repairMovieDate, repairMovieDuration,
            repairMovieName, repairPosterTextEdit, repairTrailerTextEdit;
    @FXML private ComboBox<String> repairGenreCombobox;
    @FXML private Button repairPosterBtn, repairTrailerBtn, deleteMovieBtn, updateMovieBtn;
    @FXML private TextArea repairDescripTextArea;

    // Tab Tạo
    @FXML private TextField createMovieOrigin, createMovieDate, createMovieDuration,
            createMovieName, createPosterTextEdit, createTrailerTextEdit;
    @FXML private ComboBox<String> createGenreCombobox;
    @FXML private Button createPosterBtn, createTrailerBtn, createMovieBtn;
    @FXML private TextArea createDescripTextArea;
    @FXML private TabPane movieTabPane;
    
    private final HashMap<String, Integer> searchOptionMap = new HashMap<>();
    public static List<Phim> listPhim = new ArrayList<>();
    public static int currentIndex = 0;
    public static int currentListSize = 0;
    public static int currentPage = 0;

    @FXML
    public void initialize() throws Exception {
        // Khởi tạo list phim
        ConnectDatabase connect = new ConnectDatabase();
        listPhim = connect.select("phim", null, null, Phim.class);

        // Khởi tạo combobox
        searchOptionMap.clear();
        searchOptionMap.put("Tìm theo tên phim", 1);
        searchOptionMap.put("Tìm theo thể loại", 2);
        searchOptionMap.put("Tìm theo năm sản xuất", 3);
        searchOptionMap.put("Tìm theo quốc gia", 4);
        typeSearchCombobox.getItems().setAll(searchOptionMap.keySet());
        typeSearchCombobox.getSelectionModel().selectFirst();
        
        // Khởi tạo thể loại phim
        String[] genres = {"Hành động", "Lãng mạn", "Hài hước", "Kinh dị", "Viễn tưởng", "Hài", "Tâm Lý", "Hoạt hình"};
        repairGenreCombobox.getItems().addAll(genres);
        createGenreCombobox.getItems().addAll(genres);

        // Khởi tạo giao diện list phim
        if (listPhim.size() > 0) {
            Phim movie = listPhim.get(currentIndex);
            setMovieInfo(movie);
        }
    }

    // Xử lý sự kiện
    @FXML
    private void handleSearch() throws Exception {
        String query = searchLineEdit.getText();
        String selectedText = typeSearchCombobox.getSelectionModel().getSelectedItem();
        Integer selectedId = searchOptionMap.get(selectedText);
        ConnectDatabase connect = new ConnectDatabase();
        listPhim = connect.select("phim", null, null, Phim.class);
        listPhim = searchMovies(query, selectedId);
        currentListSize = listPhim.size();
        currentPage = 0;
        currentIndex = 0;
        if (currentListSize > 0) {
            Phim movie = listPhim.get(currentIndex);
            setMovieInfo(movie);
        } else {
            clearCreateFields();
            listMovieName.setText("Không tìm thấy phim nào.");
            listMovieGenre.setText("");
            listMovieOrigin.setText("");
            listMovieDate.setText("");
            listMovieDuration.setText("");
            listMovieDescrip.setText("");
            listMoviePoster.setImage(null);
            listMovieTrailerLink.setText("");
        }
    }

    private List<Phim> searchMovies(String query, int selectedId) {
        List<Phim> result = new ArrayList<>();
        for (Phim movie : listPhim) {
            switch (selectedId) {
                case 1:
                    if (movie.getTenPhim().toLowerCase().contains(query.toLowerCase())) {
                        result.add(movie);
                    }
                    break;
                case 2:
                    if (movie.getTheLoai().toLowerCase().contains(query.toLowerCase())) {
                        result.add(movie);
                    }
                    break;
                case 3:
                    if (String.valueOf(movie.getNamSanXuat()).toLowerCase().contains(query.toLowerCase())) {
                        result.add(movie);
                    }
                    break;
                case 4:
                    if (movie.getQuocGia().toLowerCase().contains(query.toLowerCase())) {
                        result.add(movie);
                    }
                    break;
            }
        }
        return result;
    }

    @FXML
    private void handleListPreviousMovie() {
        if (currentIndex > 0) {
            currentIndex--;
            Phim movie = listPhim.get(currentIndex);
            setMovieInfo(movie);
        }
    }

    @FXML
    private void handleListNextMovie() {
        if (currentIndex < listPhim.size() - 1) {
            currentIndex++;
            Phim movie = listPhim.get(currentIndex);
            setMovieInfo(movie);
        }
    }

    @FXML
    private void handleRepairPoster() {
        // Mở hộp thoại chọn ảnh
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            repairPosterTextEdit.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    private void handleRepairTrailer() {
        // Mở hộp thoại chọn video
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files",
                "*.mp4", "*.avi", "*.mov", "*.mkv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            repairTrailerTextEdit.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    private void handleCreatePoster() {
        // Mở hộp thoại chọn ảnh
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            createPosterTextEdit.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    private void handleCreateTrailer() {
        // Mở hộp thoại chọn video
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files",
                "*.mp4", "*.avi", "*.mov", "*.mkv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            createTrailerTextEdit.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    private void handleListUpdateMovie() {
        // Chuyển sang tab sửa phim
        movieTabPane.getSelectionModel().select(1);
        Phim movie = listPhim.get(currentIndex);
        setMovieInfo(movie);
    }

    @FXML
    private void setMovieInfo(Phim movie) {
        listMovieName.setText(movie.getTenPhim());
        listMovieGenre.setText(movie.getTheLoai());
        listMovieOrigin.setText(movie.getQuocGia());
        listMovieDate.setText(String.valueOf(movie.getNamSanXuat()));
        listMovieDuration.setText(String.valueOf(movie.getThoiLuong()));
        listMovieDescrip.setText(movie.getDescription());
        Image image = new Image(movie.getPoster());
        listMoviePoster.setImage(image);

        // Cập nhật các trường sửa phim
        repairMovieName.setText(movie.getTenPhim());
        repairMovieOrigin.setText(movie.getQuocGia());
        repairMovieDate.setText(String.valueOf(movie.getNamSanXuat()));
        repairMovieDuration.setText(String.valueOf(movie.getThoiLuong()));
        repairDescripTextArea.setText(movie.getDescription());
        repairPosterTextEdit.setText(movie.getPoster());
        repairTrailerTextEdit.setText(movie.getTrailer());
        repairGenreCombobox.getSelectionModel().select(movie.getTheLoai());
    }

    @FXML
    private void deleteMovie() {
        try {
            ConnectDatabase connect = new ConnectDatabase();
            Phim movie = listPhim.get(currentIndex);
            boolean result = connect.deleteById("phim", movie.getMaPhim(), "MaPhim", Phim.class);
            if (result) {
                listPhim.remove(currentIndex);
                currentListSize--;
                System.out.println("Xóa phim thành công.");
                if (currentIndex >= currentListSize) {
                    currentIndex = currentListSize - 1;
                }
                if (currentListSize > 0) {
                    Phim nextMovie = listPhim.get(currentIndex);
                    setMovieInfo(nextMovie);
                } else {
                    clearCreateFields();
                    listMovieName.setText("Không còn phim nào.");
                    listMovieGenre.setText("");
                    listMovieOrigin.setText("");
                    listMovieDate.setText("");
                    listMovieDuration.setText("");
                    listMovieDescrip.setText("");
                    listMoviePoster.setImage(null);
                    listMovieTrailerLink.setText("");
                }
            } else {
                System.out.println("Xóa phim không thành công.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMovie() {
        try {
            ConnectDatabase connect = new ConnectDatabase();
            Phim movie = listPhim.get(currentIndex);
            movie.setTenPhim(repairMovieName.getText());
            movie.setQuocGia(repairMovieOrigin.getText());
            movie.setNamSanXuat(Integer.parseInt(repairMovieDate.getText()));
            movie.setThoiLuong(Integer.parseInt(repairMovieDuration.getText()));
            movie.setDescription(repairDescripTextArea.getText());
            movie.setPoster(repairPosterTextEdit.getText());
            movie.setTrailer(repairTrailerTextEdit.getText());
            movie.setTheLoai(repairGenreCombobox.getSelectionModel().getSelectedItem());

            boolean result = connect.update("phim", movie);
            if (result) {
                System.out.println("Cập nhật phim thành công.");
                setMovieInfo(movie);
            } else {
                System.out.println("Cập nhật phim không thành công.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void createMovie() {
        try {
            ConnectDatabase connect = new ConnectDatabase();
            Phim movie = new Phim();
            movie.setTenPhim(createMovieName.getText());
            movie.setQuocGia(createMovieOrigin.getText());
            movie.setNamSanXuat(Integer.parseInt(createMovieDate.getText()));
            movie.setThoiLuong(Integer.parseInt(createMovieDuration.getText()));
            movie.setDescription(createDescripTextArea.getText());
            movie.setPoster(createPosterTextEdit.getText());
            movie.setTrailer(createTrailerTextEdit.getText());
            movie.setTheLoai(createGenreCombobox.getSelectionModel().getSelectedItem());

            boolean result = connect.insert("phim", movie);
            if (result) {
                System.out.println("Thêm phim thành công.");
                clearCreateFields();
            } else {
                System.out.println("Thêm phim không thành công.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Các phương thức hỗ trợ
    private void clearCreateFields() {
        createMovieName.clear();
        createGenreCombobox.getSelectionModel().clearSelection();
        createMovieOrigin.clear();
        createMovieDate.clear();
        createMovieDuration.clear();
        createPosterTextEdit.clear();
        createTrailerTextEdit.clear();
        createDescripTextArea.clear();
    }
}