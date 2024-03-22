package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class BuyAcitivyAdapter extends RecyclerView.Adapter<BuyAcitivyAdapter.BuyAcitivyViewHolder> {
    private OnItemClickCallback onItemClickCallback;
    private ArrayList<BuyActivityItem> buyActivityItems;
    private Context context;

    public BuyAcitivyAdapter(Context context) {
        this.context = context;
        buyActivityItems = new ArrayList<>();
    }

    public void setBuyActivityItems(ArrayList<BuyActivityItem> buyActivityItems) {
        this.buyActivityItems = buyActivityItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BuyAcitivyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity_cart_buy, parent, false);
        return new BuyAcitivyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAcitivyViewHolder holder, int position) {
        BuyActivityItem buyActivityItem = buyActivityItems.get(position);
        holder.textView.setText(buyActivityItem.getNameBuyActivity());
        holder.imageView.setImageResource(buyActivityItem.getImageBuyAcvitity());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickCallback != null) {
                    onItemClickCallback.onItemClicked(buyActivityItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buyActivityItems.size();
    }

    public class BuyAcitivyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public BuyAcitivyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewBuyActivity);
            imageView = itemView.findViewById(R.id.imgBuyActivity);
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(BuyActivityItem buyAcitivy);
    }
}