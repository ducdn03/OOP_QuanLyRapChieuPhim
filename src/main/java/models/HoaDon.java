package models;

public class HoaDon {

    @dbTable(columnName = "MaHoaDon")
    private String maHoaDon;

    @dbTable(columnName = "MaVe")
    private String maVe;

    @dbTable(columnName = "TongTien")
    private double tongTien;

    @dbTable(columnName = "NgayXuatHoaDon")
    private String ngayXuatHoaDon;

    public HoaDon() {
        super();
    }

    public HoaDon(String maHoaDon, String maVe, double tongTien, String ngayXuatHoaDon) {
        this.maHoaDon = maHoaDon;
        this.maVe = maVe;
        this.tongTien = tongTien;
        this.ngayXuatHoaDon = ngayXuatHoaDon;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayXuatHoaDon() {
        return ngayXuatHoaDon;
    }

    public void setNgayXuatHoaDon(String ngayXuatHoaDon) {
        this.ngayXuatHoaDon = ngayXuatHoaDon;
    }
}
