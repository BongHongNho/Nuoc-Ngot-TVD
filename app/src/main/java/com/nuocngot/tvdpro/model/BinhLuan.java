package com.nuocngot.tvdpro.model;

public class BinhLuan {
    private String tenNguoiDung;

    private int maKH;

    private int maBL;

    public BinhLuan(String tenND, String binhLuan, String thoiGian, String anhBL, int maKH, int maBL) {
        this.tenNguoiDung = tenND;
        this.noiDung = binhLuan;
        this.maBL = maBL;
        this.thoiGian = thoiGian;
        this.anhDaiDien = anhBL;
        this.maKH = maKH;
    }

    public void setMaBL(int maBL) {
        this.maBL = maBL;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    private String noiDung;
    private String thoiGian;
    private String anhDaiDien;

    private int maSP;

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public int getMaSP() {
        return maSP;
    }

    public int getMaBL() {
        return maBL;
    }
}
