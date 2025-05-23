package models;

public class Ghe {
    @dbTable(columnName = "id", autoIncrement = true)
    private int id;
    @dbTable(columnName = "phong_chieu")
    private String phong_chieu;
    @dbTable(columnName = "suat_chieu")
    private int suat_chieu;
    @dbTable(columnName = "hang")
    private String hang;
    @dbTable(columnName = "cot")
    private int cot;
    @dbTable(columnName = "trang_thai")
    private int trang_thai;
    @dbTable(columnName = "ma_khach_hang")
    private Integer ma_khach_hang;

    public Ghe() {
    }
    public Ghe(int id, String phong_chieu, int suat_chieu, String hang, int cot, int trang_thai, Integer ma_khach_hang) {
        this.id = id;
        this.phong_chieu = phong_chieu;
        this.suat_chieu = suat_chieu;
        this.hang = hang;
        this.cot = cot;
        this.trang_thai = trang_thai;
        this.ma_khach_hang = ma_khach_hang;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhong_chieu() {
        return phong_chieu;
    }

    public void setPhong_chieu(String phong_chieu) {
        this.phong_chieu = phong_chieu;
    }

    public int getSuatChieu() {
        return suat_chieu;
    }

    public void setSuatChieu(int suat_chieu) {
        this.suat_chieu = suat_chieu;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public int getCot() {
        return cot;
    }

    public void setCot(int cot) {
        this.cot = cot;
    }

    public int getTrangThai() {
        return trang_thai;
    }

    public void setTrangThai(int trang_thai) {
        this.trang_thai = trang_thai;
    }

    public Integer getMaKhachHang() {
    return ma_khach_hang;
}

    public void setMaKhachHang(Integer ma_khach_hang) {
        this.ma_khach_hang = ma_khach_hang;
    }
}
