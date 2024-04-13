package com.nuocngot.tvdpro.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.BinhLuanAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.BinhLuan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChiTietSPActivity extends AppCompatActivity {
    private int maSP;
    private TextView textViewProductName, textViewProductOrigin, textViewProductInfo, textViewProductPrice, textViewProductQuantity;
    private ImageView imageViewProduct;
    private Toolbar toolbar;

    private ArrayList<BinhLuan> arrayList = new ArrayList<>();

    BinhLuanAdapter binhLuanAdapter = new BinhLuanAdapter(this, arrayList);

    private RecyclerView recycleBinhLuan;

    private TextView textViewNoBL;

    private TextInputEditText editTextBinhLuan;

    private Button btnBinhLuan;

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
        recycleBinhLuan = findViewById(R.id.recycleBinhLuan);
        editTextBinhLuan = findViewById(R.id.editTextNoiDung);
        btnBinhLuan = findViewById(R.id.btnBinhLuan);
        textViewNoBL = findViewById(R.id.textViewNoBL);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chi tiết sản phẩm");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Button buttonAddToCart = findViewById(R.id.buttonAddToCart);
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

        btnBinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(editTextBinhLuan.getText().toString().isEmpty()){
                   Toast.makeText(ChiTietSPActivity.this, "Vui lòng không để trống bình luận", Toast.LENGTH_SHORT).show();
               }else{
                   addBinhLuan();
               }
            }
        });
        recycleBinhLuan.setAdapter(binhLuanAdapter);
        recycleBinhLuan.setLayoutManager(new LinearLayoutManager(this));
        loadBinhLuan();
    }

    private void addBinhLuan() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        if (maKH == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        String tenND = "";
        String anhBL = "";
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT tenKH, hinhAnh FROM KhachHang WHERE maKH = ?", new String[]{String.valueOf(maKH)});
        if (cursor.moveToFirst()) {
            tenND = cursor.getString(cursor.getColumnIndex("tenKH"));
            anhBL = cursor.getString(cursor.getColumnIndex("hinhAnh"));
        }
        cursor.close();
        int maSP = getIntent().getIntExtra("maSP", -1);
        if (maSP == -1) {
            Toast.makeText(this, "Không tìm thấy mã sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        String binhLuan = editTextBinhLuan.getText().toString().trim();
        String thoiGian = getCurrentDateTime();
        String query = "INSERT INTO BinhLuan (maKH, maSP, tenND, anhBL, binhLuan, thoiGian) VALUES (?, ?, ?, ?, ?, ?)";
        database.execSQL(query, new Object[]{maKH, maSP, tenND, anhBL, binhLuan, thoiGian});
        Toast.makeText(this, "Thêm bình luận thành công", Toast.LENGTH_SHORT).show();
        editTextBinhLuan.setText("");
        arrayList.clear();
        loadBinhLuan();
    }

    private void loadBinhLuan() {
        SQLiteDatabase database = new DatabaseHelper(this).getReadableDatabase();
        int maSP = getIntent().getIntExtra("maSP", -1);
        String query = "SELECT * FROM BinhLuan WHERE maSP = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maSP)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenND = cursor.getString(cursor.getColumnIndex("tenND"));
                String binhLuan = cursor.getString(cursor.getColumnIndex("binhLuan"));
                String thoiGian = cursor.getString(cursor.getColumnIndex("thoiGian"));
                String anhBL = cursor.getString(cursor.getColumnIndex("anhBL"));
                int maKH = cursor.getInt(cursor.getColumnIndex("maKH"));
                int maBL = cursor.getInt(cursor.getColumnIndex("maBL"));
                BinhLuan binhLuanObj = new BinhLuan(tenND, binhLuan, maBL, thoiGian, anhBL, maKH);
                arrayList.add(binhLuanObj);
            } while (cursor.moveToNext());
            cursor.close();
            binhLuanAdapter.notifyDataSetChanged();
        }
    }




    private String getCurrentDateTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
            return currentDateTime.format(formatter);
        } else {
            return "";
        }
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
            productPrice = "Giá sản phẩm: " + cursor.getString(cursor.getColumnIndex("gia")) + " VND";
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
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        if (maKH != -1) {
            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng hay chưa
            Cursor cursor = db.rawQuery("SELECT * FROM GioHang WHERE maSP = ? AND maKH = ?", new String[]{String.valueOf(maSP), String.valueOf(maKH)});
            if (cursor.moveToFirst()) {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng của nó
                int soLuongHienTai = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int soLuongMoi = soLuongHienTai + 1;
                ContentValues values = new ContentValues();
                values.put("soLuong", soLuongMoi);
                db.update("GioHang", values, "maSP = ? AND maKH = ?", new String[]{String.valueOf(maSP), String.valueOf(maKH)});
            } else {
                // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào giỏ hàng
                Cursor cursorSanPham = db.rawQuery("SELECT * FROM SanPham WHERE maSP = ?", new String[]{String.valueOf(maSP)});
                if (cursorSanPham.moveToFirst()) {
                    String tenSP = cursorSanPham.getString(cursorSanPham.getColumnIndex("tenSP"));
                    String hinhAnh = cursorSanPham.getString(cursorSanPham.getColumnIndex("hinhAnh"));
                    int giaSP = cursorSanPham.getInt(cursorSanPham.getColumnIndex("gia"));
                    int soLuong = 1;
                    int tongTien = soLuong * giaSP;

                    ContentValues values = new ContentValues();
                    values.put("tenSP", tenSP);
                    values.put("hinhAnh", hinhAnh);
                    values.put("maKH", maKH);
                    values.put("maSP", maSP);
                    values.put("soLuong", soLuong);
                    values.put("tongTien", tongTien);
                    db.insert("GioHang", null, values);
                }
                cursorSanPham.close();
            }
            cursor.close();
            Toast.makeText(this, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            Intent gioHangIntent = new Intent(this, GioHangActivity.class);
            startActivity(gioHangIntent);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
        }
        db.close();
        dbHelper.close();
    }
}
