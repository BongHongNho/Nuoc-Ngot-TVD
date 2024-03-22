package com.nuocngot.tvdpro.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    public TextInputEditText edEmailPhone, adUsername, edPassword, edRepassword;
    public Button btnRegister;

    public TextView gotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edEmailPhone = findViewById(R.id.edEmailPhone);
        adUsername = findViewById(R.id.edUsenameRE);
        edPassword = findViewById(R.id.edPasswordRE);
        edRepassword = findViewById(R.id.edCfPasswordRE);
        btnRegister = findViewById(R.id.btnnRegister);
        gotoLogin = findViewById(R.id.tvResgiter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhone = edEmailPhone.getText().toString().trim();
                String username = adUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                String repassword = edRepassword.getText().toString().trim();
                if (emailOrPhone.isEmpty() || username.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(repassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và xác nhận mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkUsernameExists(username)) {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (createAccount(emailOrPhone, username, password)) {
                    Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi khi tạo tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkUsernameExists (String username){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM TaiKhoan WHERE tenDN = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private boolean isEmail (String str){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return str.matches(emailPattern);
    }

    private boolean isPhoneNumber (String str){
        String phonePattern = "[0-9]{10,11}";
        return str.matches(phonePattern);
    }

    private boolean createAccount(String emailOrPhone, String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;
        ContentValues taiKhoanValues = new ContentValues();
        taiKhoanValues.put("tenDN", username);
        taiKhoanValues.put("matKhau", password);
        taiKhoanValues.put("Email", isEmail(emailOrPhone) ? emailOrPhone : "");
        taiKhoanValues.put("SDT", isPhoneNumber(emailOrPhone) ? emailOrPhone : "");
        taiKhoanValues.put("role", "user");
        taiKhoanValues.put("userId", -1);
        taiKhoanValues.put("isLogin", 0);
        long maTK = db.insert("TaiKhoan", null, taiKhoanValues);
        if (maTK != -1) {
            ContentValues khachHangValues = new ContentValues();
            khachHangValues.put("maTK", maTK);
            khachHangValues.put("tenKH", username);
            khachHangValues.put("Email", isEmail(emailOrPhone) ? emailOrPhone : "");
            khachHangValues.put("SDT", isPhoneNumber(emailOrPhone) ? emailOrPhone : "");
            khachHangValues.put("diaChi", "");
            khachHangValues.put("capTV", "");
            khachHangValues.put("hinhAnh", "");
            long khachHangId = db.insert("KhachHang", null, khachHangValues);
            if (khachHangId != -1) {
                success = true;
            } else {
                db.delete("TaiKhoan", "maTK = ?", new String[]{String.valueOf(maTK)});
            }
        }

        db.close();
        return success;
    }
}