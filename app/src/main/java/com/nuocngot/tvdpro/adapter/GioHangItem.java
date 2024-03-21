package com.nuocngot.tvdpro.adapter;

public class GioHangItem {
    private int maSP;
    private String tenSP;
    private int soLuong;
    private int  gia;

    private String anhSP;

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public GioHangItem(int maSP, String tenSP, int soLuong, int gia, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.gia = gia;
        this.anhSP = hinhAnh;
    }

    public int getMaSP() {
        return maSP;
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

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public void decreaseQuantity() {
        if (soLuong > 1) {
            soLuong--;
        }
    }
    public void increaseQuantity() {
        soLuong++;
    }
}
