package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.model.LichSu;

import java.util.ArrayList;
import java.util.List;

public class LichSuMuaHangAdapter extends ArrayAdapter<LichSu> {

    private Context context;
    private ArrayList<LichSu> donMuaList;

    public LichSuMuaHangAdapter(Context context, ArrayList<LichSu> donMuaList) {
        super(context, R.layout.item_lich_su_mua_hang, donMuaList);
        this.context = context;
        this.donMuaList = donMuaList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_lich_su_mua_hang, null);
        }
        LichSu donMua = donMuaList.get(position);
        if (donMua != null) {
            TextView tenSanPhamTextView = view.findViewById(R.id.tenSanPhamTextView);
            TextView ngayMuaTextView = view.findViewById(R.id.ngayMuaTextView);
            TextView tongTienTextView = view.findViewById(R.id.tongTienTextView);
            tenSanPhamTextView.setText("Tên sản phẩm: " + donMua.getTenSanPham());
            ngayMuaTextView.setText("Ngày mua: " + donMua.getNgayMua());
            tongTienTextView.setText("Tổng tiền: " + String.valueOf(donMua.getTongTien()));
        }
        return view;
    }
}
