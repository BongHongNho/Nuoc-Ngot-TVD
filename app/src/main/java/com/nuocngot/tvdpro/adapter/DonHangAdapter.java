package com.nuocngot.tvdpro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.DonHang;

import java.util.ArrayList;

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
        holder.textViewSoLuongSPDH.setText("Số lượng mua: " + donHang.getSoLuongSPDH());
        holder.textViewTongTienDH.setText("Tổng tiền: " + donHang.getTongTienDH() + " VND");
        Glide.with(holder.itemView.getContext()).load(donHang.getAnhDH()).placeholder(R.drawable.placeholder).into(holder.imageView);
        holder.textViewNgayMua.setText(donHang.getNgayMua());
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTenDH;
        public TextView textViewTenSPDH;
        public TextView textViewSoLuongSPDH;
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
        }
    }
}

