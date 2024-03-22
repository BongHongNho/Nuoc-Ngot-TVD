package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.getContext.GetContext;

import java.util.ArrayList;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder> {
    private ArrayList<String> functions; // Danh sách các chức năng
    private OnFunctionClickListener listener;

    // Constructor
    public FunctionAdapter(ArrayList<String> functions, OnFunctionClickListener listener) {
        this.functions = functions;
        this.listener = listener;
    }

    public FunctionAdapter(ArrayList<String> functionList) {
        this.functions = functionList;
    }

    // Interface cho việc xử lý sự kiện khi người dùng click vào một chức năng
    public interface OnFunctionClickListener {
        void onFunctionClick(int position);
    }

    // ViewHolder cho mỗi mục trong danh sách
    public static class FunctionViewHolder extends RecyclerView.ViewHolder {
        public TextView functionNameTextView;

        public FunctionViewHolder(View itemView) {
            super(itemView);
            functionNameTextView = itemView.findViewById(R.id.functionNameTextView);
        }
    }

    @NonNull
    @Override
    public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new FunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionViewHolder holder, int position) {
        String functionName = functions.get(position);
        holder.functionNameTextView.setText(functionName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFunction = functions.get(position);
                if (selectedFunction.equals("Đổi ảnh đại diện")) {
                    changeProfileImage(v.getContext());
                } else if (selectedFunction.equals("Đổi tên người dùng")) {

                } else if (selectedFunction.equals("Đổi mật khẩu")) {

                } else if (selectedFunction.equals("Nâng cấp tài khoản")) {

                } else if (selectedFunction.equals("Chức năng ADMIN")) {

                }
            }
        });
    }

    private void changeProfileImage(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_profile_image, null);
        int maTK = getCurrentUserMaTK();
        String currentProfileImage = queryProfileImageFromDatabase(maTK);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi ảnh đại diện");
        builder.setMessage("Nhập link ảnh đại diện mới để thay đổi");
        TextInputEditText editTextProfile = view.findViewById(R.id.editTextProfile);
        editTextProfile.setText(currentProfileImage);
        builder.setView(view);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newProfileImage = editTextProfile.getText().toString();
                updateProfileImageInDatabase(maTK, newProfileImage,context);
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private int getCurrentUserMaTK() {
        SharedPreferences sharedPreferences = GetContext.createAppContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("maTK", -1);
    }

    private String queryProfileImageFromDatabase(int maTK) {
        String profileImage = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GetContext.createAppContext());
            db = dbHelper.getReadableDatabase();
            String selection = "maTK = ?";
            String[] selectionArgs = {String.valueOf(maTK)};
            cursor = db.query("KhachHang", new String[]{"hinhAnh"}, selection, selectionArgs, null, null, null);
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

    private void updateProfileImageInDatabase(int maTK, String newProfileImage, Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hinhAnh", newProfileImage);
            String selection = "maTK = ?";
            String[] selectionArgs = {String.valueOf(maTK)};
            int rowsAffected = db.update("KhachHang", values, selection, selectionArgs);
            if (rowsAffected > 0) {
                Toast.makeText(context, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Không thể cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    @Override
    public int getItemCount() {
        return functions.size();
    }
}
