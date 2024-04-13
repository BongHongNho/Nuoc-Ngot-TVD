package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {

    private Context context;
    private List<ThongBao> thongBaoList;

    public ThongBaoAdapter(Context context, List<ThongBao> thongBaoList) {
        this.context = context;
        this.thongBaoList = thongBaoList;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noti_item, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);
        holder.textViewNoti.setText(thongBao.getTitle());
        holder.textViewNotiND.setText(thongBao.getNoidung());
        holder.textViewTime.setText(thongBao.getTime());
        Glide.with(context).load(thongBao.getHinhAnh()).placeholder(R.drawable.placeholder).into(holder.imgNoti);
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNoti, textViewNotiND, textViewTime;
        ImageView imgNoti;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNoti = itemView.findViewById(R.id.textViewNoti);
            textViewNotiND = itemView.findViewById(R.id.textViewNotiND);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imgNoti = itemView.findViewById(R.id.imgNoti);
        }
    }
}
