package com.nuocngot.tvdpro.model;

public class LichSu {
    public LichSu() {

    }

    public int getMaDonMua() {
        return maDonMua;
    }

    public void setMaDonMua(int maDonMua) {
        this.maDonMua = maDonMua;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaTrangThaiDonHang() {
        return maTrangThaiDonHang;
    }

    public void setMaTrangThaiDonHang(int maTrangThaiDonHang) {
        this.maTrangThaiDonHang = maTrangThaiDonHang;
    }

    public String getTenDonHang() {
        return tenDonHang;
    }

    public void setTenDonHang(String tenDonHang) {
        this.tenDonHang = tenDonHang;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    private int maDonMua;
    private int maKhachHang;
    private int maSanPham;
    private int maTrangThaiDonHang;
    private String tenDonHang;
    private String tenSanPham;
    private String anhSanPham;
    private String ngayMua;
    private int soLuong;
    private int tongTien;

    public LichSu(int maDonMua, int maKhachHang, int maSanPham, int maTrangThaiDonHang, String tenDonHang, String tenSanPham, String anhSanPham, String ngayMua, int soLuong, int tongTien) {
        this.maDonMua = maDonMua;
        this.maKhachHang = maKhachHang;
        this.maSanPham = maSanPham;
        this.maTrangThaiDonHang = maTrangThaiDonHang;
        this.tenDonHang = tenDonHang;
        this.tenSanPham = tenSanPham;
        this.anhSanPham = anhSanPham;
        this.ngayMua = ngayMua;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
    }
}
