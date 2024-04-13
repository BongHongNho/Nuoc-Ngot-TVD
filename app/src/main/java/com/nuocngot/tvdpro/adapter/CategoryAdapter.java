package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.CategoryActivity;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> categoryList;
    private Context context;

    public CategoryAdapter(ArrayList<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maDM = category.getMaDM();
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("maDM", maDM);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isAdmin()) {
                    String[] items = {"Sửa danh mục", "Xóa danh mục"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Chức năng danh mục");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    showEditCategoryDialog(category);
                                    break;
                                case 1:
                                    showDeleteCategoryDialog(category);
                                    break;
                            }
                        }
                    }).show();
                }
                return false;
            }
        });
    }
    private void showEditCategoryDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        TextInputEditText editTextCategoryName = view.findViewById(R.id.editTextCategoryName);
        editTextCategoryName.setText(category.getTenDM());
        builder.setView(view)
                .setTitle("Sửa danh mục")
                .setMessage("Nhập tên danh mục mới")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCategoryName = editTextCategoryName.getText().toString().trim();
                        updateCategoryInDatabase(category.getMaDM(), newCategoryName);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void updateCategoryInDatabase(int maDM, String newCategoryName) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tenDM", newCategoryName);
        int rowsAffected = db.update("DanhMuc", values, "maDM = ?", new String[]{String.valueOf(maDM)});
        if (rowsAffected > 0) {
            Toast.makeText(context, "Danh mục đã được cập nhật", Toast.LENGTH_SHORT).show();
            loadCategoryList();
        } else {
            Toast.makeText(context, "Cập nhật danh mục thất bại", Toast.LENGTH_SHORT).show();
        }

        db.close();
        dbHelper.close();
    }
    private void loadCategoryList() {
        categoryList.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"maDM", "tenDM", "anhDM"};
        Cursor cursor = db.query("DanhMuc", projection, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maDM = cursor.getInt(cursor.getColumnIndexOrThrow("maDM"));
                String tenDM = cursor.getString(cursor.getColumnIndexOrThrow("tenDM"));
                String anhDM = cursor.getString(cursor.getColumnIndexOrThrow("anhDM"));
                categoryList.add(new Category(maDM, tenDM, anhDM));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();
        notifyDataSetChanged();
    }
    private boolean isAdmin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        return role.equals("admin");
    }
    private void showDeleteCategoryDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa danh mục")
                .setMessage("Bản muốn xóa danh mục: " + category.getTenDM() + "?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCategoryFromDatabase(category.getMaDM());
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void deleteCategoryFromDatabase(int maDM) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("DanhMuc", "maDM = ?", new String[]{String.valueOf(maDM)});
        if (rowsDeleted > 0) {
            Toast.makeText(context, "Danh mục đã được xóa", Toast.LENGTH_SHORT).show();
            loadCategoryList();
        }
        db.close();
        dbHelper.close();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView textViewCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }

        public void bind(Category category) {
            Glide.with(itemView.getContext()).load(category.getAnhDM()).into(textViewCategoryName);
        }
    }
}
