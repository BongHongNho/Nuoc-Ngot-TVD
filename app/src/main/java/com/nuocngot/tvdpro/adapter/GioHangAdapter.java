package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.getContext.GetContext;

import java.util.ArrayList;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.GioHangViewHolder> {
    private Context context;
    private ArrayList<GioHangItem> gioHangItemList;
    private OnItemChangeListener listener;

    public GioHangAdapter(Context context, ArrayList<GioHangItem> gioHangItemList, OnItemChangeListener listener) {
        this.context = context;
        this.gioHangItemList = gioHangItemList;
        this.listener = listener;
    }

    public GioHangAdapter(ArrayList<GioHangItem> gioHangItemList) {
        this.gioHangItemList = gioHangItemList;
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        GioHangItem gioHangItem = gioHangItemList.get(position);
        holder.bind(gioHangItem);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            String selection = "maSP = ?";
                            String[] selectionArgs = {String.valueOf(gioHangItemList.get(position).getMaSP())};
                            int deletedRows = db.delete("GioHang", selection, selectionArgs);
                            if (deletedRows > 0) {
                                gioHangItemList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            }
                            db.close();
                            dbHelper.close();
                        })
                        .setNegativeButton("Hủy bỏ", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangItemList.size();
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice, textViewQuantity;
        Button buttonDecrease, buttonIncrease;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
        }

        public void bind(GioHangItem gioHangItem) {
            textViewProductName.setText(gioHangItem.getTenSP());
            textViewProductPrice.setText("Giá: " + String.valueOf(gioHangItem.getGia()) + "VND");
            textViewQuantity.setText(String.valueOf(gioHangItem.getSoLuong()));
            Glide.with(imageViewProduct.getContext())
                    .load(gioHangItem.getAnhSP())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewProduct);
            buttonDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gioHangItem.decreaseQuantity();
                    textViewQuantity.setText(String.valueOf(gioHangItem.getSoLuong())); // Cập nhật số lượng mới trên giao diện
                    updateQuantityInDatabase(gioHangItem.getMaSP(), gioHangItem.getSoLuong());
                }
            });

            buttonIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gioHangItem.increaseQuantity();
                    textViewQuantity.setText(String.valueOf(gioHangItem.getSoLuong()));
                    updateQuantityInDatabase(gioHangItem.getMaSP(), gioHangItem.getSoLuong());
                }
            });

        }
    }

    private void updateQuantityInDatabase(int maSP, int soLuong) {
        DatabaseHelper dbHelper = new DatabaseHelper(GetContext.createAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "maSP = ?";
        String[] selectionArgs = {String.valueOf(maSP)};
        ContentValues values = new ContentValues();
        values.put("soLuong", soLuong);
        int updatedRows = db.update("GioHang", values, selection, selectionArgs);
        db.close();
        dbHelper.close();
        if (updatedRows > 0) {

        } else {

        }
    }

    public interface OnItemChangeListener {
        void onItemChanged(int position, int newQuantity);
    }
}
