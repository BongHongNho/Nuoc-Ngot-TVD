package com.nuocngot.tvdpro.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.DonHangAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.DonHang;

import java.util.ArrayList;
import java.util.List;

public class QuanLiDonHangActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DonHangAdapter donHangAdapter;
    private ArrayList<DonHang> donHangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_don_hang);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Quản lý đơn hàng");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyceViewDH);
        donHangList = loadDonHangList(this);
        donHangAdapter = new DonHangAdapter(donHangList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(donHangAdapter);
    }
    private ArrayList<DonHang> loadDonHangList(Context context) {
        ArrayList<DonHang> donHangList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Truy vấn để lấy danh sách đơn hàng từ bảng DonMua
        String query = "SELECT * FROM DonMua";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenDH = cursor.getString(cursor.getColumnIndex("tenDH"));
                String tenSPDH = cursor.getString(cursor.getColumnIndex("tenSPDH"));
                int soLuongSPDH = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int tongTienDH = cursor.getInt(cursor.getColumnIndex("tongTien"));
                String ngayMua = cursor.getString(cursor.getColumnIndex("ngayMua"));
                String anhDH = cursor.getString(cursor.getColumnIndex("anhDH"));
                int maDM = cursor.getInt(cursor.getColumnIndex("maDMUA"));
                int maTTDH = cursor.getInt(cursor.getColumnIndex("maTTDH"));

                // Tạo đối tượng DonHang từ dữ liệu cột của bảng DonMua
                DonHang donHang = new DonHang(tenDH, tenSPDH, soLuongSPDH, tongTienDH, ngayMua, anhDH, maDM, maTTDH);
                donHangList.add(donHang);
            } while (cursor.moveToNext());
        }

        // Đóng kết nối CSDL và giải phóng tài nguyên
        cursor.close();
        db.close();
        dbHelper.close();

        // Trả về danh sách các đơn hàng
        return donHangList;
    }

}