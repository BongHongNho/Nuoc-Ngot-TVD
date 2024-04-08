package com.nuocngot.tvdpro.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.Order;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra("order");
//        displayOrderDetails(order);
    }

//    private void displayOrderDetails(Order order) {
//        TextView tvTotalProducts = findViewById(R.id.tvTotalProducts);
//        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
//        tvTotalProducts.setText("Tổng số sản phẩm: " + order.getTotalQuantity());
//        tvTotalPrice.setText("Tổng giá tiền: " + order.getTotalPrice() + " VNĐ");
//        ListView lvOrderDetails = findViewById(R.id.lvOrderDetails);
//        OrderDetailsAdapter adapter = new OrderDetailsAdapter(this, order.getCartItems());
//        lvOrderDetails.setAdapter(adapter);
//    }

}