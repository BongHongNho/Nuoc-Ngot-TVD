package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nuocngot.tvdpro.activity.OrderActivity;

import java.util.ArrayList;

public class OrderDetailsAdapter extends BaseAdapter {
    private ArrayList<CartItem> cartItems = new ArrayList<>();

    private Context context;

    public OrderDetailsAdapter(OrderActivity orderActivity, ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.context = orderActivity;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
