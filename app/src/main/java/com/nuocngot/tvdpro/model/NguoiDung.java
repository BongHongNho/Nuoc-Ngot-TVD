package com.nuocngot.tvdpro.model;

public class NguoiDung {
    private int maND;
    private String tenDN;
    private String matKhau;
    private String email;
    private String sdt;
    private String diaChi;
    private String capTV;
    private int trangThai;
    private String role;
    private int isLogin;
    private String hinhAnh;

    // Constructor
    public NguoiDung(int maND, String tenDN, String matKhau, String email, String sdt,
                     String diaChi, String capTV, int trangThai, String role, int isLogin, String hinhAnh) {
        this.maND = maND;
        this.tenDN = tenDN;
        this.matKhau = matKhau;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.capTV = capTV;
        this.trangThai = trangThai;
        this.role = role;
        this.isLogin = isLogin;
        this.hinhAnh = hinhAnh;
    }

    public NguoiDung(int maND, String tenDN, String email, String sdt, String role) {
        this.maND = maND;
        this.tenDN = tenDN;
        this.email = email;
        this.sdt = sdt;
        this.role = role;
    }

    // Getter and Setter methods
    public int getMaND() {
        return maND;
    }

    public String getTenDN() {
        return tenDN;
    }

    public void setTenDN(String tenDN) {
        this.tenDN = tenDN;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getCapTV() {
        return capTV;
    }

    public void setCapTV(String capTV) {
        this.capTV = capTV;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
