package com.nuocngot.tvdpro.model;

public class QLUser {
    private int maTK;
    private String tenKH;
    private String anhKH;
    private String sdtKH;
    private String emailKH;
    private String diaChiKH;
    private String viTri;

    public QLUser(int maKH, String tenKH, String email, String sdt, String diaChi, String capTV, String hinhAnh) {
        this.maTK = maKH;
        this.tenKH = tenKH;
        this.emailKH = email;
        this.sdtKH = sdt;
        this.diaChiKH = diaChi;
        this.viTri = capTV;
        this.anhKH = hinhAnh;
    }


    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getAnhKH() {
        return anhKH;
    }

    public void setAnhKH(String anhKH) {
        this.anhKH = anhKH;
    }

    public String getSdtKH() {
        return sdtKH;
    }

    public void setSdtKH(String sdtKH) {
        this.sdtKH = sdtKH;
    }

    public String getEmailKH() {
        return emailKH;
    }

    public void setEmailKH(String emailKH) {
        this.emailKH = emailKH;
    }

    public String getDiaChiKH() {
        return diaChiKH;
    }

    public void setDiaChiKH(String diaChiKH) {
        this.diaChiKH = diaChiKH;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
}
