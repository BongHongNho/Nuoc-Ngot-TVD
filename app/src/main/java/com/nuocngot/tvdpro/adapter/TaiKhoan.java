package com.nuocngot.tvdpro.adapter;

public class TaiKhoan {
    private int maTK;
    private int maKH;
    private String tenDN;
    private String matKhau;
    private String email;
    private String sdt;
    private String role;
    private int userId;
    private String avatarUrl;

    public TaiKhoan(int maTK, int maKH, String tenDN, String matKhau, String email, String sdt, String role, int userId, String avatarUrl) {
        this.maTK = maTK;
        this.maKH = maKH;
        this.tenDN = tenDN;
        this.matKhau = matKhau;
        this.email = email;
        this.sdt = sdt;
        this.role = role;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
    }

    public TaiKhoan(int maTK, String tenDN, String email, String sdt, String role) {
        this.maTK = maTK;
        this.tenDN = tenDN;
        this.email = email;
        this.sdt = sdt;
        this.role = role;
    }

    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}


