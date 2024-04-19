package com.nuocngot.tvdpro.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class GioHangItem implements Parcelable {
    private static int maSP;
    private String tenSP;
    private int soLuong;
    private int gia;
    private String anhSP;
    private int maGH;
    private int  daChon;

    public GioHangItem(int maSP, String tenSP, int soLuong, int tongTien, String hinhAnh, int maGH, int daChon) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.gia = tongTien;
        this.anhSP = hinhAnh;
        this.maGH = maGH;
        this.daChon = daChon;
    }

    public GioHangItem(int maSP, int soLuong, int gia) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public GioHangItem(int maSP, int soLuong, int gia, int daChon) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.gia =gia;
        this.daChon = daChon;
    }

    public GioHangItem(int maGH, int maSP, int soLuong, int gia, int daChon) {
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.gia =gia;
        this.daChon = daChon;
        this.maGH = maGH;
    }


    public int getDaChon() {
        return daChon;
    }

    public void setDaChon(int daChon) {
        this.daChon = daChon;
    }

    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public static final Creator<GioHangItem> CREATOR = new Creator<GioHangItem>() {
        @Override
        public GioHangItem createFromParcel(Parcel in) {
            return new GioHangItem(in);
        }

        @Override
        public GioHangItem[] newArray(int size) {
            return new GioHangItem[size];
        }
    };


    public int getMaGH() {
        return maGH;
    }

    public void setMaGH(int maGH) {
        this.maGH = maGH;
    }

    public static int getMaSP() {
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

    public String getAnhSP() {
        return anhSP;
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

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public void decreaseQuantity() {
        if (soLuong > 1) {
            soLuong--;
        }
    }

    public void increaseQuantity() {
        soLuong++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maSP);
        dest.writeString(tenSP);
        dest.writeInt(soLuong);
        dest.writeInt(gia);
        dest.writeString(anhSP);
        dest.writeInt(maGH); // Ghi giá trị maGH vào Parcel
    }

    public GioHangItem(Parcel in) {
        maSP = in.readInt();
        tenSP = in.readString();
        soLuong = in.readInt();
        gia = in.readInt();
        anhSP = in.readString();
        maGH = in.readInt(); // Đọc giá trị maGH từ Parcel
    }

}
