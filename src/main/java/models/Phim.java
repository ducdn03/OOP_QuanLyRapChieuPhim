package models;

public class Phim {
    @dbTable(columnName = "MaPhim", autoIncrement = true)
    private int maPhim;

    @dbTable(columnName = "TenPhim")
    private String tenPhim;

    @dbTable(columnName = "ThoiLuong")
    private int thoiLuong;

    @dbTable(columnName = "TheLoai")
    private String theLoai;

    @dbTable(columnName = "QuocGia")
    private String quocGia;

    @dbTable(columnName = "NamSanXuat")
    private int namSanXuat;
    
    @dbTable(columnName = "poster")
    private String poster;
    
    @dbTable(columnName = "trailer")
    private String trailer;
    
    @dbTable(columnName = "descrip")
    private String descrip;
    
    public Phim() {
        super();
    }

    public Phim(int maPhim, String tenPhim, int thoiLuong, String theLoai, String quocGia
            , int namSanXuat, String poster, String trailer, String descrip) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.thoiLuong = thoiLuong;
        this.theLoai = theLoai;
        this.quocGia = quocGia;
        this.namSanXuat = namSanXuat;
        this.poster = poster;
        this.trailer = trailer;
        this.descrip = descrip;
    }

    public int getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(int maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }

    public int getNamSanXuat() {
        return namSanXuat;
    }

    public void setNamSanXuat(int namSanXuat) {
        this.namSanXuat = namSanXuat;
    }
    
    public String getPoster() {
        return poster;
    }
    
    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    public String getTrailer() {
        return trailer;
    }
    
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    } 
    
    public String getDescription() {
        return descrip;
    }
    
    public void setDescription(String descrip) {
        this.descrip =  descrip;
    }
}

