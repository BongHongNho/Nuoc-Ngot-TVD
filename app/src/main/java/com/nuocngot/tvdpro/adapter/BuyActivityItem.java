package com.nuocngot.tvdpro.adapter;

public class BuyActivityItem {
    private int imageBuyAcvitity;
    private String nameBuyActivity;

    public BuyActivityItem(int imageBuyAcvitity, String nameBuyActivity) {
        this.imageBuyAcvitity = imageBuyAcvitity;
        this.nameBuyActivity = nameBuyActivity;
    }
    public int getImageBuyAcvitity() {
        return imageBuyAcvitity;
    }

    public void setImageBuyAcvitity(int imageBuyAcvitity) {
        this.imageBuyAcvitity = imageBuyAcvitity;
    }

    public String getNameBuyActivity() {
        return nameBuyActivity;
    }

    public void setNameBuyActivity(String nameBuyActivity) {
        this.nameBuyActivity = nameBuyActivity;
    }
}
