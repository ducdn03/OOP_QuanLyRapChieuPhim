package com.mycompany.simple_project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import models.KhachHang;
import models.TaiKhoan;

public class loginFormController {
    @FXML private TextField signInUser;
    @FXML private PasswordField signInPass;
    @FXML private Label logInErrorMessage;
    @FXML private Label signUpErrorMessage;
    @FXML private Label signUp2ErrorMessage;
    @FXML private TextField signUpUsername;
    @FXML private PasswordField signUpPassword;
    @FXML private PasswordField signUpConfirmPassword;
    @FXML private TextField signUpName;
    @FXML private TextField signUpPhone;
    @FXML private TextField signUpEmail;
    
    
    @FXML
    private void signInLoginButtonPress() throws IOException {
        if (signInUser.getLength() == 0 || signInPass.getLength() == 0) {
            logInShowErrorMessage("Ten dang nhap hoac mat khau khong chinh xac");
            return;
        }

        String username = signInUser.getText();
        String password = signInPass.getText();
        String role = null;
        try {
            role = checkAccount(username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (role == null) {
            logInShowErrorMessage("Ten dang nhap hoac mat khau khong chinh xac");
            return;
        }
        if (role.equalsIgnoreCase("admin")) {
            App.setRoot("adminHome");            
        }
        else if (role.equalsIgnoreCase("kh")) {
            ConnectDatabase connectDatabase = new ConnectDatabase();
            try {
                TaiKhoan tk = null;
                List<TaiKhoan> listTK = connectDatabase.select("taikhoan", null, null, TaiKhoan.class);
                for (TaiKhoan x : listTK) {
                    if (x.getTenDangNhap().equalsIgnoreCase(username)) {
                        tk = x;
                        break;
                    }
                }
                if (tk != null) {
                    List<KhachHang> kh = connectDatabase.select("khachhang", null, null, KhachHang.class);
                    for (KhachHang x : kh) {
                        if (x.getMaTaiKhoan() == tk.getMaTaiKhoan()) {
                            App.setRootWithData("customerHome", x);
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if (role.equalsIgnoreCase("nv")) {
            App.setRoot("staffHome");
        }
        else {
            logInShowErrorMessage("Ten dang nhap hoac mat khau khong chinh xac");
            return;
        }
    }
    
    @FXML
    private void signUpSubmitButtonPress() throws Exception {
        if (signUpUsername.getLength() == 0 || signUpPassword.getLength() == 0 
                || signUpConfirmPassword.getLength() == 0){
            signUpShowErrorMessage("Yeu cau nhap day du thong tin!");
            return;
        }
        if (signUpPassword.getLength() < 8) {
            signUpShowErrorMessage("Mat khau can chua toi thieu 8 ki tu!");
            return;
        }
        if (!signUpPassword.getText().equals(signUpConfirmPassword.getText())) {
            signUpShowErrorMessage("Mat khau khong trung lap!");
            return;
        }
        String signUpUser = signUpUsername.getText();
        String signUpPass = signUpPassword.getText();
        if (isExistAccount(signUpUser) == true) {
            signUpShowErrorMessage("Da ton tai ten tai khoan. Vui long thay doi!");
            return;
        }
        ConnectDatabase connectDatabase = new ConnectDatabase();
     
        Connection conn = connectDatabase.connect();
        if (conn == null) {
            System.out.println("Can't connect to DB");
            throw new Exception("Can't connect to DB");
        }
        String query = "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, LoaiTaiKhoan)" +
                "VALUES (null, ?, ?, ?)";
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(query);
            pstm.setString(1, signUpUser);
            pstm.setString(2, signUpPass);
            pstm.setString(3, "KH");
            int row = pstm.executeUpdate();
            if (row != 0) {
                System.out.println("Them vao tai khoan thanh cong!");
                App.setRoot("signUpNameForm");
            }
                
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
            
    }
    
    @FXML
    private void signUpNameSubmitButtonPress() throws Exception {
        if (signUpName.getLength() == 0 || signUpPhone.getLength() == 0 || signUpEmail.getLength() == 0) {
            signUp2ShowErrorMessage("Yeu cau nhap day du thong tin!");
            return;
        }
        if (signUpPhone.getLength()!= 10) {
            signUp2ShowErrorMessage("So dien thoai khong hop le!");
            return;
        }
        String name = signUpName.getText();
        String phone = signUpPhone.getText();
        String email = signUpEmail.getText();
        if (!email.contains("@")) {
            signUp2ShowErrorMessage("Email khong hop le!");
            return;
        }
        ConnectDatabase connectdb = new ConnectDatabase();
        int maTaiKhoan = 0;
        try {
            TaiKhoan tk = connectdb.selectLastRow("taikhoan", "MaTaiKhoan", TaiKhoan.class);
            maTaiKhoan = tk.getMaTaiKhoan();
        } catch (SQLException e){
            e.printStackTrace();
        }
        KhachHang kh = new KhachHang(maTaiKhoan, name, email, phone);
        try {
            boolean res= connectdb.insert("khachhang", kh);
            if (!res) {
                throw new Exception("Can't insert to DB");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void signUpLinkPress() throws IOException {
        App.setRoot("signUpForm");
    }
    
    @FXML
    private void signInLinkPress() throws IOException {
        App.setRoot("loginForm");
    }
    
    @FXML
    private void signInLink2Press() throws IOException {
        App.setRoot("loginForm");
    }
    
    @FXML
    private void logInShowErrorMessage(String message) {
        logInErrorMessage.setText(message);
        logInErrorMessage.setVisible(true);
    }
    
    @FXML
    private void signUpShowErrorMessage (String message) {
        signUpErrorMessage.setText(message);
        signUpErrorMessage.setVisible(true);
    }
    
    @FXML
    private void signUp2ShowErrorMessage (String message) {
        signUp2ErrorMessage.setText(message);
        signUp2ErrorMessage.setVisible(true);
    }
    
    private boolean isExistAccount(String username) throws Exception {
        ConnectDatabase connectDatabase = new ConnectDatabase();
        try {
            List<TaiKhoan> listTK = connectDatabase.select("taikhoan", null, null, TaiKhoan.class);
            for (TaiKhoan x : listTK) {
                if (x.getTenDangNhap().equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private String checkAccount(String username, String passwrod) throws Exception {
        ConnectDatabase connectDatabase = new ConnectDatabase();
        try {
            List<TaiKhoan> listTK = connectDatabase.select("taikhoan", null, null, TaiKhoan.class);
            for (TaiKhoan x : listTK) {
                if ((x.getTenDangNhap().equalsIgnoreCase(username)) && (x.getMatKhau().equalsIgnoreCase(passwrod))) {
                    return x.getLoaiTaiKhoan();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
