package com.nuocngot.tvdpro;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.adapter.FunctionAdapter;

import java.util.ArrayList;

public class QLTaiKhoanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qltai_khoan);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleViewQL);
        toolbar.setTitle("Quản lý tài khoản");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        ArrayList<String> functionList = new ArrayList<>();
        functionList.add("Đổi ảnh đại diện");
        functionList.add("Đổi tên đăng nhập");
        functionList.add("Đổi mật khẩu" );
        functionList.add("Thay đổi địa chỉ");
        functionList.add("Thay đổi thông tin tài khoản");
        functionList.add("Nâng cấp tài khỏan");
        functionList.add("Xóa tài khoản");
        FunctionAdapter adapter = new FunctionAdapter(functionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}