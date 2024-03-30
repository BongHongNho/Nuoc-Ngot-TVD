package com.nuocngot.tvdpro.fragment;

<<<<<<< HEAD
import android.content.Context;
import android.content.SharedPreferences;
=======
>>>>>>> origin/main
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuocngot.tvdpro.R;

<<<<<<< HEAD
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.model.DonHang;
import com.nuocngot.tvdpro.adapter.DonHangAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;

import java.util.ArrayList;

public class ChoLayHangFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonHangAdapter adapter;
    private ArrayList<DonHang> donHangList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cho_lay_hang, container, false);
        recyclerView = rootView.findViewById(R.id.recycaleViewDH);
        adapter = new DonHangAdapter(donHangList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login_status", Context.MODE_PRIVATE);
        int maKH = sharedPreferences.getInt("maKH", -1);
        loadDonMuaData(maKH);
        return rootView;
    }

    private void loadDonMuaData(int maKH) {
        // Xóa các phần tử hiện có trong danh sách
        donHangList.clear();

        // Thực hiện truy vấn cơ sở dữ liệu để lấy dữ liệu đơn hàng của maKH
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "maKH = ?";
        String[] selectionArgs = {String.valueOf(maKH)};
        Cursor cursor = db.query(
                "DonMua",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Kiểm tra xem có dữ liệu không và thêm vào danh sách nếu có
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Lấy thông tin từ cursor
                String tenDH = cursor.getString(cursor.getColumnIndex("tenDH"));
                String tenSPDH = cursor.getString(cursor.getColumnIndex("tenSPDH"));
                int soLuongSPDH = cursor.getInt(cursor.getColumnIndex("soLuong"));
                int tongTienDH = cursor.getInt(cursor.getColumnIndex("tongTien"));

                // Tạo đối tượng DonHang và thêm vào danh sách
                DonHang donHang = new DonHang(tenDH, tenSPDH, soLuongSPDH, tongTienDH);
                donHangList.add(donHang);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Thông báo cho adapter biết dữ liệu đã thay đổi
        adapter.notifyDataSetChanged();
    }


}


=======
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoLayHangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoLayHangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChoLayHangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoLayHangFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoLayHangFragment newInstance(String param1, String param2) {
        ChoLayHangFragment fragment = new ChoLayHangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cho_lay_hang, container, false);
    }
}
>>>>>>> origin/main
