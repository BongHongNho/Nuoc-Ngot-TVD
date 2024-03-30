package com.nuocngot.tvdpro.model;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String email;
    private String sdt;
    private String diaChi;
    private String capTV;
    private String hinhAnh;

    public KhachHang(int maKH, String tenKH, String email, String sdt, String diaChi, String capTV) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.capTV = capTV;
        this.hinhAnh = hinhAnh;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
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

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
