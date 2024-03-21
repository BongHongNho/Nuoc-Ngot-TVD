package com.nuocngot.tvdpro.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.Category;
import com.nuocngot.tvdpro.adapter.CategoryAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

    public class CategoryFragment extends Fragment {
        private ArrayList<Category> categoryList;
        private RecyclerView recyclerView;
        private CategoryAdapter categoryAdapter;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_category, container, false);
            recyclerView = view.findViewById(R.id.recyclerViewCategory);
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
            categoryAdapter = new CategoryAdapter(categoryList, getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(categoryAdapter);
            return view;
        }
    }
