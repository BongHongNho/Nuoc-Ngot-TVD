package com.nuocngot.tvdpro.adapter;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.activity.DetailsActivity;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class viewDrinksAdapter extends RecyclerView.Adapter<viewDrinksAdapter.ViewHolder> {
    private Context mContext;
    public ArrayList<productAdapter> dataList;

    public viewDrinksAdapter(Context context, ArrayList<productAdapter> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    public viewDrinksAdapter(ArrayList<productAdapter> productDataList) {
        this.dataList = productDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drinks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.proName.setText(dataList.get(position).getItemName());
        holder.proCost.setText(dataList.get(position).getItemPrice() + " VNĐ");
        String imageUrl = dataList.get(position).getItemImage();
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.img_avatar_nam)
                .into(holder.proImage);
        holder.proSL.setText("Kho: " + (dataList.get(position).getItemQuantity()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId = dataList.get(position).getId_product();
                ProductDetail productDetail = getProductDetailFromDatabase(productId);
                if (productDetail != null) {
                    Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                    intent.putExtra("productId", productDetail.getProductId());
                    intent.putExtra("productImage", productDetail.getImageUrl());
                    intent.putExtra("productName", productDetail.getProductName());
                    intent.putExtra("productDetails", productDetail.getInfo());
                    intent.putExtra("productQuantity", productDetail.getQuantity());
                    intent.putExtra("productPrice", productDetail.getPrice());
                    startActivity(view.getContext(), intent, null);
                }
            }
        });

    }

    private ProductDetail getProductDetailFromDatabase(int productId) {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ProductDetail productDetail = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM ChiTietSanPham WHERE maSP = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});
            if (cursor.moveToFirst()) {
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                String tenSP = cursor.getString(cursor.getColumnIndex("tenSP"));
                int soLuongSP = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
                int giaSP = cursor.getInt(cursor.getColumnIndex("giaSP"));
                String xuatXu = cursor.getString(cursor.getColumnIndex("xuatXu"));
                String thongTinSP = cursor.getString(cursor.getColumnIndex("thongTinSP"));
                productDetail = new ProductDetail(productId, hinhAnh, tenSP, soLuongSP, giaSP, xuatXu, thongTinSP);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving product detail from database: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return productDetail;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView proName, proCost, proSL;
        ImageView proImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proName = itemView.findViewById(R.id.productName);
            proCost = itemView.findViewById(R.id.productCost);
            proSL = itemView.findViewById(R.id.productSL);
            proImage = itemView.findViewById(R.id.imgPro);
        }
    }
}

