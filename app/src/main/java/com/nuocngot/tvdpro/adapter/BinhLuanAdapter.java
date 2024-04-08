package com.nuocngot.tvdpro.adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.ChiTietSPActivity;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.BinhLuan;

import java.util.ArrayList;

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
        boolean isAdminCommented = isUserAdmin(binhLuan.getMaKH(), holder.itemView.getContext());
        if (isAdminCommented) {
            holder.veryfied.setVisibility(View.VISIBLE);
        } else {
            holder.veryfied.setVisibility(View.GONE);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                int position = holder.getAdapterPosition();
//                int maKH = listBinhLuan.get(position).getMaKH();
//                SQLiteDatabase database = new DatabaseHelper(holder.itemView.getContext()).getReadableDatabase();
//                String query = "SELECT maKH FROM BinhLuan WHERE maKH = ?";
//                Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(maKH)});
//                if (cursor != null && cursor.moveToFirst()) {
//                    String role = cursor.getString(cursor.getColumnIndex("maKH"));
//                    if (role != null && role.equals(maKH)) {
//                        Toast.makeText(holder.itemView.getContext(), String.valueOf(maKH), Toast.LENGTH_SHORT).show();
//                    }
//                }
                return false;
            }
        });
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
