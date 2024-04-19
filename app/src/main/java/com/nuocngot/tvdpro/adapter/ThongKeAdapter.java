package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.ThongKeItem;

import java.util.List;

public class ThongKeAdapter extends RecyclerView.Adapter<ThongKeAdapter.ViewHolder> {

    private Context context;
    private List<ThongKeItem> thongKeItemList;

    public ThongKeAdapter(Context context, List<ThongKeItem> thongKeItemList) {
        this.context = context;
        this.thongKeItemList = thongKeItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thong_ke, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongKeItem thongKeItem = thongKeItemList.get(position);
        holder.tvProductName.setText("Tên sản phẩm: " + thongKeItem.getTenSPDH());
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(thongKeItem.getSoLuong()));
        holder.tvTotal.setText("Tổng tiền: " + String.valueOf(thongKeItem.getTongTien()));
    }

    @Override
    public int getItemCount() {
        return thongKeItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}

