package com.nuocngot.tvdpro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuocngot.tvdpro.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nuocngot.tvdpro.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nuocngot.tvdpro.model.DonHang;
import com.nuocngot.tvdpro.adapter.DonHangAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class ChoXacNhanFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonHangAdapter adapter;
    private ArrayList<DonHang> donHangList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        recyclerView = rootView.findViewById(R.id.recycaleViewXN);
        adapter = new DonHangAdapter(donHangList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        loadDonMuaData(maKH);
        return rootView;
    }

    private void loadDonMuaData(int maKH) {
        donHangList.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "maKH = ? AND maTTDH = ?";
        String[] selectionArgs = {String.valueOf(maKH), "1"}; // Chỉ lấy các đơn mua có maTTDH là 1
        Cursor cursor = db.query(
                "DonMua",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenDH = cursor.getString(cursor.getColumnIndex("tenDH"));
                String tenSPDH = cursor.getString(cursor.getColumnIndex("tenSPDH"));
                int soLuongSPDH = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int tongTienDH = cursor.getInt(cursor.getColumnIndex("tongTien"));
                String ngayMua = cursor.getString(cursor.getColumnIndex("ngayMua"));
                DonHang donHang = new DonHang(tenDH, tenSPDH, soLuongSPDH, tongTienDH, ngayMua);
                donHangList.add(donHang);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }



}