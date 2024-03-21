package com.nuocngot.tvdpro;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout layout_login;

    private TextView tvForget, tvRegister;

    private Button buttonLogin;

    public TextInputEditText edUsenameLG, edPasswordLG;

    public CheckBox cbRemember;

    public MaterialCardView cvLogin;

    public static boolean isLogin;

    public String NAME_SHARED_PREFERENCES = "manager";
    public String IS_LOGIN = "isLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login = findViewById(R.id.layout_login);
        tvForget = findViewById(R.id.tvForget);
        tvRegister = findViewById(R.id.tvResgiter);
        edPasswordLG = findViewById(R.id.edPasswordLG);
        edUsenameLG = findViewById(R.id.edUsenameLG);
        cvLogin = findViewById(R.id.cvLogin);
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.getBoolean(IS_LOGIN, false);
        loadPreferences();
        if(isLogin) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show();
        }
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login with Google", Toast.LENGTH_SHORT).show();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin = findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String edUs = edUsenameLG.getText().toString().trim();
                final String edPw = edPasswordLG.getText().toString().trim();
                if (edUs.isEmpty() || edPw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edUs.length() < 3 || edPw.length() < 3) {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu quá ngắn", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userType = getUserType(edUs, edPw);
                if (userType != null) {
                    if (userType.equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        SharedPreferences.Editor editor = getSharedPreferences(NAME_SHARED_PREFERENCES, MODE_PRIVATE).edit();
                        editor.putBoolean(IS_LOGIN, true);
                        editor.apply();
                    } else if (userType.equals("user")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        SharedPreferences.Editor editor = getSharedPreferences(NAME_SHARED_PREFERENCES, MODE_PRIVATE).edit();
                        editor.putBoolean(IS_LOGIN, true);
                        editor.apply();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_login, "translationX", 200f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        layout_login.setVisibility(View.VISIBLE);
        animator.start();
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPassword();
            }
        });
    }
    private String getUserType(String username, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userType = null;
        Cursor cursor = db.rawQuery("SELECT loaiTK FROM TaiKhoan WHERE tenDN = ? AND matKhau = ?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndex("loaiTK"));
        }
        cursor.close();
        db.close();
        return userType;
    }
    private void showForgetPassword() {
        View view = getLayoutInflater().inflate(R.layout.reset_password, null);
        final TextInputEditText editTextEmailOrPhone = view.findViewById(R.id.edEmailPhoneForget); // ID của EditText trong reset_password.xml
        new AlertDialog.Builder(this)
                .setTitle("Quên mật khẩu")
                .setView(view)
                .setMessage("Vui lòng nhập email hoặc số điện thoại để lấy lại mật khẩu")
                .setPositiveButton("Đồng  ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String emailOrPhone = editTextEmailOrPhone.getText().toString().trim();
                        if (emailOrPhone.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Vui lòng nhập email hoặc số điện thoại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isExist = checkEmailOrPhoneExist(emailOrPhone);
                        if (isExist) {
                            String newPassword = generateRandomPassword();
                            updatePassword(emailOrPhone, newPassword);
                        } else {
                            Toast.makeText(LoginActivity.this, "Email hoặc số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private boolean checkEmailOrPhoneExist(String emailOrPhone) {
        boolean isExist = false;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM TaiKhoan WHERE Email = ? OR SDT = ?";
        Cursor cursor = db.rawQuery(query, new String[]{emailOrPhone, emailOrPhone});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        db.close();
        return isExist;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            newPassword.append(chars.charAt(index));
        }
        return newPassword.toString();
    }
    private void updatePassword(String emailOrPhone, String newPassword) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matKhau", newPassword);
        String whereClause = "Email = ? OR SDT = ?";
        String[] whereArgs = {emailOrPhone, emailOrPhone};
        int rowsAffected = db.update("TaiKhoan", values, whereClause, whereArgs);
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Mật khẩu mới của bạn là: " + newPassword, Toast.LENGTH_LONG).show();
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("New Password", newPassword);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Mật khẩu mới đã được sao chép vào clipboard", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Cập nhật mật khẩu không thành công", Toast.LENGTH_SHORT).show();
        }
    }


    public void loadPreferences () {
        SharedPreferences preferences = getSharedPreferences(NAME_SHARED_PREFERENCES, MODE_PRIVATE);
        isLogin = preferences.getBoolean(IS_LOGIN, false);
    }
}