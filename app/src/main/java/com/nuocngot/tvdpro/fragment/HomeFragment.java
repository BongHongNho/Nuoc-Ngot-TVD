package com.nuocngot.tvdpro.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import com.nuocngot.tvdpro.model.Category;
import com.nuocngot.tvdpro.model.SanPham;
import com.nuocngot.tvdpro.adapter.SanPhamAdapter;
import com.nuocngot.tvdpro.model.SelectTab;
import com.nuocngot.tvdpro.adapter.SelectTabAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<SanPham> sanPhamList;
    private SanPhamAdapter adapter;
    private SQLiteDatabase database;

    private ImageSwitcher imageSwitcher;

    private FloatingActionButton floatingActionButton;

    private RecyclerView selectabRecycale;

    private SearchView searchView;

    public SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(sanPhamList);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewSanPham);
        floatingActionButton = rootView.findViewById(R.id.fab);
        imageSwitcher = rootView.findViewById(R.id.imageSwitch);
        selectabRecycale = rootView.findViewById(R.id.selectTabProducts);
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
        int maTK = sharedPreferences.getInt("maTK", -1);
        sanPhamAdapter.checkAdminRole(rootView.getContext(), maTK);
        if (adapter.checkAdminRole(rootView.getContext(), maTK)) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog(rootView.getContext());
            }
        });
        final int[] imageResources = {R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_1};
        final int[] currentIndex = {0};

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(rootView.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return imageView;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageSwitcher.setImageResource(imageResources[currentIndex[0]]);
                currentIndex[0] = (currentIndex[0] + 1) % imageResources.length;
                new Handler().postDelayed(this, 2000);
            }
        }, 500);

        setUpRecyclerView();
        return rootView;
    }

    private void showAddProductDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_sp_dialog, null);
        builder.setView(dialogView);
        builder.setTitle("Thêm sản phẩm");
        builder.setMessage("Nhập thông tin sản phẩm, và thêm vào danh sách sản phẩm, vui lòng kiểm tra lại dữ liệu trước khi thêm");
        TextInputEditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        TextInputEditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        TextInputEditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);
        TextInputEditText imageViewProduct = dialogView.findViewById(R.id.imageViewProduct);
        TextInputEditText editTextXuatXu = dialogView.findViewById(R.id.editTextXuatxu);
        TextInputEditText editTextThongTinSP = dialogView.findViewById(R.id.editTextThongTin);
        Spinner spinnerDanhMuc = dialogView.findViewById(R.id.spinnerDanhMuc); // Thêm Spinner danh mục
        ArrayList<Category> danhMucList = loadDanhMucList(context);
        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, danhMucList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(spinnerAdapter);
        builder.setPositiveButton("Thêm mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productName = editTextProductName.getText().toString();
                String productPrice = editTextProductPrice.getText().toString();
                String productQuantity = editTextProductQuantity.getText().toString();
                String productImage = imageViewProduct.getText().toString();
                String xuatXu = editTextXuatXu.getText().toString();
                String thongTinSP = editTextThongTinSP.getText().toString();
                Category selectedDanhMuc = (Category) spinnerDanhMuc.getSelectedItem();
                int maDanhMuc = selectedDanhMuc.getMaDM();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues valuesSanPham = new ContentValues();
                valuesSanPham.put("tenSP", productName);
                valuesSanPham.put("gia", Integer.parseInt(productPrice));
                valuesSanPham.put("soLuong", Integer.parseInt(productQuantity));
                valuesSanPham.put("hinhAnh", productImage);
                valuesSanPham.put("maDM", maDanhMuc); // Thêm mã danh mục vào sản phẩm
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
                    sanPhamList.clear();
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

    private ArrayList<Category> loadDanhMucList(Context context) {
        ArrayList<Category> danhMucList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"maDM", "tenDM", "anhDM"};
        Cursor cursor = db.query("DanhMuc", projection, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maDM = cursor.getInt(cursor.getColumnIndexOrThrow("maDM"));
                String tenDM = cursor.getString(cursor.getColumnIndexOrThrow("tenDM"));
                String anhDM = cursor.getString(cursor.getColumnIndexOrThrow("anhDM"));
                danhMucList.add(new Category(maDM, tenDM, anhDM));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return danhMucList;
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

    private void setUpRecyclerView() {
        ArrayList<SelectTab> selectTabList = new ArrayList<>();
        selectTabList.add(new SelectTab("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Coca-Cola_logo.svg/512px-Coca-Cola_logo.svg.png"));
        selectTabList.add(new SelectTab("https://upload.wikimedia.org/wikipedia/commons/3/3b/Mirinda_Logo.png"));
        selectTabList.add(new SelectTab("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Pepsi_logo_%282014%29.svg/2560px-Pepsi_logo_%282014%29.svg.png"));
        selectTabList.add(new SelectTab("https://upload.wikimedia.org/wikipedia/vi/thumb/6/6d/Red_Bull_Logo.svg/1200px-Red_Bull_Logo.svg.png"));
        SelectTabAdapter selectTabAdapter = new SelectTabAdapter(getContext(), selectTabList);
        selectabRecycale.setAdapter(selectTabAdapter);
        selectabRecycale.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}
