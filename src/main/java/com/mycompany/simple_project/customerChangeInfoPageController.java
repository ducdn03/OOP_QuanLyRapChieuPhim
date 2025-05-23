package com.mycompany.simple_project;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.KhachHang;
import models.TaiKhoan;

import java.sql.SQLException;
import java.util.List;

import interfaces.DataReceiver;


public class customerChangeInfoPageController implements DataReceiver<KhachHang> {
    private KhachHang customer;
    private TaiKhoan account;
    @FXML private TextField customerUser, customerPass, customerName, customerEmail, customerPhone;

    @Override
    public void setData(KhachHang kh) {
        this.customer = kh;
        try {
            ConnectDatabase connect = new ConnectDatabase();
            List<TaiKhoan> tk = connect.select("taikhoan", null, null, TaiKhoan.class);
            for (TaiKhoan taiKhoan : tk) {
                if (taiKhoan.getMaTaiKhoan() == customer.getMaTaiKhoan()) {
                    account = taiKhoan;
                    customerUser.setText(taiKhoan.getTenDangNhap());
                    customerPass.setText(taiKhoan.getMatKhau());
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        customerName.setText(customer.getHoTen());
        customerEmail.setText(customer.getEmail());
        customerPhone.setText(customer.getSoDienThoai());
    }

    @FXML 
    private void initialize() throws Exception {
        
    }

    @FXML
    private void handleSaveInfo() throws Exception {
        String name = customerName.getText();
        String email = customerEmail.getText();
        String phone = customerPhone.getText();
        String username = customerUser.getText();
        String password = customerPass.getText();
        if (name.length() == 0 || email.length() == 0 || phone.length() == 0) {
            System.out.println("Yeu cau nhap day du thong tin!");
            return;
        }
        account.setTenDangNhap(username);
        account.setMatKhau(password);
        customer.setHoTen(name);
        customer.setEmail(email);
        customer.setSoDienThoai(phone);
        ConnectDatabase connect = new ConnectDatabase();
        try {
            boolean check = connect.update("taikhoan", account);
            if (!check) {
                System.out.println("Cap nhat tai khoan khong thanh cong!");
                return;
            }
            check = connect.update("khachhang", customer);
            if (!check) {
                System.out.println("Cap nhat khach hang khong thanh cong!");
                return;
            }
            System.out.println("Cap nhat thanh cong!");
        } catch (SQLException e) {
            e.printStackTrace();
        }      
    }
}