package com.nuocngot.tvdpro.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.GioHangAdapter;
import com.nuocngot.tvdpro.adapter.GioHangItem;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GioHangAdapter adapter;

    private Toolbar toolbar;
    private TextView textViewTotalPrice;
    private ArrayList<GioHangItem> gioHangItemList = new ArrayList<>();
    private GioHangAdapter gioHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        recyclerView = findViewById(R.id.recyclerViewGioHang);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        gioHangAdapter = new GioHangAdapter(gioHangItemList);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Giỏ hàng");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> finish());
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        if (maKH != -1) {
            loadGioHangData(maKH);
        } else {
            Toast.makeText(this, "Khách hàng không tồn tại", Toast.LENGTH_SHORT).show();
        }

        adapter = new GioHangAdapter(this, gioHangItemList, new GioHangAdapter.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, int newQuantity) {
                updateQuantity(position, newQuantity);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        calculateAndDisplayTotalPrice();
    }

    private void loadGioHangData(int maKH) {
        String maKHString = String.valueOf(maKH); // Chuyển maKH từ int sang String

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "maSP",
                "tenSP",
                "soLuong",
                "tongTien",
                "hinhAnh"
        };
        Cursor cursor = db.query(
                "GioHang",
                projection,
                "maKH = ?", // Sử dụng điều kiện để lọc dữ liệu theo maKH
                new String[]{maKHString}, // Truyền maKH dưới dạng một mảng chuỗi
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndexOrThrow("maSP"));
                String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("tenSP"));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                int tongTien = cursor.getInt(cursor.getColumnIndexOrThrow("tongTien"));
                String hinhAnh = cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh"));
                boolean isProductExist = false;
                for (GioHangItem item : gioHangItemList) {
                    if (item.getMaSP() == maSP) {
                        item.setSoLuong(item.getSoLuong() + 1);
                        isProductExist = true;
                        break;
                    }
                }
                if (!isProductExist) {
                    gioHangItemList.add(new GioHangItem(maSP, tenSP, soLuong, tongTien, hinhAnh));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();
        gioHangAdapter.notifyDataSetChanged();
    }


    private void updateQuantity(int position, int newQuantity) {
        GioHangItem gioHangItem = gioHangItemList.get(position);
        gioHangItem.setSoLuong(newQuantity);
        calculateAndDisplayTotalPrice();
        gioHangAdapter.notifyDataSetChanged();
    }


    private void calculateAndDisplayTotalPrice() {
        int totalPrice = 0;
        for (GioHangItem gioHangItem : gioHangItemList) {
            totalPrice += gioHangItem.getSoLuong() * gioHangItem.getGia();
        }
        textViewTotalPrice.setText("Tổng tiền: " + totalPrice + " VNĐ");
    }
}
