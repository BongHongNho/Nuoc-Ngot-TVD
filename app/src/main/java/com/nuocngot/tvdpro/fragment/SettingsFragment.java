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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.activity.DonHangActivity;
import com.nuocngot.tvdpro.activity.LichSuMuaHangActivity;
import com.nuocngot.tvdpro.activity.LoginActivity;
import com.nuocngot.tvdpro.adapter.BuyAcitivyAdapter;
import com.nuocngot.tvdpro.adapter.BuyActivityItem;
import com.nuocngot.tvdpro.adapter.FunctionAdapter;
import com.nuocngot.tvdpro.model.NguoiDung;
import com.nuocngot.tvdpro.model.TaiKhoan;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView tenTextView;
    private TextView emailTextView;
    private TextView sdtTextView;
    private TextView roleTextView;
    private ImageView veryfied ,khungVIPImageView;
    private Button btnLogOut;
    private LinearLayout btnLichSuMua, linearDonMua;
    private RecyclerView functionRecyclerView,historyRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tenTextView = view.findViewById(R.id.tenTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        sdtTextView = view.findViewById(R.id.sdtTextView);
        veryfied = view.findViewById(R.id.veryfied);
        khungVIPImageView = view.findViewById(R.id.khungVIPImageView);
        roleTextView = view.findViewById(R.id.roleTextView);
        avatarImageView = view.findViewById(R.id.avatarImageView);
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        functionRecyclerView = view.findViewById(R.id.functionRecyclerView);
        btnLichSuMua = view.findViewById(R.id.btnLichSuMua);
        linearDonMua = view.findViewById(R.id.linearDonMua);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLichSuMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LichSuMuaHangActivity.class);
                startActivity(intent);
            }
        });
        linearDonMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DonHangActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maND = sharedPreferences.getInt("maND", -1);
        String role = sharedPreferences.getString("role", "");

        if (role.equals("admin")) {
            veryfied.setVisibility(View.VISIBLE);
            khungVIPImageView.setImageResource(R.drawable.khung_vip_02);
        } else {
            veryfied.setVisibility(View.INVISIBLE);
            khungVIPImageView.setImageResource(R.drawable.khung_vip_03);
        }

        NguoiDung nguoiDung = queryNguoiDungFromDatabase(maND);
        if (nguoiDung != null) {
            tenTextView.setText("Tên đăng nhập: " + nguoiDung.getTenDN());
            emailTextView.setText("Email: " + nguoiDung.getEmail());
            sdtTextView.setText("Số điện thoại: " + nguoiDung.getSdt());
            roleTextView.setText("Vai trò: " + nguoiDung.getRole());
            String profileImageUrl = queryProfileImageFromDatabase(maND);
            if (profileImageUrl != null) {
                Glide.with(getContext()).load(profileImageUrl).placeholder(R.drawable.img_avatar_nam).into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.img_avatar_nam);
            }
        }
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", false);
                                editor.apply();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
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
        });
        setUpFunctionRecyclerView();
        setUpRecyclerView();
        return view;
    }

    private NguoiDung queryNguoiDungFromDatabase(int maND) {
        NguoiDung nguoiDung = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getReadableDatabase();
            String[] projection = {
                    "tenDN",
                    "email",
                    "sdt",
                    "role"
            };
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            cursor = db.query("NguoiDung", projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int tenDNIndex = cursor.getColumnIndex("tenDN");
                int emailIndex = cursor.getColumnIndex("email");
                int sdtIndex = cursor.getColumnIndex("sdt");
                int roleIndex = cursor.getColumnIndex("role");
                String tenDN = cursor.getString(tenDNIndex);
                String email = cursor.getString(emailIndex);
                String sdt = cursor.getString(sdtIndex);
                String role = cursor.getString(roleIndex);
                nguoiDung = new NguoiDung(maND, tenDN, email, sdt, role);
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
        return nguoiDung;
    }

    private String queryProfileImageFromDatabase(int maND) {
        String profileImage = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getReadableDatabase();
            String[] projection = {
                    "hinhAnh"
            };
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            cursor = db.query("NguoiDung", projection, selection, selectionArgs, null, null, null);
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
        int maND = sharedPreferences.getInt("maND", -1);
        NguoiDung nguoiDung = queryNguoiDungFromDatabase(maND);
        if (nguoiDung != null) {
            tenTextView.setText("Tên đăng nhập: " + nguoiDung.getTenDN());
            emailTextView.setText("Email: " + nguoiDung.getEmail());
            sdtTextView.setText("Số điện thoại: " + nguoiDung.getSdt());
            roleTextView.setText("Vai trò: " + nguoiDung.getRole());
            String profileImageUrl = queryProfileImageFromDatabase(maND);
            if (profileImageUrl != null) {
                updateProfileImageInDatabase(maND, profileImageUrl); // Di chuyển phần này lên đây
                Glide.with(getContext()).load(profileImageUrl).placeholder(R.drawable.img_avatar_nam).into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.img_avatar_nam);
            }
        }
    }

    private void updateProfileImageInDatabase(int maND, String newProfileImage) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hinhAnh", newProfileImage);
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            int rowsAffected = db.update("NguoiDung", values, selection, selectionArgs);
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

    private void setUpFunctionRecyclerView() {
        ArrayList<BuyActivityItem> functionList = new ArrayList<>();
        functionList.add(new BuyActivityItem(R.drawable.wait_confirm, "Chờ xác nhận"));
        functionList.add(new BuyActivityItem(R.drawable.wait_get_package, "Chờ lấy hàng"));
        functionList.add(new BuyActivityItem(R.drawable.delivering, "Đang giao"));
        functionList.add(new BuyActivityItem(R.drawable.delivered, "Đã giao"));
        functionList.add(new BuyActivityItem(R.drawable.cancel_box, "Đã hủy"));
        BuyAcitivyAdapter adapter = new BuyAcitivyAdapter(getContext());
        adapter.setBuyActivityItems(functionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(adapter);
    }



    private void setUpRecyclerView() {
        ArrayList<String> functionList = new ArrayList<>();
        functionList.add("Đánh giá của tôi");
        functionList.add("Quản lý tài khoản");
        if(isAdmin()) {
            functionList.add("Chức năng quản trị viên");
        }
        else {
            functionList.add("Hỗ trợ" );
        }
        functionList.add("Báo lỗi ứng dụng?");
        functionList.add("Thông tin phiên bản");
        FunctionAdapter adapter = new FunctionAdapter(functionList);
        functionRecyclerView.setAdapter(adapter);
        functionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private boolean isAdmin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        return role.equals("admin");
    }
}