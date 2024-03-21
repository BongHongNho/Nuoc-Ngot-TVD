package com.nuocngot.tvdpro.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.activity.LoginActivity;
import com.nuocngot.tvdpro.adapter.Admin;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tenTextView = view.findViewById(R.id.tenTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        sdtTextView = view.findViewById(R.id.sdtTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        TaiKhoan taiKhoan = queryTaiKhoanFromDatabase();
        if (taiKhoan != null) {
            tenTextView.setText("Tên đăng nhập: " + taiKhoan.getTenDN());
            emailTextView.setText("Email: " + taiKhoan.getEmail());
            sdtTextView.setText("Số điện thoại: " + taiKhoan.getSdt());
            roleTextView.setText("Vai trò: " +taiKhoan.getRole());
            Glide.with(getContext()).load(taiKhoan.getAvatarUrl()).placeholder(R.drawable.img_avatar_nam).into(avatarImageView);
        }
        return view;
    }

    private TaiKhoan queryTaiKhoanFromDatabase() {
        TaiKhoan taiKhoan = null;

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getReadableDatabase();
            cursor = db.query("TaiKhoan", null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int maTKIndex = cursor.getColumnIndex("maTK");
                int tenDNIndex = cursor.getColumnIndex("tenDN");
                int emailIndex = cursor.getColumnIndex("Email");
                int sdtIndex = cursor.getColumnIndex("SDT");
                int roleIndex = cursor.getColumnIndex("role");
                int maTK = cursor.getInt(maTKIndex);
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

}