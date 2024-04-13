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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.ChiTietSPActivity;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.BinhLuan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.BinhLuanViewHolder> {

    private ArrayList<BinhLuan> listBinhLuan = new ArrayList<>();

    private Context context;

    public BinhLuanAdapter(ChiTietSPActivity chiTietSPActivity, ArrayList<BinhLuan> arrayList) {
        this.listBinhLuan = arrayList;
    }

    @NonNull
    @Override
    public BinhLuanAdapter.BinhLuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_binh_luan, parent, false);
        return new BinhLuanAdapter.BinhLuanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuanAdapter.BinhLuanViewHolder holder, int position) {
        BinhLuan binhLuan = listBinhLuan.get(position);
        Glide.with(holder.itemView.getContext()).load(binhLuan.getAnhDaiDien()).into(holder.imageAvatar);
        holder.textViewTenNguoiBinhLuan.setText(binhLuan.getTenNguoiDung());
        holder.textViewBinhLuan.setText(binhLuan.getNoiDung());
        holder.textViewNgayBinhLuan.setText(binhLuan.getThoiGian());
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int savedMaKH = sharedPreferences.getInt("maKH", -1);
        boolean isAdmin = isUserAdminShare(holder.itemView.getContext());
        String binhLuanMaKH = String.valueOf(binhLuan.getMaKH());
        if (binhLuanMaKH.equals(String.valueOf(savedMaKH)) || isAdmin) {
            holder.veryfied.setVisibility(View.VISIBLE);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (binhLuanMaKH.equals(String.valueOf(savedMaKH))) {
                        showEditDeleteDialog(holder.itemView.getContext(), binhLuan);
                    } else if (isAdmin) {
                        showEditDeleteDialog(holder.itemView.getContext(), binhLuan);
                    }
                    return true;
                }
            });
        } else {
            holder.veryfied.setVisibility(View.GONE);
            holder.itemView.setOnLongClickListener(null);
        }
        boolean isAdminCommented = isUserAdmin(binhLuan.getMaKH(), holder.itemView.getContext());
        if (isAdminCommented) {
            holder.veryfied.setVisibility(View.VISIBLE);
        } else {
            holder.veryfied.setVisibility(View.GONE);
        }
    }

    private void showEditDeleteDialog(Context context, BinhLuan binhLuan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hoạt động");
        String[] actions = {"Sửa bình luận", "Xóa bình luận"};
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        editBinhLuan(context, binhLuan);
                        break;
                    case 1:
                        deleteBinhLuan(context, binhLuan);
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void editBinhLuan(Context context, BinhLuan binhLuan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa bình luận");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_binhluan, null);
        builder.setView(dialogView);
        TextInputEditText editTextTenNguoiDung = dialogView.findViewById(R.id.editTextTenNguoiDung);
        TextInputEditText editTextBinhLuan = dialogView.findViewById(R.id.editTextBinhLuan);
        editTextTenNguoiDung.setText(binhLuan.getTenNguoiDung());
        editTextBinhLuan.setText(binhLuan.getNoiDung());
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newBinhLuanContent = editTextBinhLuan.getText().toString().trim();
                SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();
                if (!newBinhLuanContent.isEmpty()) {
                    binhLuan.setNoiDung(newBinhLuanContent);
                    String thoiGianChinhSua = getCurrentDateTime() + " (Đã chỉnh sửa)";
                    binhLuan.setThoiGian(thoiGianChinhSua);
                    ContentValues values = new ContentValues();
                    values.put("binhLuan", newBinhLuanContent);
                    values.put("thoiGian", thoiGianChinhSua);
                    String selection = "maBL = ?";
                    String[] selectionArgs = {String.valueOf(binhLuan.getMaBL())};
                    int updatedRows = database.update("BinhLuan", values, selection, selectionArgs);
                    if (updatedRows > 0) {
                        Toast.makeText(context, "Đã cập nhật bình luận", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Không thể cập nhật bình luận", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
                database.close();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:s:a", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void deleteBinhLuan(Context context, BinhLuan binhLuan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa bình luận");
        builder.setMessage("Bản có chắc muốn xóa bình luận " + binhLuan.getNoiDung() + "?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();
                String selection = "maBL = ?";
                Toast.makeText(context, String.valueOf(binhLuan.getMaBL()), Toast.LENGTH_SHORT).show();
                String[] selectionArgs = {String.valueOf(binhLuan.getMaBL())};
                int deletedRows = database.delete("BinhLuan", selection, selectionArgs);
                if (deletedRows > 0) {
                    listBinhLuan.remove(binhLuan);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Đã xóa bình luận", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể xóa bình luận", Toast.LENGTH_SHORT).show();
                }
                database.close();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isUserAdminShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        return role.equals("admin");
    }

    private boolean isUserAdmin(int maKH, Context context) {
        boolean isAdmin = false;
        SQLiteDatabase database = new DatabaseHelper(context).getReadableDatabase();
        String query = "SELECT role FROM TaiKhoan WHERE maKH = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maKH)});
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndex("role"));
            if (role != null && role.equals("admin")) {
                isAdmin = true;
            }
            cursor.close();
        }

        return isAdmin;
    }

    @Override
    public int getItemCount() {
        return listBinhLuan.size();
    }

    public class BinhLuanViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewBinhLuan, textViewNgayBinhLuan, textViewTenNguoiBinhLuan;

        public ImageView imageAvatar, veryfied;

        public BinhLuanViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBinhLuan = itemView.findViewById(R.id.textViewBinhLuan);
            textViewNgayBinhLuan = itemView.findViewById(R.id.textViewNgayBinhLuan);
            textViewTenNguoiBinhLuan = itemView.findViewById(R.id.textViewTenNguoiBinhLuan);
            imageAvatar = itemView.findViewById(R.id.imgAvatar);
            veryfied = itemView.findViewById(R.id.veryfied);
        }
    }
}
