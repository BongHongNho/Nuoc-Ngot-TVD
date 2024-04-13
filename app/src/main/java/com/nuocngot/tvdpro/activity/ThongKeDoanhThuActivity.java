package com.nuocngot.tvdpro.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThongKeDoanhThuActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_doanh_thu);
        dbHelper = new DatabaseHelper(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        int doanhThuNgay = getDoanhThuTheoNgay(currentDate);
        TextView textViewDoanhThuNgay = findViewById(R.id.textViewDoanhThuNgay);
        textViewDoanhThuNgay.setText("Doanh thu ngày " + currentDate + ": " + doanhThuNgay + " VND");
    }
    private int getDoanhThuTheoNgay(String ngay) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int doanhThu = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(tongTien) AS total " +
                "FROM DonMua " +
                "WHERE ngayMua = ?", new String[]{ngay});

        if (cursor != null && cursor.moveToFirst()) {
            doanhThu = cursor.getInt(cursor.getColumnIndex("total"));
            cursor.close();
        } else {
            Toast.makeText(this, "Không có dữ liệu doanh thu cho ngày này", Toast.LENGTH_SHORT).show();
        }

        return doanhThu;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}


