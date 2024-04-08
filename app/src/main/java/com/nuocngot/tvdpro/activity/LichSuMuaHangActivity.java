package com.nuocngot.tvdpro.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.LichSuMuaHangAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.LichSu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LichSuMuaHangActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;

    private TextView textLSEmpty;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_mua_hang);
        listView = findViewById(R.id.listView);
        textLSEmpty = findViewById(R.id.textLSEmpty);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE);
        int maKhachHang = sharedPreferences.getInt("maKH", 0);
        ArrayList<LichSu> lichSuMuaHang = dbHelper.getLichSuMuaHang(maKhachHang);
        if (lichSuMuaHang.size() == 0) {
            textLSEmpty.setVisibility(View.VISIBLE);
        } else {
            textLSEmpty.setVisibility(View.GONE);
        }
        LichSuMuaHangAdapter adapter = new LichSuMuaHangAdapter(this, lichSuMuaHang);
        listView.setAdapter(adapter);
    }
}

