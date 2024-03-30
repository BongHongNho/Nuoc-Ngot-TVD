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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.QLTaiKhoanActivity;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.CNQuanTriVien;
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
                }
                if (selectedFunction.equals("Đổi tên đăng nhập")) {
                    changeUserName(v.getContext());
                }
                if (selectedFunction.equals("Đổi mật khẩu")) {
                    changePassword(v.getContext());
                }
                if (selectedFunction.equals("Thay đổi địa chỉ")) {
                    String currentAddress = getCurrentAddressFromDatabase(v.getContext());
                    showAddressChangeDialog(v.getContext(), currentAddress);
                }
                if (selectedFunction.equals("Chức năng quản trị viên")) {
                    Intent intent = new Intent(v.getContext(), CNQuanTriVien.class);
                    v.getContext().startActivity(intent);
                }
                if (selectedFunction.equals("Đánh giá của tôi")) {

                } else if (selectedFunction.equals("Quản lý tài khoản")) {
                    Intent intent = new Intent(v.getContext(), QLTaiKhoanActivity.class);
                    v.getContext().startActivity(intent);
                } else if (selectedFunction.equals("Hỗ trợ")) {

                } else if (selectedFunction.equals("Báo lỗi ứng dụng")) {

                } else if (selectedFunction.equals("Thông tin phiên bản")) {

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
                updateProfileImageInDatabase(maTK, newProfileImage, context);
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

    private void changeUserName(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_user_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi tên người dùng");
        builder.setMessage("Nhập tên người dùng mới để thay đổi");
        TextInputEditText editTextUserName = view.findViewById(R.id.editTextUsername);
        String currentUserName = getCurrentUserNameFromDatabase(context);
        editTextUserName.setText(currentUserName);
        builder.setView(view);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUserName = editTextUserName.getText().toString();
                        updateUserNameInDatabase(newUserName, context);
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private String getCurrentUserNameFromDatabase(Context context) {
        String currentUserName = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();

            // Lấy maTK từ SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
            int maTK = sharedPreferences.getInt("maTK", -1); // -1 là giá trị mặc định nếu không tìm thấy maTK

            if (maTK != -1) {
                String[] projection = {"tenDN"};
                String selection = "maTK = ?";
                String[] selectionArgs = {String.valueOf(maTK)};
                cursor = db.query("TaiKhoan", projection, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    currentUserName = cursor.getString(cursor.getColumnIndex("tenDN"));
                }
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
        return currentUserName;
    }


    private void updateUserNameInDatabase(String newUserName, Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tenDN", newUserName);
            int rowsAffected = db.update("TaiKhoan", values, null, null);
            if (rowsAffected > 0) {
                Toast.makeText(context, "Cập nhật tên người dùng mới", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Không thể cập nhật tên người dùng", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private void changePassword(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đổi mật khẩu");
        builder.setMessage("Nhập mật khẩu cũ và mật khẩu mới để thay đổi");
        TextInputEditText editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        TextInputEditText editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        TextInputEditText editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);

        builder.setView(view);
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String oldPassword = editTextOldPassword.getText().toString();
                        String newPassword = editTextNewPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();
                        if (!checkOldPassword(view.getContext(), oldPassword)) {
                            Toast.makeText(context, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(context, "Mật khẩu mới và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updatePasswordInDatabase(newPassword, context);
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private boolean checkOldPassword(Context context, String oldPassword) {
        String currentPassword = getCurrentPasswordFromDatabase(context);
        return oldPassword.equals(currentPassword);
    }

    private void updatePasswordInDatabase(String newPassword, Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("matKhau", newPassword);
            db.update("TaiKhoan", values, null, null);
            Toast.makeText(context, "Mật khẩu đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private String getCurrentPasswordFromDatabase(Context context) {
        String currentPassword = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String[] projection = {"matKhau"};
            cursor = db.query("TaiKhoan", projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                currentPassword = cursor.getString(cursor.getColumnIndex("matKhau"));
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
        return currentPassword;
    }

    private String getCurrentAddressFromDatabase(Context context) {
        String currentAddress = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();
            String[] projection = {"diaChi"};
            cursor = db.query("KhachHang", projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                currentAddress = cursor.getString(cursor.getColumnIndexOrThrow("diaChi"));
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
        return currentAddress;
    }

    private void showAddressChangeDialog(Context context, String currentAddress) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_address, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi địa chỉ");
        builder.setMessage("Nhập địa chỉ mới để thay đổi");
        TextInputEditText input = view.findViewById(R.id.editTextAddress);
        input.setText(currentAddress);
        builder.setView(view);
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAddress = input.getText().toString().trim();
                updateAddressInDatabase(context, newAddress);
                Toast.makeText(context, "Đã cập nhật địa chỉ mới", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateAddressInDatabase(Context context, String newAddress) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("diaChi", newAddress);
            int rowsAffected = db.update("KhachHang", values, null, null);
            if (rowsAffected > 0) {
                Toast.makeText(context, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Cập nhật địa chỉ thất bại", Toast.LENGTH_SHORT).show();
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