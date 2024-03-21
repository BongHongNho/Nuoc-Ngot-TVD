package com.nuocngot.tvdpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.CartItem;
import com.nuocngot.tvdpro.database.DatabaseHelper;

public class DetailsActivity extends AppCompatActivity {
        ImageView imgDetais;
        TextView ivNameDe, tvDetais, tvSoLuongDe, tvGiaTienDE;
        Button btnAddToCard;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chi_tiet_sp);
        }
//
////            imgDetais = findViewById(R.id.imgProDe);
////            ivNameDe = findViewById(R.id.tvNameDe);
////            tvDetais = findViewById(R.id.tvDetais);
////            tvSoLuongDe = findViewById(R.id.tvSoLuongDe);
////            tvGiaTienDE = findViewById(R.id.tvGiaTienDE);
////            btnAddToCard = findViewById(R.id.btnAddToCard);
//
//            Intent intent = getIntent();
//            if (intent != null) {
//                String productName = intent.getStringExtra("productName");
//                String productDetails = intent.getStringExtra("productDetails");
//                int productQuantity = intent.getIntExtra("productQuantity", 0);
//                int productPrice = intent.getIntExtra("productPrice", 0);
//                String imageUrl = intent.getStringExtra("productImage");
//
//                if (imageUrl != null) {
//                    Glide.with(this)
//                            .load(imageUrl)
//                            .placeholder(R.drawable.img_avatar_nam)
//                            .into(imgDetais);
//                }
//
//                ivNameDe.setText(productName);
//                tvDetais.setText(productDetails);
//                tvSoLuongDe.setText("Số lượng: " + String.valueOf(productQuantity));
//                tvGiaTienDE.setText("Giá tiền: " + String.valueOf(productPrice) + " VNĐ");
//            }
//
//            btnAddToCard.setOnClickListener(v -> {
//                addToCartAndOpenCartActivity();
//            });
//        }
//
//        private void addToCartAndOpenCartActivity() {
//            Intent intent = getIntent();
//            int productId = intent.getIntExtra("productId", 0);
//            String productName = intent.getStringExtra("productName");
//            String productImage = intent.getStringExtra("productImage");
//            int productPrice = intent.getIntExtra("productPrice", 0);
//            int productQuantity = intent.getIntExtra("productQuantity", 0);
//
//            if (productId != 0 && productName != null && productImage != null && productPrice != 0 && productQuantity != 0) {
//                DatabaseHelper dbHelper = new DatabaseHelper(this);
//                if (dbHelper.isProductInCart(productId)) {
//                    int currentQuantity = dbHelper.getProductQuantityInCart(this, productId);
//                    int totalPrice = currentQuantity * productPrice;
//                    dbHelper.updateCartItem(productId, currentQuantity + 1 , totalPrice);
//                } else {
//                    CartItem selectedProduct = new CartItem(productId, productName, productImage, productPrice, 1);
//                    dbHelper.addCartItem(selectedProduct);
//                }
//
//                Intent cartIntent = new Intent(DetailsActivity.this, CartActivity.class);
//                startActivity(cartIntent);
//            } else {
//                Toast.makeText(this, "Không thể thêm sản phẩm vào giỏ hàng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//            }
//        }

    }
