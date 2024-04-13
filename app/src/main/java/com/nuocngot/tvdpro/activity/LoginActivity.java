package com.nuocngot.tvdpro.activity;// LoginActivity.java

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextUsername, editTextPassword;
    private CheckBox checkBoxRemember;

    private boolean checkBoxChecked = false;
    private Button buttonLogin;
    private TextView textViewForgotPassword, textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUsername.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (editTextPassword.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu quá ngắn", Toast.LENGTH_SHORT).show();
                } else if (editTextUsername.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Username quá ngắn", Toast.LENGTH_SHORT).show();

                } else {
                    loginUser();
                }
            }
        });
        CheckBox checkBoxRemember = findViewById(R.id.checkBoxRemember);
        checkBoxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("checkbox", isChecked);
                if (isChecked) {
                    editor.putString("username", editTextUsername.getText().toString());
                    editor.putString("password", editTextPassword.getText().toString());
                } else {
                    editor.remove("username");
                    editor.remove("password");
                }
                editor.apply();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        boolean checkboxChecked = sharedPreferences.getBoolean("checkbox", false);
        checkBoxRemember.setChecked(checkboxChecked);
        if (checkboxChecked) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            editTextUsername.setText(savedUsername);
            editTextPassword.setText(savedPassword);
        }

        textViewForgotPassword.setOnClickListener(view -> forgotPassword());
        checkLoginStatus();
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "maND",
                "tenDN",
                "matKhau",
                "role"
        };

        String selection = "tenDN = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(
                "NguoiDung",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int maND = cursor.getInt(cursor.getColumnIndexOrThrow("maND"));
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow("matKhau"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            if (password.equals(storedPassword)) {
                // Đăng nhập thành công
                saveLoginStatus(true, maND, role);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        dbHelper.close();
    }

    private void saveLoginStatus(boolean isLoggedIn, int maND, String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putInt("maND", maND);
        editor.putString("role", role);
        editor.apply();
    }


    private void forgotPassword() {
        View view = LayoutInflater.from(this).inflate(R.layout.reset_password, null);
        TextInputEditText edEmailandPhone = view.findViewById(R.id.edEmailPhoneForget);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quên mật khẩu");
        builder.setView(view);
        builder.setMessage("Vui lòng nhập email hoac số điện thoại để đặt lại mật khẩu");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailPhone = edEmailandPhone.getText().toString();
                resetPassword(emailPhone);
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void resetPassword(String emailPhone) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "maTK",
                "tenDN",
                "matKhau"
        };
        String selection = "tenDN = ?";
        String[] selectionArgs = {emailPhone};
        Cursor cursor = db.query(
                "TaiKhoan",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            int maTK = cursor.getInt(cursor.getColumnIndexOrThrow("maTK"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("tenDN"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("matKhau"));
            String newPassword = "123456";
            String updateQuery = "UPDATE TaiKhoan SET matKhau = ? WHERE maTK = ?";
            db.execSQL(updateQuery, new Object[]{newPassword, maTK});
            Toast.makeText(this, "Mật khẩu đã được reset thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Không tìm thấy email hoặc số điện thoại", Toast.LENGTH_SHORT).show();
        }
    }
}
