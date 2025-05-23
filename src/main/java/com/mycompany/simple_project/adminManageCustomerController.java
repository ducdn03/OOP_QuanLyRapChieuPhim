package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import models.KhachHang;
import models.TaiKhoan;

public class adminManageCustomerController {

    @FXML
    private TextField searchLineEdit;
    @FXML
    private ComboBox<String> typeSearchCombobox;
    @FXML
    private FontAwesomeIcon searchBtn;

    // Các thành phần Tab "Sửa"
    @FXML
    private TextField repairUserName;
    @FXML
    private TextField repairPassword;
    @FXML
    private TextField repairName;
    @FXML
    private TextField repairMail;
    @FXML
    private TextField repairPhone;

    // Các thành phần Tab "Tạo"
    @FXML
    private TextField createUserName;
    @FXML
    private TextField createPassword;
    @FXML
    private TextField createName;
    @FXML
    private TextField createMail;
    @FXML
    private TextField createPhone;

    @FXML
    public void initialize() {
        populateSearchTypes();
    }

    private final HashMap<String, Integer> searchOptionMap = new HashMap<>();

    public void populateSearchTypes() {
        searchOptionMap.clear();
        searchOptionMap.put("Tìm theo tên tài khoản", 1);
        searchOptionMap.put("Tìm theo tên", 2);
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
        if (selectedId == null || query.length() == 0) {
            System.out.println("ERROR");
        }
        switch (selectedId) {
            case 1: // Tìm bằng tên tài khoản
                TaiKhoan tk = findByUsername(query);
                if (tk != null) {
                    int userId = tk.getMaTaiKhoan();
                    KhachHang kh = findUser(String.valueOf(userId), "MaTaiKhoan");
                    if (kh != null) {
                        repairUserName.setText(query);
                        repairPassword.setText(tk.getMatKhau());
                        repairName.setText(kh.getHoTen());
                        repairMail.setText(kh.getEmail());
                        repairPhone.setText(kh.getSoDienThoai());
                    }
                } else {
                    deleteInTable();
                    System.out.println("Không tìm thấy tài khoản.");
                }
                break;

            case 2: // Tìm bằng email
                KhachHang khByEmail = findUser(query, "Email");
                if (khByEmail != null) {
                    TaiKhoan tkByEmail = findById(khByEmail.getMaTaiKhoan());
                    repairUserName.setText(tkByEmail.getTenDangNhap());
                    repairPassword.setText(tkByEmail.getMatKhau());
                    repairName.setText(khByEmail.getHoTen());
                    repairMail.setText(khByEmail.getEmail());
                    repairPhone.setText(khByEmail.getSoDienThoai());
                } else {
                    deleteInTable();
                    System.out.println("Không tìm thấy khách hàng với email này.");
                }
                break;

            case 3: // Tìm bằng số điện thoại
                KhachHang khByPhone = findUser(query, "SoDienThoai");
                if (khByPhone != null) {
                    TaiKhoan tkByPhone = findById(khByPhone.getMaTaiKhoan());
                    repairUserName.setText(tkByPhone.getTenDangNhap());
                    repairPassword.setText(tkByPhone.getMatKhau());
                    repairName.setText(khByPhone.getHoTen());
                    repairMail.setText(khByPhone.getEmail());
                    repairPhone.setText(khByPhone.getSoDienThoai());
                } else {
                    deleteInTable();
                    System.out.println("Không tìm thấy khách hàng với số điện thoại này.");
                }
                break;

            default:
                System.out.println("Loại tìm kiếm không hợp lệ.");
                break;
        }
    }
    
    @FXML
    public void deleteInTable() {
        repairUserName.setText("");
        repairPassword.setText("");
        repairName.setText("");
        repairMail.setText("");
        repairPhone.setText("");
        createUserName.setText("");
        createPassword.setText("");
        createName.setText("");
        createMail.setText("");
        createPhone.setText("");
    }

    public TaiKhoan findById(int id) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        try {
            List<TaiKhoan> listTK = connect.select("taikhoan", null, null, TaiKhoan.class);
            for (TaiKhoan x : listTK) {
                if (id == x.getMaTaiKhoan()) {
                    return x;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TaiKhoan findByUsername(String user) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        try {
            List<TaiKhoan> listTK = connect.select("taikhoan", null, null, TaiKhoan.class);
            for (TaiKhoan x : listTK) {
                if ((x.getTenDangNhap().equalsIgnoreCase(user))) {
                    return x;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhachHang findUser(String query, String column) throws Exception {
        ConnectDatabase connect = new ConnectDatabase();
        try {
            List<KhachHang> ListKH = connect.select("khachhang", column, query, KhachHang.class);
            if (ListKH.size() < 1) {
                return null;
            } else {
                return ListKH.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void deleteCustomer() {
        try {
            String username = repairUserName.getText();
            TaiKhoan tk = findByUsername(username);
            if (tk == null) {
                System.out.println("Không tìm thấy tài khoản để xoá.");
                return;
            }

            ConnectDatabase db = new ConnectDatabase();

            // Tìm khách hàng theo mã tài khoản
            KhachHang kh = findUser(String.valueOf(tk.getMaTaiKhoan()), "MaTaiKhoan");
            if (kh != null) {
                db.delete("khachhang", kh);
            }

            db.delete("taikhoan", tk);

            System.out.println("Xoá thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateCustomer() {
        try {
            String username = repairUserName.getText();

            TaiKhoan tk = findByUsername(username);
            if (tk == null) {
                System.out.println("Không tìm thấy tài khoản để cập nhật.");
                return;
            }

            // Cập nhật thông tin tài khoản
            tk.setMatKhau(repairPassword.getText());

            ConnectDatabase db = new ConnectDatabase();
            db.update("taikhoan", tk);

            // Cập nhật thông tin khách hàng
            KhachHang kh = findUser(String.valueOf(tk.getMaTaiKhoan()), "MaTaiKhoan");
            if (kh != null) {
                kh.setHoTen(repairName.getText());
                kh.setEmail(repairMail.getText());
                kh.setSoDienThoai(repairPhone.getText());

                db.update("khachhang", kh);
            }

            System.out.println("Cập nhật thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createCustomer() {
        try {
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(createUserName.getText());
            tk.setMatKhau(createPassword.getText());
            tk.setLoaiTaiKhoan("KH");

            ConnectDatabase db = new ConnectDatabase();

            boolean success = db.insert("taikhoan", tk);
            if (!success) {
                System.out.println("Tạo tài khoản thất bại.");
                return;
            }
            
            TaiKhoan newTK = findByUsername(createUserName.getText());

            // Sau khi insert, ID được tự động gán lại
            KhachHang kh = new KhachHang();
            kh.setMaTaiKhoan(newTK.getMaTaiKhoan());
            kh.setHoTen(createName.getText());
            kh.setEmail(createMail.getText());
            kh.setSoDienThoai(createPhone.getText());

            success = db.insert("khachhang", kh);
            if (!success) {
                System.out.println("Tạo khách hàng thất bại.");
                return;
            }

            System.out.println("Tạo tài khoản & khách hàng thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
