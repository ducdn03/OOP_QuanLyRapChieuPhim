package models;

public class NhanVien {

    @dbTable(columnName = "MaNhanVien", autoIncrement = true)
    private int maNhanVien;

    @dbTable(columnName = "MaTaiKhoan")
    private int maTaiKhoan;

    @dbTable(columnName = "HoTen")
    private String hoTen;

    @dbTable(columnName = "NgaySinh")
    private String ngaySinh;

    @dbTable(columnName = "DiaChi")
    private String diaChi;

    @dbTable(columnName = "GioiTinh")
    private String gioiTinh;

    @dbTable(columnName = "Email")
    private String email;

    @dbTable(columnName = "SoDienThoai")
    private String soDienThoai;

    @dbTable(columnName = "NgayVaoLam")
    private String ngayVaoLam;

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public NhanVien() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NhanVien(int maNhanVien, int maTaiKhoan, String hoTen, String ngaySinh, String diaChi, String gioiTinh,
            String email, String soDienThoai, String ngayVaoLam) {
        super();
        this.maNhanVien = maNhanVien;
        this.maTaiKhoan = maTaiKhoan;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

}
