package com.nuocngot.tvdpro.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.activity.ChiTietSPActivity;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    private Context context;
    private ArrayList<SanPham> sanPhamList;

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SanPhamAdapter(Context context, ArrayList<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    public SanPhamAdapter(ArrayList<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);
        holder.tenSP.setText(sanPham.getTenSP());
        holder.giaSP.setText(String.valueOf(sanPham.getGia()) + " VNĐ");
        if (sanPham.getSoLuong() == 0) {
            holder.hinhAnh.setImageResource(R.drawable.sold_out);
            holder.itemView.setOnClickListener(null);
            holder.itemView.setClickable(true);
            holder.slKho.setText("Hết hàng");
        } else {
            Glide.with(holder.hinhAnh.getContext()).load(sanPham.getHinhAnh()).placeholder(R.drawable.placeholder).into(holder.hinhAnh);
            holder.slKho.setText("Kho: " + String.valueOf(sanPham.getSoLuong()));
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), ChiTietSPActivity.class);
                    intent.putExtra("maSP", sanPham.getMaSP());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Context context = v.getContext();
                    if (context != null) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("login_status", Context.MODE_PRIVATE);
                        int maTK = sharedPreferences.getInt("maTK", -1);
                        if (checkAdminRole(v.getContext(), maTK)) {
                            showAdminDialog(v.getContext(), position);
                        } else {

                        }
                    }
                    return true;
                }
            });
        }
    }


    public boolean checkAdminRole(Context context, int maTK) {
        boolean isAdmin = false;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT role FROM TaiKhoan WHERE maTK = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maTK)});
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndex("role"));
            if (role.equals("admin")) {
                isAdmin = true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isAdmin;
    }


    private void showAdminDialog(Context context, int position) {
        String[] options = {"Xóa sản phẩm", "Sửa sản phẩm"};

        new AlertDialog.Builder(context)
                .setTitle("Chức năng sản phẩm")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showDeleteProductDialog(context, position);
                                break;
                            case 1:
                                showEditProductDialog(context, position);
                                break;
                        }
                    }
                })
                .setPositiveButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showEditProductDialog(Context context, int position) {
        int productId_edit = sanPhamList.get(position).getMaSP();
        DatabaseHelper dbHelper_edit = new DatabaseHelper(context);
        SQLiteDatabase db_edit = dbHelper_edit.getReadableDatabase();
        String[] projection = {"tenSP", "gia", "soLuong", "hinhAnh", "maDM"}; // Thêm cột maDM vào projection
        String selection_edit = "maSP = ?";
        String[] selectionArgs_edit = {String.valueOf(productId_edit)};
        Cursor cursorSanPham = db_edit.query(
                "SanPham",
                projection,
                selection_edit,
                selectionArgs_edit,
                null,
                null,
                null
        );

        // Truy vấn dữ liệu từ bảng ChiTietSanPham
        String[] projectionChiTiet = {"xuatXu", "thongTinSP"};
        Cursor cursorChiTiet = db_edit.query(
                "ChiTietSanPham",
                projectionChiTiet,
                selection_edit,
                selectionArgs_edit,
                null,
                null,
                null
        );

        if (cursorSanPham != null && cursorSanPham.moveToFirst() && cursorChiTiet != null && cursorChiTiet.moveToFirst()) {
            String tenSP = cursorSanPham.getString(cursorSanPham.getColumnIndexOrThrow("tenSP"));
            int gia = cursorSanPham.getInt(cursorSanPham.getColumnIndexOrThrow("gia"));
            int soLuong = cursorSanPham.getInt(cursorSanPham.getColumnIndexOrThrow("soLuong"));
            String hinhAnh = cursorSanPham.getString(cursorSanPham.getColumnIndexOrThrow("hinhAnh"));
            int maDM = cursorSanPham.getInt(cursorSanPham.getColumnIndexOrThrow("maDM")); // Lấy maDM từ cột maDM
            String xuatXu = cursorChiTiet.getString(cursorChiTiet.getColumnIndexOrThrow("xuatXu"));
            String thongTinSP = cursorChiTiet.getString(cursorChiTiet.getColumnIndexOrThrow("thongTinSP"));
            showEditProductDialog(context, productId_edit, tenSP, gia, soLuong, hinhAnh, maDM, xuatXu, thongTinSP); // Sửa tham số truyền vào
        }
        if (cursorSanPham != null) {
            cursorSanPham.close();
        }
        if (cursorChiTiet != null) {
            cursorChiTiet.close();
        }
        db_edit.close();
        dbHelper_edit.close();
    }

    private void showEditProductDialog(Context context, int productId, String tenSP, int gia, int soLuong, String hinhAnh, int maDM, String xuatXu, String thongTinSP) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_sp_dialog, null);
        builder.setView(dialogView);
        builder.setTitle("Sửa thông tin");

        TextInputEditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        TextInputEditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        TextInputEditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);
        TextInputEditText imageViewProduct = dialogView.findViewById(R.id.imageViewProduct);
        TextInputEditText editTextXuatXu = dialogView.findViewById(R.id.editTextXuatxu);
        TextInputEditText editTextThongTinSP = dialogView.findViewById(R.id.editTextThongTin);
        Spinner spinnerDanhMuc = dialogView.findViewById(R.id.spinnerDanhMuc); // Thêm Spinner danh mục
        editTextProductName.setText(tenSP);
        editTextProductPrice.setText(String.valueOf(gia));
        editTextProductQuantity.setText(String.valueOf(soLuong));
        imageViewProduct.setText(hinhAnh);
        editTextXuatXu.setText(xuatXu);
        editTextThongTinSP.setText(thongTinSP);
        ArrayList<Category> danhMucList = loadDanhMucList(context);
        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, danhMucList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(spinnerAdapter);
        for (int i = 0; i < danhMucList.size(); i++) {
            if (danhMucList.get(i).getMaDM() == maDM) { // Set giá trị mặc định cho Spinner danh mục
                spinnerDanhMuc.setSelection(i);
                break;
            }
        }
        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productName = editTextProductName.getText().toString();
                String productPrice = editTextProductPrice.getText().toString();
                String productQuantity = editTextProductQuantity.getText().toString();
                String productImage = imageViewProduct.getText().toString();
                String xuatXu = editTextXuatXu.getText().toString();
                String thongTinSP = editTextThongTinSP.getText().toString();
                int maDanhMuc = danhMucList.get(spinnerDanhMuc.getSelectedItemPosition()).getMaDM(); // Lấy maDM từ Spinner danh mục
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues valuesSanPham = new ContentValues();
                valuesSanPham.put("tenSP", productName);
                valuesSanPham.put("gia", Integer.parseInt(productPrice));
                valuesSanPham.put("soLuong", Integer.parseInt(productQuantity));
                valuesSanPham.put("hinhAnh", productImage);
                valuesSanPham.put("maDM", maDanhMuc); // Thêm maDM vào ContentValues
                String selectionSanPham = "maSP = ?";
                String[] selectionArgsSanPham = {String.valueOf(productId)};
                int rowsUpdatedSanPham = db.update("SanPham", valuesSanPham, selectionSanPham, selectionArgsSanPham);
                ContentValues valuesChiTiet = new ContentValues();
                valuesChiTiet.put("tenSP", productName);
                valuesChiTiet.put("gia", Integer.parseInt(productPrice));
                valuesChiTiet.put("soLuong", Integer.parseInt(productQuantity));
                valuesChiTiet.put("hinhAnh", productImage);
                valuesChiTiet.put("xuatXu", xuatXu);
                valuesChiTiet.put("thongTinSP", thongTinSP);
                String selectionChiTiet = "maSP = ?";
                String[] selectionArgsChiTiet = {String.valueOf(productId)};
                int rowsUpdatedChiTiet = db.update("ChiTietSanPham", valuesChiTiet, selectionChiTiet, selectionArgsChiTiet);

                if (rowsUpdatedSanPham > 0 && rowsUpdatedChiTiet > 0) {
                    Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    loadData(context);
                    notifyDataSetChanged();
                }

                db.close();
                dbHelper.close();
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

    private ArrayList<Category> loadDanhMucList(Context context) {
        ArrayList<Category> danhMucList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"maDM", "tenDM"};
        Cursor cursor = db.query("DanhMuc", projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maDM = cursor.getInt(cursor.getColumnIndexOrThrow("maDM"));
                String tenDM = cursor.getString(cursor.getColumnIndexOrThrow("tenDM"));
                Category category = new Category(maDM, tenDM);
                danhMucList.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();
        return danhMucList;
    }


    private void showDeleteProductDialog(Context context, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int productId = sanPhamList.get(position).getMaSP();
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.query(
                                "GioHang",
                                null,
                                "maSP = ?",
                                new String[]{String.valueOf(productId)},
                                null,
                                null,
                                null
                        );

                        if (cursor != null && cursor.getCount() > 0) {
                            String selection = "maSP = ?";
                            String[] selectionArgs = {String.valueOf(productId)};
                            int deletedRows = db.delete("GioHang", selection, selectionArgs);
//            if (deletedRows > 0) {
//                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "Xóa sản phẩm khỏi giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
//            }
                        }

                        String selectionSanPham = "maSP = ?";
                        String[] selectionArgsSanPham = {String.valueOf(productId)};
                        int deletedRowsSanPham = db.delete("SanPham", selectionSanPham, selectionArgsSanPham);
                        if (deletedRowsSanPham > 0) {
                            Toast.makeText(context, "Đã xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            loadData(context);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }

                        // Đóng các đối tượng Cursor và Database khi đã sử dụng xong
                        if (cursor != null) {
                            cursor.close();
                        }
                        db.close();
                        dbHelper.close();
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }


    public void loadData(Context context) {
        sanPhamList.clear();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "maSP",
                "tenSP",
                "gia",
                "soLuong",
                "hinhAnh"
        };
        Cursor cursor = db.query(
                "SanPham",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(cursor.getColumnIndexOrThrow("maSP"));
                String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("tenSP"));
                int gia = cursor.getInt(cursor.getColumnIndexOrThrow("gia"));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                String hinhAnh = cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh"));
                sanPhamList.add(new SanPham(maSP, hinhAnh, tenSP, soLuong, gia));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();
        notifyDataSetChanged(); // Cập nhật RecyclerView sau khi tải dữ liệu mới
    }


    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder {
        ImageView hinhAnh;
        TextView tenSP, giaSP, slKho;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            hinhAnh = itemView.findViewById(R.id.imageViewSanPham);
            tenSP = itemView.findViewById(R.id.textViewTenSP);
            giaSP = itemView.findViewById(R.id.textViewGiaSP);
            slKho = itemView.findViewById(R.id.textViewKho);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
