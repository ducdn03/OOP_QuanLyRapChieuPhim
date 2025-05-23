package models;

import java.time.LocalDate;

public class LichChieu {

    @dbTable(columnName = "MaLichChieu", autoIncrement = true)
    private int maLichChieu;

    @dbTable(columnName = "TenPhim")
    private String tenPhim;

    @dbTable(columnName = "PhongChieu")
    private String phongChieu;

    @dbTable(columnName = "GioChieu")
    private String gioChieu;

    @dbTable(columnName = "NgayChieu")
    private LocalDate ngayChieu;

    @dbTable(columnName = "GheTrong")
    private int gheTrong;

    @dbTable(columnName = "TrangThai")
    private String trangThai;
    
    public LichChieu() {
    }
    // Constructor đầy đủ
    public LichChieu(int maLichChieu, String tenPhim, String phongChieu, String gioChieu,
                     LocalDate ngayChieu, int gheTrong, String trangThai) {
        super();
        this.maLichChieu = maLichChieu;
        this.tenPhim = tenPhim;
        this.phongChieu = phongChieu;
        this.gioChieu = gioChieu;
        this.ngayChieu = ngayChieu;
        this.gheTrong = gheTrong;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public int getMaLichChieu() {
        return maLichChieu;
    }

    public void setMaLichChieu(int maLichChieu) {
        this.maLichChieu = maLichChieu;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(String phongChieu) {
        this.phongChieu = phongChieu;
    }

    public String getGioChieu() {
        return gioChieu;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public int getGheTrong() {
        return gheTrong;
    }

    public void setGheTrong(int gheTrong) {
        this.gheTrong = gheTrong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

