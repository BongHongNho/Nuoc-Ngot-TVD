package com.nuocngot.tvdpro.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.GioHangAdapter;
import com.nuocngot.tvdpro.adapter.GioHangItem;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThanhToanActivity extends AppCompatActivity {
    private TextView textViewDiaChi;
    private RecyclerView recyclerViewSanPham;

    private LinearLayout selectPTTT;

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
        selectPTTT = findViewById(R.id.selectPTTT);
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
        ArrayList<GioHangItem> selectedItems = getIntent().getParcelableArrayListExtra("selected_items");
        GioHangAdapter gioHangAdapter = new GioHangAdapter(selectedItems);
        recyclerViewSanPham.setAdapter(gioHangAdapter);
        recyclerViewSanPham.setLayoutManager(new LinearLayoutManager(this));
        int tongTienHang = 0;
        for (GioHangItem item : selectedItems) { // Sử dụng selectedItems thay vì gioHangItems
            tongTienHang += item.getSoLuong() * item.getGia();
        }
        textViewTongTienHang.setText("Tổng tiền hàng: " + tongTienHang + " VNĐ");
        int finalTongTienHang = tongTienHang;
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                int maKH = sharedPreferences.getInt("maKH", -1); // -1 là giá trị mặc định nếu không tìm thấy
                int maTTDH = 1;
                String ngayMua = getCurrentDate();
                int soLuong = selectedItems.size();
                int tongTien = finalTongTienHang;
                DatabaseHelper dbHelper = new DatabaseHelper(ThanhToanActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("maKH", maKH);
                values.put("maTTDH", maTTDH);
                values.put("ngayMua", ngayMua);
                values.put("soLuong", soLuong);
                values.put("tongTien", tongTien);
                values.put("maSP", selectedItems.get(0).getMaSP()); // Giả sử lấy maSP của sản phẩm đầu tiên
                values.put("maTTHD", 1); // Giả sử trạng thái đơn hàng là 1

                long result = db.insert("DonMua", null, values);
                if (result != -1) {
                    Toast.makeText(ThanhToanActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThanhToanActivity.this, "Đặt hàng không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });



        loadDonMuaData();
        selectPTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhuongThucThanhToan();
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void loadDonMuaData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "GioHang",
                null,
                null,
                null, // Thay vì new String[]{"1"}
                null,
                null,
                null
        );
        int tongTienDonHang = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maKH = cursor.getInt(cursor.getColumnIndex("maKH"));
                int tongTien = cursor.getInt(cursor.getColumnIndex("tongTien"));
                tongTienDonHang += tongTien;
                Cursor khCursor = db.query(
                        "KhachHang",
                        null,
                        "maKH = ?",
                        new String[]{String.valueOf(maKH)},
                        null,
                        null,
                        null
                );
                if (khCursor != null && khCursor.moveToFirst()) {
                    String tenKH = khCursor.getString(khCursor.getColumnIndex("tenKH"));
                    String sdt = khCursor.getString(khCursor.getColumnIndex("SDT"));
                    String diaChi = khCursor.getString(khCursor.getColumnIndex("diaChi"));
                    textViewDiaChi.setText("Địa chỉ mua hàng: " + diaChi);
                    textViewTongThanhToan.setText(tongTienDonHang + " VNĐ");
                    textViewPhiVanChuyen.setText("0 VNĐ"); // Có thể cần lấy dữ liệu phí vận chuyển từ bảng nếu có
                    textViewTongTienHang.setText(tongTienDonHang + " VNĐ");
                    textViewSDT.setText("Số điện thoại: " + sdt);
                    textViewNguoiNhan.setText("Người nhận: " + tenKH);

                    khCursor.close();
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        dbHelper.close();
    }


    private void loadPhuongThucThanhToan() {
        String[] phuongThucThanhToan = {"Thanh toán khi nhận hàng", "Thanh toán qua ngân hàng"};
        new AlertDialog.Builder(this)
                .setTitle("Phương thức thanh toán")
                .setMessage("Vui lý chọn phương thức thanh toán")
                .setItems(phuongThucThanhToan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewPhuongThucThanhToan.setText(phuongThucThanhToan[which]);
                    }
                })
                .show();

    }

}