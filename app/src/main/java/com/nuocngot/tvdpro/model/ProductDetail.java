package com.nuocngot.tvdpro.model;
public class ProductDetail {
    private int productId;
    private String imageUrl;
    private String productName;
    private int quantity;
    private int price;
    private String origin;
    private String info;

    public ProductDetail(int productId, String imageUrl, String productName, int quantity, int price, String origin, String info) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.origin = origin;
        this.info = info;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
