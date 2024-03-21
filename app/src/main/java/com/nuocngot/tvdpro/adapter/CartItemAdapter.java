package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.activity.CartActivity;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class CartItemAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<CartItem> cartItemList = new ArrayList<>();

    public CartItemAdapter(CartActivity cartActivity, ArrayList<CartItem> cartItems) {
        this.context = cartActivity;
        this.cartItemList = cartItems;
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        TextView tvProductName, tvProductPrice, tvProductQuantity, tvEditSL, tvMinus, tvPlus;
        ImageView ivProductImage;
        ivProductImage = view.findViewById(R.id.imgCartImg);
        Glide.with(context).load(cartItemList.get(position).getProductImage()).into(ivProductImage);
        tvProductName = view.findViewById(R.id.tvProductName);
        tvProductPrice = view.findViewById(R.id.tvProductPrice);
        tvProductQuantity = view.findViewById(R.id.tvProductQuantity);
        tvEditSL = view.findViewById(R.id.tvEditSL);
        tvMinus = view.findViewById(R.id.tvMinus);
        tvPlus = view.findViewById(R.id.tvPlus);
        int productQuantityInCart = getProductQuantityInCart(cartItemList.get(position).getProductId());
        tvEditSL.setText(String.valueOf(productQuantityInCart));
        tvProductName.setText("Tên sản phẩm: " + cartItemList.get(position).getProductName());
        tvProductPrice.setText("Giá: " + getTotalPay(cartItemList.get(position).getProductId()));
        tvProductQuantity.setText("Số lượng: " + getProductQuantityInCart(cartItemList.get(position).getProductId()));
        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(tvEditSL.getText().toString());
                if (currentQuantity > 0) {
                    currentQuantity--;
                    tvEditSL.setText(String.valueOf(currentQuantity));
                    tvProductQuantity.setText("Số lượng: " + currentQuantity);
                    int productPrice = cartItemList.get(position).getProductPrice();
                    int totalPrice = currentQuantity * productPrice;
                    tvProductPrice.setText("Giá: " + String.valueOf(totalPrice));
                    cartItemList.get(position).setProductQuantity(currentQuantity);
                    int productId = cartItemList.get(position).getProductId();
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.updateCartItem(productId, currentQuantity, totalPrice);
                    dbHelper.getTotalPay();
                }
            }
        });

        tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(tvEditSL.getText().toString());
                currentQuantity++;
                tvEditSL.setText(String.valueOf(currentQuantity));
                tvProductQuantity.setText("Số lượng: " + currentQuantity);
                int productPrice = cartItemList.get(position).getProductPrice();
                int totalPrice = currentQuantity * productPrice;
                tvProductPrice.setText("Giá: " + String.valueOf(totalPrice));
                cartItemList.get(position).setProductQuantity(currentQuantity);
                int productId = cartItemList.get(position).getProductId();
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.updateCartItem(productId, currentQuantity, totalPrice);
                dbHelper.getTotalPay();
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc chắn muốn xóa " + cartItemList.get(position).getProductName() + "?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            dbHelper.deleteCartItem(cartItemList.get(position).getProductId());
                            cartItemList.remove(position);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });
        return view;
    }

    public int getTotalPay(int productId) {
        int totalPay = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tongTien) FROM GioHang WHERE maSP = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            totalPay = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalPay;
    }

    private int getProductQuantityInCart(int productId) {
        int quantity = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT soLuongSP FROM GioHang WHERE maSP = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("soLuongSP"));
        }
        cursor.close();
        db.close();

        return quantity;
    }

}
