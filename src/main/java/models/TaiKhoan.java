package models;

public class TaiKhoan {

    @dbTable(columnName = "MaTaiKhoan", autoIncrement = true)
    private int maTaiKhoan;

    @dbTable(columnName = "TenDangNhap")
    private String tenDangNhap;

    @dbTable(columnName = "MatKhau")
    private String matKhau;

    @dbTable(columnName = "LoaiTaiKhoan")
    private String loaiTaiKhoan;

    public TaiKhoan() {
        super();
    }

    public TaiKhoan(int maTaiKhoan, String tenDangNhap, String matKhau, String loaiTaiKhoan) {
        super();
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(String loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }
}
