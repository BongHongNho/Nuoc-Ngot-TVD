package com.nuocngot.tvdpro.model;

public class SanPham {
    private int maSP;
    private String hinhAnh;
    private String tenSP;
    private int soLuong;
    private int gia;

    public SanPham(int maSP, String hinhAnh, String tenSP, int soLuong, int gia) {
        this.maSP = maSP;
        this.hinhAnh = hinhAnh;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public SanPham(int maSP, String tenSP, int gia, String hinhAnh, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
    }

    public int getMaSP() {
        return maSP;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public String getTenSP() {
        return tenSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public int getGia() {
        return gia;
    }
}
