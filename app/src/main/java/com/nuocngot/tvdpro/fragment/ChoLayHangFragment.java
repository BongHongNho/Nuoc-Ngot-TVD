package com.nuocngot.tvdpro.fragment;

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

public class ChoLayHangFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonHangAdapter adapter;
    private ArrayList<DonHang> donHangList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cho_lay_hang, container, false);
        recyclerView = rootView.findViewById(R.id.recycaleViewDH);
        adapter = new DonHangAdapter(donHangList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        loadDonMuaData(maKH);
        return rootView;
    }

    private void loadDonMuaData(int maKH) {
        // Xóa các phần tử hiện có trong danh sách
        donHangList.clear();

        // Thực hiện truy vấn cơ sở dữ liệu để lấy dữ liệu đơn hàng của maKH
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "maKH = ?";
        String[] selectionArgs = {String.valueOf(maKH)};
        Cursor cursor = db.query(
                "DonMua",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Kiểm tra xem có dữ liệu không và thêm vào danh sách nếu có
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Lấy thông tin từ cursor
                String tenDH = cursor.getString(cursor.getColumnIndex("tenDH"));
                String tenSPDH = cursor.getString(cursor.getColumnIndex("tenSPDH"));
                int soLuongSPDH = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int tongTienDH = cursor.getInt(cursor.getColumnIndex("tongTien"));

                // Tạo đối tượng DonHang và thêm vào danh sách
                DonHang donHang = new DonHang(tenDH, tenSPDH, soLuongSPDH, tongTienDH);
                donHangList.add(donHang);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Thông báo cho adapter biết dữ liệu đã thay đổi
        adapter.notifyDataSetChanged();
    }


}


