package com.nuocngot.tvdpro.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.GioHangAdapter;
import com.nuocngot.tvdpro.adapter.GioHangItem;
import com.nuocngot.tvdpro.adapter.ThanhToanAdapter;
import com.nuocngot.tvdpro.model.KhachHang;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ThanhToanActivity extends AppCompatActivity {
    private TextView textViewDiaChi;
    private RecyclerView recyclerViewSanPham;
    private LinearLayout selectPTTT, linearDCNH;
    private Toolbar toolbar;
    private TextView textViewPhuongThucThanhToan;
    private TextView textViewTongTienHang;
    private TextView textViewPhiVanChuyen;
    private TextView textViewTongThanhToan, textViewSDT, textViewNguoiNhan, textViewTotalPrice, textViewTongSL;
    private Button btnDatHang;

    private int finalTongTienHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        textViewDiaChi = findViewById(R.id.textViewDiaChi);
        textViewSDT = findViewById(R.id.textViewSDT);
        selectPTTT = findViewById(R.id.selectPTTT);
        linearDCNH = findViewById(R.id.linearDCNH);
        textViewNguoiNhan = findViewById(R.id.textViewNguoiNhan);
        recyclerViewSanPham = findViewById(R.id.recyclerViewSanPham);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        textViewPhuongThucThanhToan = findViewById(R.id.textViewPhuongThucThanhToan);
        textViewPhuongThucThanhToan.setText("Vui lòng chọn phương thức thanh toán");
        textViewTongTienHang = findViewById(R.id.textViewTongTienHang);
        textViewPhiVanChuyen = findViewById(R.id.textViewPhiVanChuyen);
        textViewTongThanhToan = findViewById(R.id.textViewTongThanhToan);
        textViewTongSL = findViewById(R.id.textViewTongSL);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Thanh toán");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        selectPTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToanActivity.this);
                builder.setTitle("Chọn phương thức thanh toán");
                String[] phuongThucThanhToan = {"Thanh toán khi nhận hàng", "Thanh toán qua ngân hàng"};
                builder.setItems(phuongThucThanhToan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedPTTT = phuongThucThanhToan[which];
                        textViewPhuongThucThanhToan.setText("Phương thức thanh toán: " + selectedPTTT);
                        int tongTienHang = 0;
                        ArrayList<GioHangItem> gioHangItems = getIntent().getParcelableArrayListExtra("selected_items");
                        for (GioHangItem item : gioHangItems) {
                            tongTienHang += item.getSoLuong() * item.getGia();
                        }
                        finalTongTienHang = tongTienHang;
                        finalTongTienHang = calculateFinalTongTienHang();
                    }
                });
                builder.show();
            }
        });


        linearDCNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                int maTK = sharedPreferences.getInt("maTK", -1);
                DatabaseHelper dbHelper = new DatabaseHelper(ThanhToanActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase(); // Sử dụng getWritableDatabase() thay vì getReadableDatabase()
                Cursor cursor = db.query(
                        "KhachHang",
                        null,
                        "maTK = ?",
                        new String[]{String.valueOf(maTK)},
                        null,
                        null,
                        null
                );
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        String tenKH = cursor.getString(cursor.getColumnIndex("tenKH"));
                        String sdt = cursor.getString(cursor.getColumnIndex("SDT"));
                        String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                        cursor.close();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToanActivity.this);
                        builder.setTitle("Thông tin nhận hàng");
                        View view = getLayoutInflater().inflate(R.layout.dialog_thong_tin_nhan_hang, null);
                        TextInputEditText edtTenNguoiNhan = view.findViewById(R.id.edtTenNguoiNhan);
                        TextInputEditText edtSDT = view.findViewById(R.id.edtSDT);
                        TextInputEditText edtDiaChi = view.findViewById(R.id.edtDiaChi);
                        if (tenKH != null && !tenKH.isEmpty()) {
                            edtTenNguoiNhan.setText(tenKH);
                        }
                        if (sdt != null && !sdt.isEmpty()) {
                            edtSDT.setText(sdt);
                        }
                        if (diaChi != null && !diaChi.isEmpty()) {
                            edtDiaChi.setText(diaChi);
                        }

                        builder.setView(view);
                        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tenNguoiNhan = edtTenNguoiNhan.getText().toString();
                                String sdtNguoiNhan = edtSDT.getText().toString();
                                String diaChiNhanHang = edtDiaChi.getText().toString();
                                ContentValues values = new ContentValues();
                                values.put("maTK", maTK);
                                values.put("tenKH", tenNguoiNhan);
                                values.put("SDT", sdtNguoiNhan);
                                values.put("diaChi", diaChiNhanHang);
                                long result = db.insert("KhachHang", null, values);
                                if (result != -1) {
                                    Toast.makeText(ThanhToanActivity.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ThanhToanActivity.this, "Cập nhật thông tin không thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Hủy", null);
                        builder.show();
                    } else {
                        Toast.makeText(ThanhToanActivity.this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (db != null) {
                        db.close();
                    }
                    if (dbHelper != null) {
                        dbHelper.close();
                    }
                }
            }
        });

        btnDatHang = findViewById(R.id.btnDatHang);
        String diaChi = "123 Đường ABC, Quận XYZ, Thành phố HCM";
        textViewDiaChi.setText("Địa chỉ mua hàng: " + diaChi);
        ArrayList<GioHangItem> gioHangItems = getIntent().getParcelableArrayListExtra("selected_items");
        ThanhToanAdapter gioHangAdapter = new ThanhToanAdapter(gioHangItems);
        recyclerViewSanPham.setAdapter(gioHangAdapter);
        recyclerViewSanPham.setLayoutManager(new LinearLayoutManager(this));
        int tongTienHang = 0;
        for (GioHangItem item : gioHangItems) {
            tongTienHang += item.getSoLuong() * item.getGia();
        }
        textViewTongTienHang.setText(tongTienHang + " VNĐ");
        textViewTotalPrice.setText(tongTienHang + " VNĐ");
        textViewTongSL.setText("Số loại nước ngọt: " + gioHangItems.size());
        finalTongTienHang = tongTienHang;
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                int maKH = sharedPreferences.getInt("maTK", -1);
                if (textViewPhuongThucThanhToan.getText().toString().equals("Vui lòng chọn phương thức thanh toán")) {
                    Toast.makeText(ThanhToanActivity.this, "Vui lòng chọn phương thức thanh toán trước khi đặt hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseHelper dbHelper = new DatabaseHelper(ThanhToanActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(
                        "KhachHang",
                        null,
                        "maKH = ?",
                        new String[]{String.valueOf(maKH)},
                        null,
                        null,
                        null
                );
                if (cursor != null && cursor.moveToFirst()) {
                    String tenKH = cursor.getString(cursor.getColumnIndex("tenKH"));
                    String sdt = cursor.getString(cursor.getColumnIndex("SDT"));
                    String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                    cursor.close();
                    if (tenKH.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToanActivity.this);
                        builder.setTitle("Lỗi");
                        builder.setMessage("Vui lòng cung cấp đầy đủ thông tin của người nhận trước khi đặt hàng.");
                        builder.setPositiveButton("Đồng ý", null);
                        builder.show();
                    } else {
                        String tenSPDH = "";
                        String anhDH = getRandomProductImage(gioHangItems); // Lấy
                        int soLuong = 0;
                        int tongTien = finalTongTienHang;
                        ArrayList<GioHangItem> gioHangItems = getIntent().getParcelableArrayListExtra("selected_items");
                        for (GioHangItem item : gioHangItems) {
                            tenSPDH += item.getTenSP() + ", ";
                            soLuong += item.getSoLuong();
                        }
                        if (tenSPDH.endsWith(", ")) {
                            tenSPDH = tenSPDH.substring(0, tenSPDH.length() - 2);
                        }
                        String ngayMua = getCurrentDate();
                        ContentValues values = new ContentValues();
                        values.put("maKH", maKH);
                        values.put("maTTDH", 1);
                        values.put("tenDH", "Đơn hàng mới");
                        values.put("tenSPDH", tenSPDH);
                        values.put("anhDH", anhDH);
                        values.put("ngayMua", ngayMua);
                        values.put("soLuong", soLuong);
                        values.put("tongTien", tongTien);
                        long result = db.insert("DonMua", null, values);
                        if (result != -1) {
                            Toast.makeText(ThanhToanActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                            SharedPreferences sh = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                            maKH = sh.getInt("maTK", -1);
                            xoaGioHangTheoMaKH(maKH);
                            gioHangAdapter.notifyDataSetChanged();
                            finish();
                        } else {
                            Toast.makeText(ThanhToanActivity.this, "Đặt hàng không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ThanhToanActivity.this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
                }
                db.close();
                dbHelper.close();
            }
        });
        loadDonMuaData();
    }

    private void xoaGioHangTheoMaKH(int maKH) {
        DatabaseHelper dbHelper = new DatabaseHelper(ThanhToanActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection = {"maSP", "soLuong"};
        String selection = "maKH=?";
        String[] selectionArgs = {String.valueOf(maKH)};
        Cursor cursor = db.query("GioHang", projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndex("maSP"));
                int soLuongMua = cursor.getInt(cursor.getColumnIndex("soLuong"));
                String ctspSelection = "maSP=?";
                String[] ctspSelectionArgs = {String.valueOf(maSP)};
                Cursor ctspCursor = db.query("ChiTietSanPham", new String[]{"soLuong"}, ctspSelection, ctspSelectionArgs, null, null, null);
                if (ctspCursor != null && ctspCursor.moveToFirst()) {
                    int soLuongHienCo = ctspCursor.getInt(ctspCursor.getColumnIndex("soLuong"));
                    ctspCursor.close();
                    int soLuongMoi = soLuongHienCo - soLuongMua;
                    ContentValues ctspValues = new ContentValues();
                    ctspValues.put("soLuong", soLuongMoi);
                    int ctspRowsAffected = db.update("ChiTietSanPham", ctspValues, "maSP=?", new String[]{String.valueOf(maSP)});
                    if (ctspRowsAffected > 0) {
                        ContentValues spValues = new ContentValues();
                        spValues.put("soLuong", soLuongMoi);
                        int spRowsAffected = db.update("SanPham", spValues, "maSP=?", new String[]{String.valueOf(maSP)});
                        if (spRowsAffected > 0) {

                        } else {

                        }
                    } else {

                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        String deleteSelection = "maKH=?";
        String[] deleteSelectionArgs = {String.valueOf(maKH)};
        int rowsDeleted = db.delete("GioHang", deleteSelection, deleteSelectionArgs);
        if (rowsDeleted > 0) {

        } else {

        }
        db.close();
    }




    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getRandomProductImage(ArrayList<GioHangItem> gioHangItems) {
        if (gioHangItems != null && gioHangItems.size() > 0) {
            int randomIndex = new Random().nextInt(gioHangItems.size());
            GioHangItem randomProduct = gioHangItems.get(randomIndex);
            return randomProduct.getAnhSP();
        }
        return null;
    }

    private int calculateFinalTongTienHang() {
        double phiVanChuyen = 0;
        String selectedPTTT = textViewPhuongThucThanhToan.getText().toString();
        if (selectedPTTT.equals("Phương thức thanh toán: Thanh toán khi nhận hàng")) {
            phiVanChuyen = 0.05 * finalTongTienHang;
        } else if (selectedPTTT.equals("Phương thức thanh toán: Thanh toán qua ngân hàng")) {
            phiVanChuyen = 0.02 * finalTongTienHang;
        }
        double tongThanhToan = finalTongTienHang + phiVanChuyen;
        textViewPhiVanChuyen.setText(phiVanChuyen + " VNĐ");
        textViewTongThanhToan.setText(tongThanhToan + " VNĐ");
        textViewTotalPrice.setText(tongThanhToan + " VNĐ");
        return (int) tongThanhToan;
    }


    private void loadDonMuaData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1); // Lấy mã khách hàng từ SharedPreferences
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "KhachHang",
                null,
                "maKH = ?",
                new String[]{String.valueOf(maKH)},
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenKH = cursor.getString(cursor.getColumnIndex("tenKH"));
                String sdt = cursor.getString(cursor.getColumnIndex("SDT"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                if (diaChi != null && !diaChi.isEmpty()) {
                    textViewDiaChi.setText("Địa chỉ mua hàng: " + diaChi);
                } else {
                    textViewDiaChi.setText("Địa chỉ mua hàng: (Trống)");
                }
                if (sdt != null && !sdt.isEmpty()) {
                    textViewSDT.setText("Số điện thoại: " + sdt);
                } else {
                    textViewSDT.setText("Số điện thoại: (Trống)");
                }

                textViewNguoiNhan.setText("Người nhận: " + tenKH);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            textViewDiaChi.setText("Địa chỉ mua hàng: (Trống)");
            textViewSDT.setText("Số điện thoại: (Trống)");
            textViewNguoiNhan.setText("Người nhận: (Trống)");
        }
        db.close();
        dbHelper.close();
    }

}
