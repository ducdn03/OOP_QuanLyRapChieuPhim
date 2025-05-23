package models;

public class KhachHang {

	@dbTable(columnName = "MaKhachHang", autoIncrement = true)
	private int maKhachHang;
	
	@dbTable(columnName = "MaTaiKhoan")
	private int maTaiKhoan;
	
	@dbTable(columnName = "HoTen")
	private String hoTen;
	
	@dbTable(columnName = "Email")
	private String email;
	
	@dbTable(columnName = "SoDienThoai")
	private String soDienThoai;

	public KhachHang() {
		super();
	}

	public KhachHang(int maTaiKhoan, String hoTen, String email, String soDienThoai) {
		super();
		this.maTaiKhoan = maTaiKhoan;
		this.hoTen = hoTen;
		this.email = email;
		this.soDienThoai = soDienThoai;
	}

	public int getMaKhachHang() {
		return maKhachHang;
	}

	public void setMaKhachHang(int maKhachHang) {
		this.maKhachHang = maKhachHang;
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
}