package com.nuocngot.tvdpro.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

public class ChiTietSPActivity extends AppCompatActivity {
    private int maSP;
    private TextView textViewProductName, textViewProductOrigin, textViewProductInfo, textViewProductPrice, textViewProductQuantity;
    private ImageView imageViewProduct;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sp);
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewProductOrigin = findViewById(R.id.textViewProductOrigin);
        textViewProductInfo = findViewById(R.id.textViewProductInfo);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewProductPrice = findViewById(R.id.textViewProductPrice);
        textViewProductQuantity = findViewById(R.id.textViewProductQualaty);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chi tiết sản phẩm");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
            });
        Button buttonAddToCart = findViewById(R.id.buttonAddToCart); // Lấy tham chiếu đến nút "Thêm vào giỏ hàng"
        Intent intent = getIntent();
        maSP = intent.getIntExtra("maSP", -1);
        if (maSP != -1) {
            showProductDetails();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
        }
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartAndNavigateToCartActivity(maSP);
            }
        });
    }
    private void showProductDetails() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String productName = "";
        String productOrigin = "";
        String productInfo = "";
        String productPrice = "";
        String productQuantity = "";
        String imageURL = "";
        Cursor cursor = db.rawQuery("SELECT tenSP, xuatXu, thongTinSP, hinhAnh, gia, soLuong FROM ChiTietSanPham WHERE maSP = ?", new String[]{String.valueOf(maSP)});
        if (cursor.moveToFirst()) {
            productName = cursor.getString(cursor.getColumnIndex("tenSP"));
            String origin = cursor.getString(cursor.getColumnIndex("xuatXu"));
            productOrigin = "Xuất xứ: " + origin;
            productInfo = "Thông tin sản phẩm: " + cursor.getString(cursor.getColumnIndex("thongTinSP"));
            imageURL = cursor.getString(cursor.getColumnIndex("hinhAnh"));
            productPrice = "Giá: " + cursor.getString(cursor.getColumnIndex("gia")) + " VND";
            productQuantity = "Số lượng: " + cursor.getString(cursor.getColumnIndex("soLuong"));
        }
        cursor.close();
        dbHelper.close();
        textViewProductName.setText(productName);
        textViewProductOrigin.setText(productOrigin);
        textViewProductInfo.setText(productInfo);
        textViewProductPrice.setText(productPrice);
        textViewProductQuantity.setText(productQuantity);
        Glide.with(this).load(imageURL).placeholder(R.drawable.placeholder).into(imageViewProduct);
    }


    private void addToCartAndNavigateToCartActivity(int maSP) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE maSP = ?", new String[]{String.valueOf(maSP)});
        if (cursor.moveToFirst()) {
            String tenSP = cursor.getString(cursor.getColumnIndex("tenSP"));
            String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
            int giaSP = cursor.getInt(cursor.getColumnIndex("gia"));
            int soLuong = 1;
            int tongTien = soLuong * giaSP;
            SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
            int maKH = sharedPreferences.getInt("maKH", -1);
            if (maKH != -1) {
                ContentValues values = new ContentValues();
                values.put("tenSP", tenSP);
                values.put("hinhAnh", hinhAnh);
                values.put("maKH", maKH);
                values.put("maSP", maSP);
                values.put("soLuong", soLuong);
                values.put("tongTien", tongTien);
                long newRowId = db.insert("GioHang", null, values);

                if (newRowId != -1) {
                    Toast.makeText(this, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm sản phẩm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
        dbHelper.close();
        Intent gioHangIntent = new Intent(this, GioHangActivity.class);
        startActivity(gioHangIntent);
    }

}
