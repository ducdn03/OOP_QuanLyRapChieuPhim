package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

import java.util.HashMap;
import java.util.List;

import models.TaiKhoan;
import models.NhanVien;

public class adminManageStaffController {
    @FXML
    private TextField searchLineEdit;
    @FXML
    private ComboBox<String> typeSearchCombobox;
    @FXML
    private FontAwesomeIcon searchBtn;
    // FXML elements for "Sửa" tab (Edit)
    @FXML
    private TextField repairUserName;
    @FXML
    private TextField repairPassword;
    @FXML
    private TextField repairName;
    @FXML
    private TextField repairSex;
    @FXML
    private TextField repairBirth;
    @FXML
    private TextField repairAddress;
    @FXML
    private TextField repairMail;
    @FXML
    private TextField repairPhone;
    @FXML
    private TextField repairFirstJobDay;
    // FXML elements for "Tạo" tab (Create)
    @FXML
    private TextField createUserName;
    @FXML
    private TextField createPassword;
    @FXML
    private TextField createName;
    @FXML
    private TextField createSex;
    @FXML
    private TextField createBirth;
    @FXML
    private TextField createAddress;
    @FXML
    private TextField createMail;
    @FXML
    private TextField createPhone;
    @FXML
    private TextField createFirstJobDay;
    private final HashMap<String, Integer> searchOptionMap = new HashMap<>();

    @FXML
    public void initialize() {
        populateSearchTypes();
    }

    public void populateSearchTypes() {
        searchOptionMap.clear();
        searchOptionMap.put("Tìm theo tên tài khoản", 1);
        searchOptionMap.put("Tìm theo họ tên", 2);
        searchOptionMap.put("Tìm theo email", 3);
        searchOptionMap.put("Tìm theo số điện thoại", 4);
        typeSearchCombobox.getItems().setAll(searchOptionMap.keySet());
        typeSearchCombobox.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleSearch() throws Exception {
        String query = searchLineEdit.getText();
        String selectedText = typeSearchCombobox.getSelectionModel().getSelectedItem();
        Integer selectedId = searchOptionMap.get(selectedText);

        if (selectedId == null || query.isEmpty()) {
            System.out.println("Thông tin tìm kiếm không hợp lệ.");
            return;
        }

        switch (selectedId) {
            case 1:
                TaiKhoan tk = findByUsername(query);
                if (tk != null) {
                    NhanVien nv = findEmployeeByField("MaTaiKhoan", String.valueOf(tk.getMaTaiKhoan()));
                    if (nv != null) {
                        fillRepairForm(tk, nv);
                    }
                } else {
                    clearForm();
                    System.out.println("Không tìm thấy tài khoản.");
                }
                break;
            case 2:
                NhanVien nvHoTen = findEmployeeByField("HoTen", query);
                if (nvHoTen != null) {
                    TaiKhoan tkHoTen = findById(nvHoTen.getMaTaiKhoan());
                    fillRepairForm(tkHoTen, nvHoTen);
                }
                else {
                    clearForm();
                    System.out.println("Không tìm thấy nhân viên có họ tên này.");
                }
            case 3:
                NhanVien nvEmail = findEmployeeByField("Email", query);
                if (nvEmail != null) {
                    TaiKhoan tkEmail = findById(nvEmail.getMaTaiKhoan());
                    fillRepairForm(tkEmail, nvEmail);
                } else {
                    clearForm();
                    System.out.println("Không tìm thấy nhân viên với email này.");
                }
                break;
            case 4:
                NhanVien nvPhone = findEmployeeByField("SoDienThoai", query);
                if (nvPhone != null) {
                    TaiKhoan tkPhone = findById(nvPhone.getMaTaiKhoan());
                    fillRepairForm(tkPhone, nvPhone);
                } else {
                    clearForm();
                    System.out.println("Không tìm thấy nhân viên với số điện thoại này.");
                }
                break;
            default:
                System.out.println("Loại tìm kiếm không hợp lệ.");
        }
    }

    private void fillRepairForm(TaiKhoan tk, NhanVien nv) {
        repairUserName.setText(tk.getTenDangNhap());
        repairPassword.setText(tk.getMatKhau());
        repairName.setText(nv.getHoTen());
        repairMail.setText(nv.getEmail());
        repairPhone.setText(nv.getSoDienThoai());
        repairBirth.setText(nv.getNgaySinh());
        repairAddress.setText(nv.getDiaChi());
        repairFirstJobDay.setText(nv.getNgayVaoLam());
        repairSex.setText(nv.getGioiTinh());
    }

    @FXML
    public void clearForm() {
        repairUserName.setText("");
        repairPassword.setText("");
        repairName.setText("");
        repairMail.setText("");
        repairPhone.setText("");
        repairBirth.setText("");
        repairAddress.setText("");
        repairFirstJobDay.setText("");
        repairSex.setText("");
        createUserName.setText("");
        createPassword.setText("");
        createName.setText("");
        createMail.setText("");
        createPhone.setText("");
        createBirth.setText("");
        createAddress.setText("");
        createFirstJobDay.setText("");
        createSex.setText("");
    }

    public TaiKhoan findById(int id) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        List<TaiKhoan> accounts = connect.select("taikhoan", null, null, TaiKhoan.class);
        for (TaiKhoan tk : accounts) {
            if (tk.getMaTaiKhoan() == id) return tk;
        }
        return null;
    }

