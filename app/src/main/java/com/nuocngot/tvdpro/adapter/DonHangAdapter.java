package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.DonHang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private ArrayList<DonHang> donHangList;


    public DonHangAdapter(ArrayList<DonHang> donHangList) {
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_donhhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.textViewTenDH.setText(donHang.getTenDH());
        holder.textViewTenSPDH.setText(donHang.getTenSPDH());
        int maTTDH = donHang.getMaTTDH();
        if (maTTDH == 1) {
            holder.textViewTrangThai.setText("Chờ xác nhận");
        } else if (maTTDH == 2) {
            holder.textViewTrangThai.setText("Chờ lấy hàng");
        } else if (maTTDH == 3) {
            holder.textViewTrangThai.setText("Đang giao");
        } else if (maTTDH == 4) {
            holder.textViewTrangThai.setText("Đã giao");
        } else if (maTTDH == 5) {
            holder.textViewTrangThai.setText("Đã hủy");
        }
        holder.textViewSoLuongSPDH.setText("Số lượng mua: " + donHang.getSoLuongSPDH());
        holder.textViewTongTienDH.setText("Tổng tiền: " + donHang.getTongTienDH() + " VND");
        Glide.with(holder.itemView.getContext()).load(donHang.getAnhDH()).placeholder(R.drawable.placeholder).into(holder.imageView);
        holder.textViewNgayMua.setText(donHang.getNgayMua());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                showThongTinDH(position, holder.itemView.getContext());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = holder.getAdapterPosition(); // Lưu vị trí cuối cùng vào biến final
                SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
                String role = sharedPreferences.getString("role", "");
                if (role.equals("admin")) {
                    showSelectTTDH(position, holder.itemView.getContext()); // Truyền vị trí vào phương thức showSelectTTDH
                    return true;
                }
                return false;
            }
        });
    }
    private void showSelectTTDH(final int position, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái đơn hàng");
        String[] trangThaiDonHang = {"Chờ xác nhận", "Chờ lấy hàng", "Đang giao", "Đã giao", "Đã hủy"};
        builder.setItems(trangThaiDonHang, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedTTDH = trangThaiDonHang[which];
                updateDonHangTrangThai(position, selectedTTDH, context);
            }
        });

        builder.show();
    }
    private void updateDonHangTrangThai(int position, String selectedTTDH, Context context) {
        DonHang donHang = donHangList.get(position);
        int maTTDH = -1;
        switch (selectedTTDH) {
            case "Chờ xác nhận":
                maTTDH = 1;
                break;
            case "Chờ lấy hàng":
                maTTDH = 2;
                break;
            case "Đang giao":
                maTTDH = 3;
                break;
            case "Đã giao":
                maTTDH = 4;
                break;
            case "Đã hủy":
                maTTDH = 5;
                break;
        }

        if (maTTDH != -1) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:aa");
            String ngayCapNhat = sdf.format(cal.getTime());
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("maTTDH", maTTDH);
            values.put("ngayMua", ngayCapNhat);
            int maDM = donHang.getMaDM();
            String selection = "maDMUA = ?";
            String[] selectionArgs = {String.valueOf(maDM)};
            int rowsAffected = db.update("DonMua", values, selection, selectionArgs);
            db.close();
            dbHelper.close();
            if (rowsAffected > 0) {
                donHang.setMaTTDH(maTTDH);
                donHang.setNgayMua(ngayCapNhat);
                notifyDataSetChanged();
                Toast.makeText(context, "Cập nhật trạng thái đơn hàng thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Cập nhật trạng thái đơn hàng không thành công", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Không tìm thấy mã trạng thái", Toast.LENGTH_SHORT).show();
        }
    }
    private void showThongTinDH(int position, Context context) {
        if (position != RecyclerView.NO_POSITION) {
            DonHang donHang = donHangList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chi tiết đơn hàng");
            StringBuilder dialogContent = new StringBuilder();
            dialogContent.append("Tên đơn hàng: ").append(donHang.getTenDH()).append("\n")
                    .append("Tên sản phẩm: ").append(donHang.getTenSPDH()).append("\n")
                    .append("Số lượng: ").append(donHang.getSoLuongSPDH()).append("\n")
                    .append("Tổng tiền: ").append(donHang.getTongTienDH()).append(" VND").append("\n")
                    .append("Ngày mua: ").append(donHang.getNgayMua());

            builder.setMessage(dialogContent.toString());
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTenDH;
        public TextView textViewTenSPDH;
        public TextView textViewSoLuongSPDH;

        public TextView textViewTrangThai;
        public TextView textViewTongTienDH;
        public TextView textViewNgayMua;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTenDH = itemView.findViewById(R.id.textViewTenDH);
            textViewTenSPDH = itemView.findViewById(R.id.textViewTenSPDH);
            textViewSoLuongSPDH = itemView.findViewById(R.id.textViewSoLuongSPDH);
            textViewTongTienDH = itemView.findViewById(R.id.textViewTongTienDH);
            textViewNgayMua = itemView.findViewById(R.id.textViewNgayMua);
            imageView = itemView.findViewById(R.id.imageViewSPTTDH);
            textViewTrangThai = itemView.findViewById(R.id.textViewTrangThai);
        }
    }
}

