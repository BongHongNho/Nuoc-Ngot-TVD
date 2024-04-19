package com.nuocngot.tvdpro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuocngot.tvdpro.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nuocngot.tvdpro.model.DonHang;
import com.nuocngot.tvdpro.adapter.DonHangAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class DaGiaoFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonHangAdapter adapter;
    private TextView textViewEmpty;
    private ArrayList<DonHang> donHangList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_da_giao, container, false);
        textViewEmpty = rootView.findViewById(R.id.textViewGHEmpty);
        recyclerView = rootView.findViewById(R.id.recycaleViewDG);
        adapter = new DonHangAdapter(donHangList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maND", -1);
        loadDonMuaData(maKH);
        return rootView;
    }

    private void loadDonMuaData(int maKH) {
        donHangList.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "maND = ? AND maTTDH = ?";
        String[] selectionArgs = {String.valueOf(maKH), "4"};
        Cursor cursor = db.query(
                "DonMua",
                null,
                selection,
                selectionArgs,
                null,
                null,
                "ngayMua DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenDH = cursor.getString(cursor.getColumnIndex("tenDH"));
                String tenSPDH = cursor.getString(cursor.getColumnIndex("tenSPDH"));
                int soLuongSPDH = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int tongTienDH = cursor.getInt(cursor.getColumnIndex("tongTien"));
                String ngayMua = cursor.getString(cursor.getColumnIndex("ngayMua"));
                String anhDH = cursor.getString(cursor.getColumnIndex("anhDH"));
                int maDM = cursor.getInt(cursor.getColumnIndex("maDMUA"));
                int maTTDH = cursor.getInt(cursor.getColumnIndex("maTTDH"));
                DonHang donHang = new DonHang(tenDH, tenSPDH, soLuongSPDH, tongTienDH, ngayMua, anhDH, maDM, maTTDH);
                donHangList.add(donHang);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Collections.sort(donHangList, new Comparator<DonHang>() {
            @Override
            public int compare(DonHang o1, DonHang o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                try {
                    Date date1 = sdf.parse(o1.getNgayMua());
                    Date date2 = sdf.parse(o2.getNgayMua());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        adapter.notifyDataSetChanged();
    }

}