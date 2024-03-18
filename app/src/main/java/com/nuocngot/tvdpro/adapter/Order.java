package com.nuocngot.tvdpro.adapter;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private ArrayList<CartItem> cartItems;
    private int totalQuantity;
    private int totalPrice;

    public Order(ArrayList<CartItem> cartItems, int totalQuantity, int totalPrice) {
        this.cartItems = cartItems;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
