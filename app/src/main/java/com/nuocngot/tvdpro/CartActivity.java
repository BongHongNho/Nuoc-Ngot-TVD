package com.nuocngot.tvdpro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.nuocngot.tvdpro.adapter.CartItem;
import com.nuocngot.tvdpro.adapter.CartItemAdapter;
import com.nuocngot.tvdpro.adapter.Order;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public ListView listView;
    Toolbar toolbar;
    TextView tvTotal;
    MaterialCardView btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listView = findViewById(R.id.list_view_cart);
        toolbar = findViewById(R.id.toolbar);
        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v -> {
            ArrayList<CartItem> cartItems = getCartItems();
            int totalQuantity = calculateTotalQuantity(cartItems);
            int totalPrice = calculateTotalPrice(cartItems);
            Order order = new Order(cartItems, totalQuantity, totalPrice);
            Intent intent = new Intent(CartActivity.this, OrderActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
        tvTotal = findViewById(R.id.tvTotal);
        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(this);
        int totalPay = dbHelper.getTotalPay();
        tvTotal.setText(totalPay + " VNĐ");
        toolbar.setTitle("Giỏ hàng");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        displayCartItems();
    }

    public ArrayList<CartItem> getCartItems() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GioHang", null);
        if (cursor.moveToFirst()) {
//            do {
//                int productId = cursor.getInt(cursor.getColumnIndex("maSP"));
//                String productName = cursor.getString(cursor.getColumnIndex("tenSP"));
//                CartItem cartItem = new CartItem(productId, productName);
//                cartItems.add(cartItem);
//            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }


    private int calculateTotalQuantity(ArrayList<CartItem> cartItems) {
        int totalQuantity = 0;
        for (CartItem cartItem : cartItems) {
            totalQuantity += cartItem.getProductQuantity();
        }
        return totalQuantity;
    }

    private int calculateTotalPrice(ArrayList<CartItem> cartItems) {
        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProductPrice() * cartItem.getProductQuantity();
        }
        return totalPrice;
    }

    private void displayCartItems() {
        ArrayList<CartItem> cartItems = getCartItemsFromDatabase();
        CartItemAdapter adapter = new CartItemAdapter(CartActivity.this, cartItems);
        listView.setAdapter(adapter);
    }

    private ArrayList<CartItem> getCartItemsFromDatabase() {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GioHang", null);
        if (cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(cursor.getColumnIndex("maSP"));
                String productName = cursor.getString(cursor.getColumnIndex("tenSP"));
                String productImage = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                int productPrice = cursor.getInt(cursor.getColumnIndex("giaSP"));
                int productQuantity = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
                CartItem cartItem = new CartItem(productId, productName, productImage, productPrice, productQuantity);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cartItems;
    }
}