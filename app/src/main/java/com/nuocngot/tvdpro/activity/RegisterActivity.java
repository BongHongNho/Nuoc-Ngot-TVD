package com.nuocngot.tvdpro.activity;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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

    public TextView gotoLogin, textViewRegister;

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
        textViewRegister = findViewById(R.id.textViewRegister);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textViewRegister, "translationY", 200f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhone = edEmailPhone.getText().toString().trim();
                String username = adUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                String repassword = edRepassword.getText().toString().trim();

                // Kiểm tra các trường nhập liệu
                if (emailOrPhone.isEmpty() || username.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra độ dài tối thiểu cho mật khẩu
                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra mật khẩu và xác nhận mật khẩu có trùng khớp
                if (!password.equals(repassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu và xác nhận mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra tính hợp lệ của tên đăng nhập
                if (checkUsernameExists(username)) {
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tiến hành tạo tài khoản
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

    private boolean createAccount(String emailOrPhone, String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;
        ContentValues nguoiDungValues = new ContentValues();
        nguoiDungValues.put("tenDN", username);
        nguoiDungValues.put("tenND", username);
        nguoiDungValues.put("matKhau", password);
        nguoiDungValues.put("email", isEmail(emailOrPhone) ? emailOrPhone : "");
        nguoiDungValues.put("sdt", isPhoneNumber(emailOrPhone) ? emailOrPhone : "");
        nguoiDungValues.put("diaChi", "");
        nguoiDungValues.put("capTV", "");
        nguoiDungValues.put("trangThai", 1);
        nguoiDungValues.put("role", "user");
        nguoiDungValues.put("isLogin", 0);
        long maND = db.insert("NguoiDung", null, nguoiDungValues);
        if (maND != -1) {
            success = true;
        }
        db.close();
        return success;
    }
    private boolean checkUsernameExists(String username) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NguoiDung WHERE tenDN = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    private boolean isEmail(String str) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return str.matches(emailPattern);
    }

    private boolean isPhoneNumber(String str) {
        String phonePattern = "[0-9]{10,11}";
        return str.matches(phonePattern);
    }

}
