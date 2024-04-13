package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.database.DatabaseHelper;
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
        holder.textViewTenTK.setText("Tên: " + qlUser.getTenKH());
        holder.textViewEmail.setText("Email: " + qlUser.getEmailKH());
        holder.textViewViTri.setText("Vị trí: " + qlUser.getViTri());
        holder.textViewDiaChi.setText("Địa chỉ: " + qlUser.getDiaChiKH());
        holder.textViewMaTK.setText("Mã TK: " + qlUser.getMaTK());
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        if (role != null && role.equals("admin")) {
            holder.imageViewKhungAvt.setImageResource(R.drawable.khung_vip_02);
        } else {
            holder.imageViewKhungAvt.setImageResource(R.drawable.khung_vip_03);
        }
        Glide.with(holder.imageViewEdit.getContext())
                .load(qlUser.getAnhKH())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageViewEdit);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                QLUser qlUser = qlUsers.get(position);
                showInfoDialog(holder.itemView.getContext(), qlUser);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                QLUser qlUser = qlUsers.get(position);
                showDeleteDialog(holder.itemView.getContext(), qlUser);
                return true;
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

    private void showDeleteDialog(Context context, QLUser qlUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa người dùng");
        builder.setMessage("Bạn có muốn xóa người dùng " + qlUser.getTenKH() + " ?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    int deletedRowsKhachHang = db.delete("KhachHang", "maKH=?", new String[]{String.valueOf(qlUser.getMaTK())});
                    if (deletedRowsKhachHang > 0) {
                        int deletedRowsTaiKhoan = db.delete("TaiKhoan", "maTK=?", new String[]{String.valueOf(qlUser.getMaTK())});
                        if (deletedRowsTaiKhoan > 0) {
                            db.setTransactionSuccessful();
                        }
                    }
                } catch (Exception e) {
                    Log.e("DeleteError", "Error deleting user: " + e.getMessage());
                } finally {
                    db.endTransaction();
                    db.close();
                    dbHelper.close();
                    ArrayList<QLUser> updatedList = loadQLUserList(context);
                    updateQLUserList(updatedList);
                    Toast.makeText(context, "Đã xóa người dùng " + qlUser.getTenKH(), Toast.LENGTH_SHORT).show();
                }
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

    public void updateQLUserList(ArrayList<QLUser> newList) {
        qlUsers.clear();
        qlUsers.addAll(newList);
        notifyDataSetChanged();
    }

    public ArrayList<QLUser> loadQLUserList(Context context) {
        ArrayList<QLUser> qlUsers = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        String query = "SELECT * FROM NguoiDung";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maKH = cursor.getInt(cursor.getColumnIndex("maND"));
                String tenKH = cursor.getString(cursor.getColumnIndex("tenND"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
                String diaChi = cursor.getString(cursor.getColumnIndex("diaChi"));
                String capTV = cursor.getString(cursor.getColumnIndex("capTV"));
                String hinhAnh = cursor.getString(cursor.getColumnIndex("hinhAnh"));
                QLUser qlUser = new QLUser(maKH, tenKH, email, sdt, diaChi, capTV, hinhAnh);
                qlUsers.add(qlUser);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return qlUsers;
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
