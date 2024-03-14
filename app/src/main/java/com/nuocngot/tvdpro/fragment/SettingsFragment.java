package com.nuocngot.tvdpro.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.nuocngot.tvdpro.LoginActivity;
import com.nuocngot.tvdpro.adapter.Admin;
import com.nuocngot.tvdpro.adapter.stAdpater;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.stListViewAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    public ArrayList<stAdpater> stAdapters = new ArrayList();

    public ListView listView;

    public Button btnExit;
    public TextView tvName, tvThanhVien;

    public ImageView ivAvatar;
    public String NAME_SHARED_PREFERENCES = "manager";
    public String IS_LOGIN = "isLogin";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings, container, false);
        listView = view.findViewById(R.id.listViewST);
        btnExit = view.findViewById(R.id.btnLogout);
        tvName = view.findViewById(R.id.tvName);
        tvThanhVien = view.findViewById(R.id.tvThanhVien);
        ivAvatar = view.findViewById(R.id.imgAvatar);
        stAdapters.add(new stAdpater("Đổi ảnh đại diện"));
        stAdapters.add(new stAdpater("Đổi tên tài khoản"));
        stAdapters.add(new stAdpater("Thay đổi địa chỉ"));
        stAdapters.add(new stAdpater("Đổi mật khẩu"));
        stAdapters.add(new stAdpater("Nâng cấp tài khoản"));
        stListViewAdapter adapter = new stListViewAdapter(getContext(), stAdapters);
        listView.setAdapter(adapter);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAskDialog();
            }
        });
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Admin admin = dbHelper.getAdminInfo(); 
        if (admin != null) {
            tvName.setText(admin.getTenAdmin());
            Glide.with(getContext()).load(admin.getHinhAnh()).into(ivAvatar);
        }

        return view;
    }


    public void showAskDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Đăng xuất");
        alertDialog.setMessage("Bạn muốn đăng xuất tài khoản?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(IS_LOGIN, false);
                editor.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}