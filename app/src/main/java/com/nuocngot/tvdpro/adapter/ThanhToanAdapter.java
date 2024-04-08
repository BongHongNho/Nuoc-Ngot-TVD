package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.getContext.GetContext;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.GioHangViewHolder> {
    private Context context;
    private ArrayList<GioHangItem> gioHangItemList;
    private OnItemChangeListener listener;

    public ThanhToanAdapter(Context context, ArrayList<GioHangItem> gioHangItemList, OnItemChangeListener listener) {
        this.context = context;
        this.gioHangItemList = gioHangItemList;
        this.listener = listener;
    }

    public ThanhToanAdapter(ArrayList<GioHangItem> gioHangItemList) {
        this.gioHangItemList = gioHangItemList;
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_thanhtoan, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        GioHangItem gioHangItem = gioHangItemList.get(position);
        holder.bind(gioHangItem);
    }


    @Override
    public int getItemCount() {
        return gioHangItemList.size();
    }

    public ArrayList<GioHangItem> getSelectedItems() {
        ArrayList<GioHangItem> selectedItems = new ArrayList<>();
        for (GioHangItem item : gioHangItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice, textViewQuantity;
        MaterialCheckBox cbSelectItem;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelectItem = itemView.findViewById(R.id.cbSelectItem);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            cbSelectItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        GioHangItem gioHangItem = gioHangItemList.get(position);
                        updateSelectionInDatabase(gioHangItem.getMaSP(), isChecked);
                    }
                }
            });
        }

        private void updateSelectionInDatabase(int maSP, boolean selected) {
            DatabaseHelper dbHelper = new DatabaseHelper(GetContext.createAppContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("isSelected", selected ? 1 : 0);
            String selection = "maSP = ?";
            String[] selectionArgs = {String.valueOf(maSP)};
            int updatedRows = db.update("GioHang", values, selection, selectionArgs);
            db.close();
            dbHelper.close();
            if (updatedRows > 0) {

            } else {

            }
        }

        public void bind(GioHangItem gioHangItem) {
            textViewProductName.setText(gioHangItem.getTenSP());
            Glide.with(imageViewProduct.getContext())
                    .load(gioHangItem.getAnhSP())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewProduct);
        }
    }
    public interface OnItemChangeListener {
        void onItemChanged(int position, int newQuantity);
    }
}
