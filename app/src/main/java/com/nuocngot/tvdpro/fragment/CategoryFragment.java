package com.nuocngot.tvdpro.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.Category;
import com.nuocngot.tvdpro.adapter.CategoryAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

    public class CategoryFragment extends Fragment {
        private ArrayList<Category> categoryList;
        private RecyclerView recyclerView;
        private CategoryAdapter categoryAdapter;

        private FloatingActionButton fab;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_category, container, false);
            recyclerView = view.findViewById(R.id.recyclerViewCategory);
            fab = view.findViewById(R.id.fabAdd);
            if (isAdmin()) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }
            categoryList = new ArrayList<>();
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {"maDM", "tenDM"};
            Cursor cursor = db.query("DanhMuc", projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maDM = cursor.getInt(cursor.getColumnIndexOrThrow("maDM"));
                    String tenDM = cursor.getString(cursor.getColumnIndexOrThrow("tenDM"));
                    categoryList.add(new Category(maDM, tenDM));
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
            dbHelper.close();
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3); // 3 là số cột
            recyclerView.setLayoutManager(layoutManager);
            categoryAdapter = new CategoryAdapter(categoryList, getContext());
            recyclerView.setAdapter(categoryAdapter);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogToAddCategory();
                }
            });
            return view;
        }

        private boolean isAdmin() {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_status", Context.MODE_PRIVATE);
            String role = sharedPreferences.getString("role", "");
            return role.equals("admin");
        }

        private void showDialogToAddCategory() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
            TextInputEditText editTextCategoryName = dialogView.findViewById(R.id.editTextCategoryName);
            builder.setView(dialogView)
                    .setTitle("Thêm danh mục")
                    .setMessage("Nhập tên danh mục")
                    .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String categoryName = editTextCategoryName.getText().toString().trim();
                            if (!categoryName.isEmpty()) {
                                addCategoryToDatabase(categoryName);
                            } else {
                                Toast.makeText(getContext(), "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        private void addCategoryToDatabase(String categoryName) {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tenDM", categoryName);
            long newRowId = db.insert("DanhMuc", null, values);
            if (newRowId != -1) {
                Toast.makeText(getContext(), "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                loadCategoryList();
            } else {
                Toast.makeText(getContext(), "Đã xảy ra lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }
        private void loadCategoryList() {
            categoryList.clear();
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection = {"maDM", "tenDM"};
            Cursor cursor = db.query("DanhMuc", projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maDM = cursor.getInt(cursor.getColumnIndexOrThrow("maDM"));
                    String tenDM = cursor.getString(cursor.getColumnIndexOrThrow("tenDM"));
                    categoryList.add(new Category(maDM, tenDM));
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
            dbHelper.close();
            categoryAdapter.notifyDataSetChanged();
        }
    }
