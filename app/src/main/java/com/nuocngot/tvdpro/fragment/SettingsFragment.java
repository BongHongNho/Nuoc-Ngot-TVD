package com.nuocngot.tvdpro.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.activity.LoginActivity;
import com.nuocngot.tvdpro.adapter.Admin;
import com.nuocngot.tvdpro.adapter.FunctionAdapter;
import com.nuocngot.tvdpro.adapter.TaiKhoan;
import com.nuocngot.tvdpro.adapter.stAdpater;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.stListViewAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView tenTextView;
    private TextView emailTextView;
    private TextView sdtTextView;
    private TextView roleTextView;

    private RecyclerView functionRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tenTextView = view.findViewById(R.id.tenTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        sdtTextView = view.findViewById(R.id.sdtTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        functionRecyclerView = view.findViewById(R.id.functionRecyclerView);
        setUpRecyclerView();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maTK = sharedPreferences.getInt("maTK", -1);
        TaiKhoan taiKhoan = queryTaiKhoanFromDatabase(maTK);
        if (taiKhoan != null) {
            tenTextView.setText("Tên đăng nhập: " + taiKhoan.getTenDN());
            emailTextView.setText("Email: " + taiKhoan.getEmail());
            sdtTextView.setText("Số điện thoại: " + taiKhoan.getSdt());
            roleTextView.setText("Vai trò: " + taiKhoan.getRole());
            String profileImageUrl = queryProfileImageFromDatabase(maTK);
            if (profileImageUrl != null) {
                Glide.with(getContext()).load(profileImageUrl).placeholder(R.drawable.img_avatar_nam).into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.img_avatar_nam);
            }
        }
        return view;
    }

    private TaiKhoan queryTaiKhoanFromDatabase(int maTK) {
        TaiKhoan taiKhoan = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getReadableDatabase();
            String[] projection = {
                    "tenDN",
                    "Email",
                    "SDT",
                    "role"
            };
            String selection = "maTK = ?";
            String[] selectionArgs = {String.valueOf(maTK)};
            cursor = db.query("TaiKhoan", projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int tenDNIndex = cursor.getColumnIndex("tenDN");
                int emailIndex = cursor.getColumnIndex("Email");
                int sdtIndex = cursor.getColumnIndex("SDT");
                int roleIndex = cursor.getColumnIndex("role");
                String tenDN = cursor.getString(tenDNIndex);
                String email = cursor.getString(emailIndex);
                String sdt = cursor.getString(sdtIndex);
                String role = cursor.getString(roleIndex);
                taiKhoan = new TaiKhoan(maTK, tenDN, email, sdt, role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return taiKhoan;
    }

    private String queryProfileImageFromDatabase(int maTK) {
        String profileImage = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getReadableDatabase();
            String[] projection = {
                    "hinhAnh"
            };
            String selection = "maTK = ?";
            String[] selectionArgs = {String.valueOf(maTK)};
            cursor = db.query("KhachHang", projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int profileImageIndex = cursor.getColumnIndex("hinhAnh");
                profileImage = cursor.getString(profileImageIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return profileImage;
    }
    private void refreshUserData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maTK = sharedPreferences.getInt("maTK", -1);
        TaiKhoan taiKhoan = queryTaiKhoanFromDatabase(maTK);
        if (taiKhoan != null) {
            tenTextView.setText("Tên đăng nhập: " + taiKhoan.getTenDN());
            emailTextView.setText("Email: " + taiKhoan.getEmail());
            sdtTextView.setText("Số điện thoại: " + taiKhoan.getSdt());
            roleTextView.setText("Vai trò: " + taiKhoan.getRole());
            String profileImageUrl = queryProfileImageFromDatabase(maTK);
            if (profileImageUrl != null) {
                updateProfileImageInDatabase(maTK, profileImageUrl); // Di chuyển phần này lên đây
                Glide.with(getContext()).load(profileImageUrl).placeholder(R.drawable.img_avatar_nam).into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.img_avatar_nam);
            }
        }
    }


    private void updateProfileImageInDatabase(int maTK, String newProfileImage) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hinhAnh", newProfileImage);
            String selection = "maTK = ?";
            String[] selectionArgs = {String.valueOf(maTK)};
            int rowsAffected = db.update("KhachHang", values, selection, selectionArgs);
            if (rowsAffected > 0) {
                refreshUserData();
                Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Không thể cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private void setUpRecyclerView() {
        ArrayList<String> functionList = new ArrayList<>();
        functionList.add("Đổi ảnh đại diện");
        functionList.add("Đổi tên người dùng");
        functionList.add("Đổi mật khẩu");
        functionList.add("Nâng cấp tài khoản");
        functionList.add("Chức năng ADMIN");
        FunctionAdapter adapter = new FunctionAdapter(functionList);
        functionRecyclerView.setAdapter(adapter);
        functionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}