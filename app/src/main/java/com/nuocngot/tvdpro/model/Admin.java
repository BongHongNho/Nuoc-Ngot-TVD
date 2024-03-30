package com.nuocngot.tvdpro.model;

public class Admin {
    private String tenAdmin;
    private String hinhAnh;

    public Admin(String tenAdmin, String hinhAnh) {
        this.tenAdmin = tenAdmin;
        this.hinhAnh = hinhAnh;
    }

    public Admin() {

    }

    public String getTenAdmin() {
        return tenAdmin;
    }

    public void setTenAdmin(String tenAdmin) {
        this.tenAdmin = tenAdmin;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}

