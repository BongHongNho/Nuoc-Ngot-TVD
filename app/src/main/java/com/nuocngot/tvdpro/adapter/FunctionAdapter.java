package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.activity.QLTaiKhoanActivity;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.CNQuanTriVien;
import com.nuocngot.tvdpro.activity.QuanLiDonHangActivity;
import com.nuocngot.tvdpro.activity.QuanLyUserActivity;
import com.nuocngot.tvdpro.activity.ThongKeDoanhThuActivity;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.getContext.GetContext;

import java.util.ArrayList;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder> {
    private ArrayList<String> functions;
    private OnFunctionClickListener listener;

    public FunctionAdapter(ArrayList<String> functions, OnFunctionClickListener listener) {
        this.functions = functions;
        this.listener = listener;
    }

    public FunctionAdapter(ArrayList<String> functionList) {
        this.functions = functionList;
    }

    public interface OnFunctionClickListener {
        void onFunctionClick(int position);
    }

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
                int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí của item trong adapter
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                String selectedFunction = functions.get(adapterPosition);
                Context context = v.getContext();

                switch (selectedFunction) {
                    case "Đổi ảnh đại diện":
                        changeProfileImage(context);
                        break;
                    case "Đổi tên đăng nhập":
                        changeUserName(context);
                        break;
                    case "Đổi mật khẩu":
                        changePassword(context);
                        break;
                    case "Thay đổi địa chỉ":
                        String currentAddress = getCurrentAddressFromDatabase(context);
                        showAddressChangeDialog(context, currentAddress);
                        break;
                    case "Chức năng quản trị viên":
                        startNewActivity(context, CNQuanTriVien.class);
                        break;
                    case "Quản lý người dùng":
                        startNewActivity(context, QuanLyUserActivity.class);
                        break;
                    case "Quản lý đơn hàng":
                        startNewActivity(context, QuanLiDonHangActivity.class);
                        break;
                    case "Quản lý tài khoản":
                        startNewActivity(context, QLTaiKhoanActivity.class);
                        break;
                    case "Hỗ trợ":
                    case "Báo lỗi ứng dụng?":
                        showErrorReport(context);
                        break;
                    case "Thông tin phiên bản":
                        showVersionInfoDialog(context);
                        break;
                    case "Thống kê doanh số":
                        startNewActivity(context, ThongKeDoanhThuActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void startNewActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    private void showVersionInfoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông tin phiên bản");
        builder.setMessage("Sinh viên: Nguyễn Huy Phước Tấn\nMã sinh viên: PH28818\nSinh viên: Vi Văn Hậu\nMã sinh viên: PH38983");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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

    private void showAddressChangeDialog(Context context, String currentAddress) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_address, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi địa chỉ");
        builder.setMessage("Nhập địa chỉ mới để thay đổi");

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

            SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
            int maND = sharedPreferences.getInt("maND", -1);

            if (maND != -1) {
                String selection = "maND = ?";
                String[] selectionArgs = {String.valueOf(maND)};
                int rowsAffected = db.update("NguoiDung", values, selection, selectionArgs);

                if (rowsAffected > 0) {
                    Toast.makeText(context, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                }
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
        builder.setMessage("Nhập mật khẩu cũ và mật khẩu mới để thay đổi");

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

                if (!checkOldPassword(context, oldPassword)) {
                    Toast.makeText(context, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(context, "Mật khẩu mới và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                updatePasswordInDatabase(newPassword, context);
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
    private void updatePasswordInDatabase(String newPassword, Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
            int maND = sharedPreferences.getInt("maND", -1); // Lấy mã người dùng (maND)

            if (maND != -1) {
                ContentValues values = new ContentValues();
                values.put("matKhau", newPassword);
                String selection = "maND = ?";
                String[] selectionArgs = {String.valueOf(maND)};
                int rowsAffected = db.update("TaiKhoan", values, selection, selectionArgs);

                if (rowsAffected > 0) {
                    Toast.makeText(context, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private boolean checkOldPassword(Context context, String oldPassword) {
        String currentPassword = getCurrentPasswordFromDatabase(context);
        return oldPassword.equals(currentPassword);
    }

    private String getCurrentPasswordFromDatabase(Context context) {
        String currentPassword = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();

            SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
            int maND = sharedPreferences.getInt("maND", -1); // Lấy mã người dùng (maND)

            if (maND != -1) {
                String[] projection = {"matKhau"};
                String selection = "maND = ?";
                String[] selectionArgs = {String.valueOf(maND)};
                cursor = db.query("TaiKhoan", projection, selection, selectionArgs, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    currentPassword = cursor.getString(cursor.getColumnIndex("matKhau"));
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
        return currentPassword;
    }



    private void showErrorReport(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Báo lỗi và hỗ trợ");
        builder.setMessage("Để báo lỗi hoặc hỗ trợ, bạn có thể gửi thư về địa chỉ\nEmail: tannhpph28818@fpt.edu.vn\nHotline: 035.976.2830");
        builder.setPositiveButton("Liên hệ Hotline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0359762830"));
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Liên hệ Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:tannhpph28818@fpt.edu.vn"));
                context.startActivity(intent);
            }
        });
        builder.show();
    }


    private void changeUserName(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_user_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi tên người dùng");
        builder.setMessage("Nhập tên người dùng mới để thay đổi");
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
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private String getCurrentUserNameFromDatabase(Context context) {
        String currentUserName = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();
            int maND = getCurrentUserMaND();
            String[] projection = {"tenDN"};
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            cursor = db.query("NguoiDung", projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                currentUserName = cursor.getString(cursor.getColumnIndexOrThrow("tenDN"));
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
            int rowsAffected = db.update("NguoiDung", values, null, null);
            if (rowsAffected > 0) {
                Toast.makeText(context, "Cập nhật tên người dùng thành công", Toast.LENGTH_SHORT).show();
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

    private String getCurrentAddressFromDatabase(Context context) {
        String currentAddress = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();
            int maND = getCurrentUserMaND();
            String[] projection = {"diaChi"};
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            cursor = db.query("NguoiDung", projection, selection, selectionArgs, null, null, null);
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


    private void changeProfileImage(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_profile_image, null);
        int maND = getCurrentUserMaND();
        String currentProfileImage = queryProfileImageFromDatabase(maND);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thay đổi ảnh đại diện");
        builder.setMessage("Nhập link ảnh đại diện mới để thay đổi");
        TextInputEditText editTextProfile = view.findViewById(R.id.editTextProfile);
        editTextProfile.setText(currentProfileImage);
        builder.setView(view);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newProfileImage = editTextProfile.getText().toString();
                updateProfileImageInDatabase(maND, newProfileImage, context);
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

    private int getCurrentUserMaND() {
        SharedPreferences sharedPreferences = GetContext.createAppContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("maND", -1);
    }

    private String queryProfileImageFromDatabase(int maND) {
        String profileImage = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GetContext.createAppContext());
            db = dbHelper.getReadableDatabase();
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            cursor = db.query("NguoiDung", new String[]{"hinhAnh"}, selection, selectionArgs, null, null, null);
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

    private void updateProfileImageInDatabase(int maND, String newProfileImage, Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hinhAnh", newProfileImage);
            String selection = "maND = ?";
            String[] selectionArgs = {String.valueOf(maND)};
            int rowsAffected = db.update("NguoiDung", values, selection, selectionArgs);
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

    // Các phương thức khác tương tự cho việc thay đổi thông tin người dùng

    @Override
    public int getItemCount() {
        return functions.size();
    }
}
