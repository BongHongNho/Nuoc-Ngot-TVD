package com.nuocngot.tvdpro.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.GioHangAdapter;
import com.nuocngot.tvdpro.adapter.GioHangItem;
import com.nuocngot.tvdpro.model.KhachHang;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class ThanhToanActivity extends AppCompatActivity {
    private TextView textViewDiaChi;
    private RecyclerView recyclerViewSanPham;

    private Toolbar toolbar;
    private TextView textViewPhuongThucThanhToan;
    private TextView textViewTongTienHang;
    private TextView textViewPhiVanChuyen;
    private TextView textViewTongThanhToan, textViewSDT,textViewNguoiNhan;
    private Button btnDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        textViewDiaChi = findViewById(R.id.textViewDiaChi);
        textViewSDT = findViewById(R.id.textViewSDT);
        textViewNguoiNhan = findViewById(R.id.textViewNguoiNhan);
        recyclerViewSanPham = findViewById(R.id.recyclerViewSanPham);
        textViewPhuongThucThanhToan = findViewById(R.id.textViewPhuongThucThanhToan);
        textViewTongTienHang = findViewById(R.id.textViewTongTienHang);
        textViewPhiVanChuyen = findViewById(R.id.textViewPhiVanChuyen);
        textViewTongThanhToan = findViewById(R.id.textViewTongThanhToan);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Thanh toán");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnDatHang = findViewById(R.id.btnDatHang);
        String diaChi = "123 Đường ABC, Quận XYZ, Thành phố HCM";
        textViewDiaChi.setText("Địa chỉ mua hàng: " + diaChi);
        ArrayList<GioHangItem> gioHangItems = getIntent().getParcelableArrayListExtra("gio_hang_items");
        GioHangAdapter gioHangAdapter = new GioHangAdapter(gioHangItems);
        recyclerViewSanPham.setAdapter(gioHangAdapter);
        recyclerViewSanPham.setLayoutManager(new LinearLayoutManager(this));
        int tongTienHang = 0;
        for (GioHangItem item : gioHangItems) {
            tongTienHang += item.getSoLuong() * item.getGia();
        }
        textViewTongTienHang.setText("Tổng tiền hàng: " + tongTienHang + " VNĐ");
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ThanhToanActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ThanhToanActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loadDonMuaData(); // Thêm phương thức loadDonMuaData() vào onCreate() để hiển thị thông tin đơn mua
    }

    @SuppressLint("Range")
    private void loadDonMuaData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Thực hiện truy vấn dữ liệu từ bảng KhachHang
        Cursor cursor = db.query(
                "KhachHang",
                null, // Truy vấn tất cả các cột
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int tongTienDonHang = 0;
            do {
                 int maKH = cursor.getInt(cursor.getColumnIndex("maKH"));
                String tenKH = cursor.getString(cursor.getColumnIndex("tenKH"));
                String email = cursor.getString(cursor.getColumnIndex("Email"));
                String sdt = cursor.getString(cursor.getColumnIndex("SDT"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                String capTV = cursor.getString(cursor.getColumnIndex("capTV"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                KhachHang khachHang = new KhachHang(maKH, tenKH, email, sdt, diaChi, capTV);
                textViewDiaChi.setText("Địa chỉ mua hàng: " + diaChi);
                textViewPhuongThucThanhToan.setText("Phương thức thanh toán: " + capTV);
                textViewTongThanhToan.setText(tongTienDonHang + " VNĐ");
                textViewPhiVanChuyen.setText( 0 + " VNĐ");
                textViewTongTienHang.setText(tongTienDonHang + " VNĐ");
                textViewSDT.setText("Số điện thoại: " + sdt);
                textViewNguoiNhan.setText("Người nhận: " + tenKH);

            } while (cursor.moveToNext());
            cursor.close(); // Đóng con trỏ sau khi sử dụng
        }

        db.close(); // Đóng cơ sở dữ liệu sau khi sử dụng
        dbHelper.close();
    }

}