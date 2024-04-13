package com.nuocngot.tvdpro.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.QLUserAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.QLUser;

import java.util.ArrayList;

public class QuanLyUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<QLUser> khachHangs = new ArrayList<>();
    private QLUserAdapter adapter = new QLUserAdapter(khachHangs);
    private Toolbar toolbar;
    private FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_user);
        recyclerView = findViewById(R.id.recycleViewUser);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Quản lý người dùng");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fabAddUser = findViewById(R.id.fabAddUser);
        fabAddUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogAddUser();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadKhachHangData();
    }

    private void showDialogAddUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextSDT);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextDiaChi);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);

        builder.setTitle("Thêm người dùng");
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!username.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !address.isEmpty() && !password.isEmpty()) {
                    saveUserToDatabase(username, username, password, email, phoneNumber, address, "1");
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void saveUserToDatabase(String tenND, String username, String password, String email, String sdt, String diaChi, String capTV) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            boolean userExists = checkUserExists(db, email, sdt);
            if (!userExists) {
                ContentValues nguoiDungValues = new ContentValues();
                nguoiDungValues.put("tenND", tenND);
                nguoiDungValues.put("email", email);
                nguoiDungValues.put("sdt", sdt);
                nguoiDungValues.put("diaChi", diaChi);
                nguoiDungValues.put("capTV", capTV);
                nguoiDungValues.put("trangThai", 1);

                // Thêm dữ liệu vào bảng NguoiDung
                long nguoiDungId = db.insert("NguoiDung", null, nguoiDungValues);

                if (nguoiDungId != -1) {
                    Toast.makeText(this, "Người dùng đã được thêm thành công", Toast.LENGTH_SHORT).show();
                    ArrayList<QLUser> updatedList = loadQLUserList(this);
                    updateQLUserList(updatedList);
                } else {
                    Toast.makeText(this, "Lỗi khi thêm người dùng", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AddUserError", "Error adding user: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi thêm người dùng", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
            dbHelper.close();
        }
    }


    private boolean checkUserExists(SQLiteDatabase db, String email, String sdt) {
        Cursor cursor = db.query(
                "NguoiDung",
                null,
                "email = ? OR sdt = ?",
                new String[] { email, sdt },
                null,
                null,
                null
        );
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }

    private ArrayList<QLUser> loadQLUserList(Context context) {
        ArrayList<QLUser> list = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "NguoiDung",
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maND = cursor.getInt(cursor.getColumnIndex("maND")); // Sử dụng "maND" thay vì "maKH"
                String tenND = cursor.getString(cursor.getColumnIndex("tenND")); // Sử dụng "tenND" thay vì "tenKH"
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                String capTV = cursor.getString(cursor.getColumnIndex("capTV"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                QLUser qlUser = new QLUser(maND, tenND, email, sdt, diaChi, capTV, hinhAnh); // Sử dụng "maND" và "tenND"
                list.add(qlUser);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }


    private void updateQLUserList(ArrayList<QLUser> updatedList) {
        khachHangs.clear();
        khachHangs.addAll(updatedList);
        adapter.notifyDataSetChanged();
    }

    private void loadKhachHangData() {
        khachHangs.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "NguoiDung", // Thay "KhachHang" thành "NguoiDung"
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maND = cursor.getInt(cursor.getColumnIndex("maND"));
                String tenKH = cursor.getString(cursor.getColumnIndex("tenND"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                String capTV = cursor.getString(cursor.getColumnIndex("capTV"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                QLUser khachHang = new QLUser(maND, tenKH, email, sdt, diaChi, capTV, hinhAnh);
                khachHangs.add(khachHang);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }


}