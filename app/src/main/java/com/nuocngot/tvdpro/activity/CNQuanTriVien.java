package com.nuocngot.tvdpro.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.FunctionAdapter;

import java.util.ArrayList;

public class CNQuanTriVien extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnquan_tri_vien);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chức năng quản trị viên");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.recycleViewQTV);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        ArrayList<String> functionList = new ArrayList<>();
        functionList.add("Quản lý sản phẩm");
        functionList.add("Quản lý người dùng");
        functionList.add("Quản lý danh mục");
        functionList.add("Thống kê doanh số");
        functionList.add("Hỗ trợ khách hàng");
        FunctionAdapter adapter = new FunctionAdapter(functionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}