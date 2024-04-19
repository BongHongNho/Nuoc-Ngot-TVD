package com.nuocngot.tvdpro.model;

public class DonHang {
    private String tenDH;
    private String tenSPDH;
    private int soLuongSPDH;
    private int tongTienDH;
    private String ngayMua;
    private String anhDH;
    private int maDM;
    private int maND;

    public DonHang(String tenDH, String tenSPDH, int soLuongSPDH, int tongTienDH, String ngayMua, String anhDH, int maDM, int maND, int maTTDH) {
        this.tenDH = tenDH;
        this.tenSPDH = tenSPDH;
        this.soLuongSPDH = soLuongSPDH;
        this.tongTienDH = tongTienDH;
        this.ngayMua = ngayMua;
        this.anhDH = anhDH;
        this.maDM = maDM;
        this.maND = maND;
        this.maTTDH = maTTDH;
    }

    public DonHang(String tenDH, String tenSPDH, int soLuongSPDH, int maTTDHValue, String ngayMua, int tongTien, String anhDH, int maND) {
        this.tenDH = tenDH;
        this.tenSPDH = tenSPDH;
        this.soLuongSPDH = soLuongSPDH;
        this.ngayMua = ngayMua;
        this.tongTienDH = tongTien;
        this.anhDH = anhDH;
        this.maND = maND;
        this.maTTDH = maTTDHValue;
    }

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

    public DonHang(String tenDH, String tenSPDH, int soLuongSPDH, int maTTDHValue, String ngayMua, int tongTien, String anhDH) {
        this.tenDH = tenDH;
        this.tenSPDH = tenSPDH;
        this.soLuongSPDH = soLuongSPDH;
        this.ngayMua = ngayMua;
        this.tongTienDH = tongTien;
        this.anhDH = anhDH;
        this.maTTDH = maTTDHValue;
    }

    public int getMaND() {
        return maND;
    }

    public void setMaND(int maND) {
        this.maND = maND;
    }

    private int maTTDH;


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
