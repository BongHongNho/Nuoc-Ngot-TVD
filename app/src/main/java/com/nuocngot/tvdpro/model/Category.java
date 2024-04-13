package com.nuocngot.tvdpro.model;

public class Category {
    private int maDM;
    private String tenDM;
    private String anhDM;

    public Category(int maDM, String tenDM, String anhDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        this.anhDM = anhDM;
    }

    public int getMaDM() {
        return maDM;
    }

    public void setMaDM(int maDM) {
        this.maDM = maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    public String getAnhDM() {
        return anhDM;
    }

    public void setAnhDM(String anhDM) {
        this.anhDM = anhDM;
    }

    @Override
    public String toString() {
        return tenDM;
    }
}
