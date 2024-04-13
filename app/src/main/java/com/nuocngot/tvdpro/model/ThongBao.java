package com.nuocngot.tvdpro.model;

public class ThongBao {
    public String title;
    public String noidung;
    public String time;
    public String hinhAnh;

    public ThongBao(String title, String noidung, String time, String hinhAnh) {
        this.title = title;
        this.noidung = noidung;
        this.time = time;
        this.hinhAnh = hinhAnh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }
}