    public TaiKhoan findByUsername(String username) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        List<TaiKhoan> accounts = connect.select("taikhoan", null, null, TaiKhoan.class);
        for (TaiKhoan tk : accounts) {
            if (tk.getTenDangNhap().equalsIgnoreCase(username)) return tk;
        }
        return null;
    }

    public NhanVien findEmployeeByField(String column, String value) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        List<NhanVien> employees = connect.select("nhanvien", column, value, NhanVien.class);
        return employees.isEmpty() ? null : employees.get(0);
    }

    @FXML
    public void deleteEmployee() {
        try {
            TaiKhoan tk = findByUsername(repairUserName.getText());
            if (tk == null) {
                System.out.println("Không tìm thấy tài khoản.");
                return;
            }

            NhanVien nv = findEmployeeByField("MaTaiKhoan", String.valueOf(tk.getMaTaiKhoan()));
            ConnectDatabase db = new ConnectDatabase();

            if (nv != null) db.delete("nhanvien", nv);
            db.delete("taikhoan", tk);

            clearForm();
            System.out.println("Xoá thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateEmployee() {
        try {
            TaiKhoan tk = findByUsername(repairUserName.getText());
            if (tk == null) {
                System.out.println("Không tìm thấy tài khoản.");
                return;
            }

            tk.setMatKhau(repairPassword.getText());

            NhanVien nv = findEmployeeByField("MaTaiKhoan", String.valueOf(tk.getMaTaiKhoan()));
            if (nv != null) {
                nv.setHoTen(repairName.getText());
                nv.setEmail(repairMail.getText());
                nv.setSoDienThoai(repairPhone.getText());
                nv.setDiaChi(repairAddress.getText());
                nv.setGioiTinh(repairSex.getText());
                nv.setNgaySinh(repairBirth.getText());
                nv.setNgayVaoLam(repairFirstJobDay.getText());

                ConnectDatabase db = new ConnectDatabase();
                db.update("taikhoan", tk);
                db.update("nhanvien", nv);

                System.out.println("Cập nhật thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createEmployee() {
        try {
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(createUserName.getText());
            tk.setMatKhau(createPassword.getText());
            tk.setLoaiTaiKhoan("NV");

            ConnectDatabase db = new ConnectDatabase();
            if (!db.insert("taikhoan", tk)) {
                System.out.println("Tạo tài khoản thất bại.");
                return;
            }

            TaiKhoan newTK = findByUsername(createUserName.getText());
            NhanVien nv = new NhanVien();
            nv.setMaTaiKhoan(newTK.getMaTaiKhoan());
            nv.setHoTen(createName.getText());
            nv.setEmail(createMail.getText());
            nv.setSoDienThoai(createPhone.getText());
            nv.setDiaChi(createAddress.getText());
            nv.setNgaySinh(createBirth.getText());
            nv.setNgayVaoLam(createFirstJobDay.getText());
            nv.setGioiTinh(createSex.getText());

            if (!db.insert("nhanvien", nv)) {
                System.out.println("Tạo nhân viên thất bại.");
                return;
            }

            clearForm();
            System.out.println("Tạo nhân viên thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
