package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.QLUser;

import java.util.ArrayList;

public class QLUserAdapter extends RecyclerView.Adapter<QLUserAdapter.QLUserViewHolder> {

    private ArrayList<QLUser> qlUsers = new ArrayList<>();
    public Context context;

    public QLUserAdapter(ArrayList<QLUser> qlUsers) {
        this.qlUsers = qlUsers;
    }

    @NonNull
    @Override
    public QLUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ql_user, parent, false);
        return new QLUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QLUserViewHolder holder, int position) {
        QLUser qlUser = qlUsers.get(position);
        holder.textViewTenTK.setText("Tên: " + qlUser.getTenKH());
        holder.textViewEmail.setText("Email: " + qlUser.getEmailKH());
        holder.textViewViTri.setText("Vị trí: " + qlUser.getViTri());
        holder.textViewDiaChi.setText("Địa chỉ: " + qlUser.getDiaChiKH());
        holder.textViewMaTK.setText("Mã TK: " + qlUser.getMaTK());
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        if (role.equals("admin")) {
            holder.imageViewKhungAvt.setImageDrawable(holder.imageViewKhungAvt.getContext().getResources().getDrawable(R.drawable.khung_vip_02));
        } else {
            holder.imageViewKhungAvt.setImageDrawable(holder.imageViewKhungAvt.getContext().getResources().getDrawable(R.drawable.khung_vip_03));
        }
        Glide.with(holder.imageViewEdit.getContext()).load(qlUser.getAnhKH()).placeholder(R.drawable.placeholder).into(holder.imageViewEdit);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int position = holder.getAdapterPosition();
                QLUser qlUser = qlUsers.get(position);
                showInfoDialog(holder.itemView.getContext(), qlUser);
            }
        });
    }

    private void showInfoDialog(Context context, QLUser qlUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông tin người dùng");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_info_user, null);
        builder.setView(view);
        ImageView imageViewHA = view.findViewById(R.id.imageViewHA);
        TextView textViewTenTK = view.findViewById(R.id.textViewTenTK);
        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
        TextView textViewViTri = view.findViewById(R.id.textViewViTri);
        TextView textViewDiaChi = view.findViewById(R.id.textViewDiaChi);
        TextView textViewMaTK = view.findViewById(R.id.textViewMaTK);
        textViewTenTK.setText("Tên: " + qlUser.getTenKH());
        textViewEmail.setText("Email: " + qlUser.getEmailKH());
        textViewViTri.setText("Vị trí: " + qlUser.getViTri());
        textViewDiaChi.setText("Địa chỉ: " + qlUser.getDiaChiKH());
        textViewMaTK.setText("Mã TK: " + qlUser.getMaTK());
       Glide.with(imageViewHA.getContext()).load(qlUser.getAnhKH()).placeholder(R.drawable.placeholder).into(imageViewHA);
       builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
       });
       builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
           }
       });
       builder.show();
    }


    @Override
    public int getItemCount() {
        return qlUsers.size();
    }

    public class QLUserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTenTK, textViewEmail, textViewViTri, textViewMaTK, textViewDiaChi;
        ImageView imageViewEdit, imageViewKhungAvt;

        public QLUserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTenTK = itemView.findViewById(R.id.textViewTenKH);
            textViewEmail = itemView.findViewById(R.id.textViewSDTEmail);
            textViewViTri = itemView.findViewById(R.id.textViewViTri);
            textViewMaTK = itemView.findViewById(R.id.textViewMaTK);
            imageViewEdit = itemView.findViewById(R.id.imageViewAnhKH);
            textViewDiaChi = itemView.findViewById(R.id.textViewDiaChi);
            imageViewKhungAvt = itemView.findViewById(R.id.khungVIPImageView);
        }
    }
}
