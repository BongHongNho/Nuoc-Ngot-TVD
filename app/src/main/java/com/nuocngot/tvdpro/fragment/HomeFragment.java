package com.nuocngot.tvdpro.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.SanPham;
import com.nuocngot.tvdpro.adapter.SanPhamAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<SanPham> sanPhamList;
    private SanPhamAdapter adapter;
    private SQLiteDatabase database;

    private FloatingActionButton floatingActionButton;

    public  SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(sanPhamList);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewSanPham);
        floatingActionButton = rootView.findViewById(R.id.fab);
        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        sanPhamList = new ArrayList<>();
        adapter = new SanPhamAdapter(sanPhamList);
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(sanPhamList);
        recyclerView.setAdapter(adapter);
        DatabaseHelper dbHelper = new DatabaseHelper(rootView.getContext());
        database = dbHelper.getReadableDatabase();
        loadSanPhamData();
        SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        sanPhamAdapter.checkAdminRole(rootView.getContext(), username);
        if(adapter.checkAdminRole(rootView.getContext(), username)) {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        else {
            floatingActionButton.setVisibility(View.GONE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog(rootView.getContext());
            }
        });
        return rootView;
    }

    private void showAddProductDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_sp_dialog, null);
        builder.setView(dialogView);
        builder.setTitle("Thêm sản phẩm");

        TextInputEditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        TextInputEditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        TextInputEditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);
        TextInputEditText imageViewProduct = dialogView.findViewById(R.id.imageViewProduct);
        TextInputEditText editTextXuatXu = dialogView.findViewById(R.id.editTextXuatxu);
        TextInputEditText editTextThongTinSP = dialogView.findViewById(R.id.editTextThongTin);

        builder.setPositiveButton("Thêm mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productName = editTextProductName.getText().toString();
                String productPrice = editTextProductPrice.getText().toString();
                String productQuantity = editTextProductQuantity.getText().toString();
                String productImage = imageViewProduct.getText().toString();
                String xuatXu = editTextXuatXu.getText().toString();
                String thongTinSP = editTextThongTinSP.getText().toString();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues valuesSanPham = new ContentValues();
                valuesSanPham.put("tenSP", productName);
                valuesSanPham.put("gia", Integer.parseInt(productPrice));
                valuesSanPham.put("soLuong", Integer.parseInt(productQuantity));
                valuesSanPham.put("hinhAnh", productImage);
                long sanPhamId = db.insert("SanPham", null, valuesSanPham);
                ContentValues valuesChiTiet = new ContentValues();
                valuesChiTiet.put("maSP", sanPhamId);
                valuesChiTiet.put("xuatXu", xuatXu);
                valuesChiTiet.put("thongTinSP", thongTinSP);
                valuesChiTiet.put("tenSP", productName);
                valuesChiTiet.put("gia", Integer.parseInt(productPrice));
                valuesChiTiet.put("soLuong", Integer.parseInt(productQuantity));
                valuesChiTiet.put("hinhAnh", productImage);
                long chiTietId = db.insert("ChiTietSanPham", null, valuesChiTiet);
                if (sanPhamId != -1 && chiTietId != -1) {
                    Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    loadData(getContext());
                    sanPhamAdapter.notifyDataSetChanged();
                }

                db.close();
                dbHelper.close();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void loadData(Context context) {
        sanPhamList.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "maSP",
                "tenSP",
                "gia",
                "soLuong",
                "hinhAnh"
        };
        Cursor cursor = db.query(
                "SanPham",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndexOrThrow("maSP"));
                String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("tenSP"));
                int gia = cursor.getInt(cursor.getColumnIndexOrThrow("gia"));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                String hinhAnh = cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh"));
                sanPhamList.add(new SanPham(maSP, tenSP, hinhAnh, soLuong, gia));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        dbHelper.close();
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void loadSanPhamData() {
        Cursor cursor = database.rawQuery("SELECT * FROM SanPham", null);
        if (cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndex("maSP"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                String tenSP = cursor.getString(cursor.getColumnIndex("tenSP"));
                int soLuong = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int gia = cursor.getInt(cursor.getColumnIndex("gia"));

                SanPham sanPham = new SanPham(maSP, hinhAnh, tenSP, soLuong, gia);
                sanPhamList.add(sanPham);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
