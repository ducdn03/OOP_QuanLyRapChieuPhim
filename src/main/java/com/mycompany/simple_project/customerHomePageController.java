package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import models.Phim;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

public class customerHomePageController {

    @FXML
    private ImageView posterImage;

    @FXML
    private Label movieName;

    @FXML
    private Label movieGenre;

    @FXML
    private Label movieDuration;

    @FXML
    private Label movieDate;

    @FXML
    private Label movieOrigin;

    @FXML
    private HBox listPosterLayout;

    private static List<Phim> listPhim = new ArrayList<>();

    public void initialize() {
        ConnectDatabase connect = new ConnectDatabase();
        try {
            listPhim = connect.select("phim", null, null, Phim.class);

            for (Phim phim : listPhim) {
                Image image = new Image(phim.getPoster(), true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(80);
                imageView.setFitHeight(120);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setStyle("-fx-cursor: hand;");

                imageView.setOnMouseClicked((MouseEvent event) -> {
                    updateMovieInfo(phim);
                });

                listPosterLayout.getChildren().add(imageView);
            }

            if (!listPhim.isEmpty()) {
                updateMovieInfo(listPhim.get(0)); // Hiển thị phim đầu tiên mặc định
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMovieInfo(Phim phim) {
        movieName.setText(phim.getTenPhim());
        movieGenre.setText(phim.getTheLoai());
        movieDuration.setText(phim.getThoiLuong() + " phút");
        movieDate.setText(String.valueOf(phim.getNamSanXuat()));
        movieOrigin.setText(phim.getQuocGia());

        try {
            Image image = new Image(phim.getPoster(), true);
            posterImage.setImage(image);
        } catch (Exception e) {
            posterImage.setImage(null);
        }
    }
}
