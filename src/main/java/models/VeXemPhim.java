package models;

import java.time.LocalDate;

public class VeXemPhim {
    @dbTable(columnName = "MaVe", autoIncrement = true)
    private int maVe;

    @dbTable(columnName = "MaKhachHang")
    private int maKhachHang;

    @dbTable(columnName = "TenPhongChieu")
    private String tenPhongChieu;

    @dbTable(columnName = "ThoiGianDat")
    private LocalDate thoiGianDat;

    @dbTable(columnName = "ViTriGhe")
    private String viTriGhe;

    @dbTable(columnName = "TenPhim")
    private String tenPhim;

    @dbTable(columnName = "NgayChieu")
    private LocalDate ngayChieu;

    @dbTable(columnName = "SuatChieu")
    private String suatChieu;

    public VeXemPhim() {
    }

    public VeXemPhim(int maVe, int maKhachHang, String tenPhongChieu, LocalDate thoiGianDat,
                     String viTriGhe, String tenPhim, LocalDate ngayChieu, String suatChieu) {
        this.maVe = maVe;
        this.maKhachHang = maKhachHang;
        this.tenPhongChieu = tenPhongChieu;
        this.thoiGianDat = thoiGianDat;
        this.viTriGhe = viTriGhe;
        this.tenPhim = tenPhim;
        this.ngayChieu = ngayChieu;
        this.suatChieu = suatChieu;
    }

    public int getMaVe() {
        return maVe;
    }

    public void setMaVe(int maVe) {
        this.maVe = maVe;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenPhongChieu() {
        return tenPhongChieu;
    }

    public void setTenPhongChieu(String tenPhongChieu) {
        this.tenPhongChieu = tenPhongChieu;
    }

    public LocalDate getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(LocalDate thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public String getViTriGhe() {
        return viTriGhe;
    }

    public void setViTriGhe(String viTriGhe) {
        this.viTriGhe = viTriGhe;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(String suatChieu) {
        this.suatChieu = suatChieu;
    }
}
