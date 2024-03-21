package com.nuocngot.tvdpro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nuocngot.tvdpro.adapter.Order;
import com.nuocngot.tvdpro.adapter.OrderDetailsAdapter;

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