package models;

public class PhongChieu {
    @dbTable(columnName = "MaPhongChieu", autoIncrement = true)
    private int maPhongChieu;
    
    @dbTable(columnName = "TenPhongChieu")
    private String tenPhongChieu;
    
    @dbTable(columnName = "SoGhe")
    private int soGhe;
    
    private boolean[][] maTranGhe;

    public PhongChieu() {
        super();
    }

    public PhongChieu(int maPhongChieu, String tenPhongChieu, int soGhe, int soHang, int soCot) {
        this.maPhongChieu = maPhongChieu;
        this.tenPhongChieu = tenPhongChieu;
        this.soGhe = soGhe;
        this.maTranGhe = new boolean[soHang][soCot];
        // mặc định tất cả ghế chưa được đặt (false)
    }

    public int getMaPhongChieu() {
        return maPhongChieu;
    }

    public void setMaPhongChieu(int maPhongChieu) {
        this.maPhongChieu = maPhongChieu;
    }

    public String getTenPhongChieu() {
        return tenPhongChieu;
    }

    public void setTenPhongChieu(String tenPhongChieu) {
        this.tenPhongChieu = tenPhongChieu;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public boolean[][] getMaTranGhe() {
        return maTranGhe;
    }

    public void setMaTranGhe(boolean[][] maTranGhe) {
        this.maTranGhe = maTranGhe;
    }

    // Hàm kiểm tra trạng thái ghế
    public boolean isGheDaDat(int hang, int cot) {
        return maTranGhe[hang][cot];
    }

    // Hàm đặt ghế
    public void datGhe(int hang, int cot) {
        if (!maTranGhe[hang][cot]) {
            maTranGhe[hang][cot] = true;
        }
    }

    // Hàm hủy đặt ghế
    public void huyGhe(int hang, int cot) {
        if (maTranGhe[hang][cot]) {
            maTranGhe[hang][cot] = false;
        }
    }
}
