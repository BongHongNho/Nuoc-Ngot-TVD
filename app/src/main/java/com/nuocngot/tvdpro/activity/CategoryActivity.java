package com.nuocngot.tvdpro.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.SanPham;
import com.nuocngot.tvdpro.adapter.SanPhamAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private int maDM;
    private TextView textViewNoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        maDM = getIntent().getIntExtra("maDM", 0);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kết quả tìm kiếm");
        toolbar.setNavigationIcon(R.drawable.back);
        textViewNoItem = findViewById(R.id.textViewNoItem);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerViewProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<SanPham> productList = loadProductsByCategory(maDM);
        if (productList.isEmpty()) {
            textViewNoItem.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoItem.setVisibility(View.GONE);
        }
        SanPhamAdapter adapter = new SanPhamAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<SanPham> loadProductsByCategory(int maDM) {
        ArrayList<SanPham> productList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"maSP", "tenSP", "gia", "hinhAnh", "soLuong"}; // Thêm cột soLuong vào projection
        String selection = "maDM = ?";
        String[] selectionArgs = {String.valueOf(maDM)};
        Cursor cursor = db.query("SanPham", projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndexOrThrow("maSP"));
                String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("tenSP"));
                int gia = cursor.getInt(cursor.getColumnIndexOrThrow("gia"));
                String hinhAnh = cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh"));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong")); // Lấy thông tin soLuong từ cột soLuong
                SanPham sanPham = new SanPham(maSP, tenSP, gia, hinhAnh, soLuong); // Thêm soLuong vào đối tượng SanPham
                productList.add(sanPham);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();
        return productList;
    }
}
