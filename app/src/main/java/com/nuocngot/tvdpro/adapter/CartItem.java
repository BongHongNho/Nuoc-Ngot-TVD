package com.nuocngot.tvdpro.adapter;

public class CartItem {
    private int id; // Mã số của mục trong giỏ hàng
    private int customerId; // Mã số của khách hàng
    private String customerName; // Tên của khách hàng
    private int productId; // Mã số của sản phẩm
    private String productName; // Tên của sản phẩm
    private String productImage; // URL hình ảnh của sản phẩm
    private int productPrice; // Giá của sản phẩm
    private int productQuantity; // Số lượng sản phẩm trong giỏ hàng
    private int quantityInStock; // Số lượng sản phẩm có sẵn
    private String totalAmount; // Tổng số tiền của mục trong giỏ hàng

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CartItem(int id, int customerId, String customerName, int productId, String productName, String productImage, int productPrice, int productQuantity, int quantityInStock, String totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.quantityInStock = quantityInStock;
        this.totalAmount = totalAmount;
    }

    public CartItem() {
    }

    // Các phương thức getter và setter
}

