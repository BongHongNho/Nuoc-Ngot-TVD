package com.nuocngot.tvdpro.adapter;

public class Category {
    private int maDM;
    private String tenDM;

    public void setMaDM(int maDM) {
        this.maDM = maDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    public Category(int maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    public int getMaDM() {
        return maDM;
    }

    public String getTenDM() {
        return tenDM;
    }
    @Override
    public String toString() {
        return tenDM; // hoặc bất kỳ thuộc tính nào bạn muốn hiển thị
    }
}
