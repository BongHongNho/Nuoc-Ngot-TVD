package com.nuocngot.tvdpro.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.QLUserAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.QLUser;

import java.util.ArrayList;

public class QuanLyUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<QLUser> khachHangs = new ArrayList<>();
    private QLUserAdapter adapter = new QLUserAdapter(khachHangs);
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_user);
        recyclerView = findViewById(R.id.recycleViewUser);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Quản lý người dùng");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadKhachHangData();
    }
    private void loadKhachHangData() {
        khachHangs.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "KhachHang",
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maKH = cursor.getInt(cursor.getColumnIndex("maKH"));
                String tenKH = cursor.getString(cursor.getColumnIndex("tenKH"));
                String email = cursor.getString(cursor.getColumnIndex("Email"));
                String sdt = cursor.getString(cursor.getColumnIndex("SDT"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                String capTV = cursor.getString(cursor.getColumnIndex("capTV"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                QLUser khachHang = new QLUser(maKH, tenKH, email, sdt, diaChi, capTV, hinhAnh);
                khachHangs.add(khachHang);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

}