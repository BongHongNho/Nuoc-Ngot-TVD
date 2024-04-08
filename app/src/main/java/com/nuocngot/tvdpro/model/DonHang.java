package com.nuocngot.tvdpro.model;

public class DonHang {
    private String tenDH;
    private String tenSPDH;
    private int soLuongSPDH;
    private int tongTienDH;
    private String ngayMua;
    private String anhDH;
    private int maDM;

    private int maTTDH;

    public DonHang(String tenDH, String tenSPDH, int soLuongSPDH, int tongTienDH, String ngayMua, String anhDH, int maDM, int maTTDH) {
        this.tenDH = tenDH;
        this.tenSPDH = tenSPDH;
        this.soLuongSPDH = soLuongSPDH;
        this.tongTienDH = tongTienDH;
        this.ngayMua = ngayMua;
        this.anhDH = anhDH;
        this.maDM = maDM;
        this.maTTDH = maTTDH;
    }

    public int getMaTTDH() {
        return maTTDH;
    }

    public int getMaDM() {
        return maDM;
    }

    public void setMaDM(int maDM) {
        this.maDM = maDM;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }

    public String getTenDH() {
        return tenDH;
    }

    public void setTenDH(String tenDH) {
        this.tenDH = tenDH;
    }

    public String getTenSPDH() {
        return tenSPDH;
    }

    public void setTenSPDH(String tenSPDH) {
        this.tenSPDH = tenSPDH;
    }

    public int getSoLuongSPDH() {
        return soLuongSPDH;
    }

    public void setSoLuongSPDH(int soLuongSPDH) {
        this.soLuongSPDH = soLuongSPDH;
    }

    public int getTongTienDH() {
        return tongTienDH;
    }

    public void setTongTienDH(int tongTienDH) {
        this.tongTienDH = tongTienDH;
    }

    public String getAnhDH() {
        return anhDH;
    }

    public void setAnhDH(String anhDH) {
        this.anhDH = anhDH;
    }

    public void setMaTTDH(int maTTDH) {
    }
}
