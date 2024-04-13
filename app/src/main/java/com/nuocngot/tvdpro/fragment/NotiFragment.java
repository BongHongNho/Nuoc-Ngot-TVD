package com.nuocngot.tvdpro.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.ThongBaoAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.ThongBao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NotiFragment extends Fragment {

    private RecyclerView recyclerViewNoti;
    private ThongBaoAdapter thongBaoAdapter;
    private List<ThongBao> thongBaoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);
        thongBaoList = loadThongBaoList(requireContext(), 100);
        recyclerViewNoti = view.findViewById(R.id.recycleViewNoti);
        thongBaoAdapter = new ThongBaoAdapter(requireContext(), thongBaoList);
        recyclerViewNoti.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewNoti.setAdapter(thongBaoAdapter);
        return view;
    }

    public static List<ThongBao> loadThongBaoList(Context context, int count) {
        List<ThongBao> danhSachThongBao = new ArrayList<>();
        String[] tieuDeThongBao = {
                "Thông báo đặt hàng thành công!",
                "Thanh toán thành công",
                "Cập nhật địa chỉ giao hàng",
                "Cảm ơn đã mua hàng tại cửa hàng của chúng tôi!",
                "Sản phẩm mới đã được cập nhật"
        };
        String[] noiDungThongBao = {
                "Chúc mừng bạn đã đặt hàng thành công!",
                "Đã nhận thanh toán của bạn.",
                "Thiếu thông tin về địa chỉ giao hàng, vui lòng cập nhật.",
                "Cảm ơn bạn đã mua hàng tại cửa hàng chúng tôi!",
                "Cập nhật sản phẩm mới nhất từ cửa hàng của chúng tôi."
        };
        String[] anhThongBaoUrls = {
                "https://cdn-icons-png.flaticon.com/512/3119/3119338.png",
        };
        Random random = new Random();
        int day = random.nextInt(30) + 1;
        int month = random.nextInt(12) + 1;
        int year = 2024;
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        int millisecond = random.nextInt(1000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        Date randomDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:a");
        String thoiGian = dateFormat.format(randomDate);
        for (int i = 0; i < count; i++) {
            String tieuDe = tieuDeThongBao[i % tieuDeThongBao.length];
            String noiDung = noiDungThongBao[i % noiDungThongBao.length];
            String anhThongBaoUrl = anhThongBaoUrls[i % anhThongBaoUrls.length];
            ThongBao thongBao = new ThongBao(tieuDe, noiDung, thoiGian, anhThongBaoUrl);
            danhSachThongBao.add(thongBao);
        }
        return danhSachThongBao;
    }
}
