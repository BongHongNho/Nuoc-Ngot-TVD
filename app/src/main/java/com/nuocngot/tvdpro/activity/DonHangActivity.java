package com.nuocngot.tvdpro.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nuocngot.tvdpro.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.nuocngot.tvdpro.adapter.BuyActivityItem;
import com.nuocngot.tvdpro.adapter.DonHangPagerAdapter;

import java.util.ArrayList;

public class DonHangActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private  ArrayList<BuyActivityItem> functionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> finish());
        int selectedTabPosition = getIntent().getIntExtra("selected_tab_position", 0);
        DonHangPagerAdapter adapter = new DonHangPagerAdapter(this);
        viewPager.setAdapter(adapter);
        functionList.add(new BuyActivityItem(R.drawable.wait_confirm, "Chờ xác nhận"));
        functionList.add(new BuyActivityItem(R.drawable.wait_get_package, "Chờ lấy hàng"));
        functionList.add(new BuyActivityItem(R.drawable.delivering, "Đang giao"));
        functionList.add(new BuyActivityItem(R.drawable.delivered, "Đã giao"));
        functionList.add(new BuyActivityItem(R.drawable.cancel_box, "Đã hủy"));
        for (int i = 0; i < functionList.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View customView = LayoutInflater.from(DonHangActivity.this).inflate(R.layout.item_tab_donhang, null);
            ImageView tabIcon = customView.findViewById(R.id.imgBuyActivity);
            TextView tabText = customView.findViewById(R.id.textViewBuyActivity);
            tabIcon.setImageResource(functionList.get(i).getImageBuyAcvitity());
            tabText.setText(functionList.get(i).getNameBuyActivity());
            tab.setCustomView(customView);
            tabLayout.addTab(tab);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager.setCurrentItem(selectedTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void addCustomTabs(TabLayout tabLayout, ArrayList<BuyActivityItem> itemList) {
        for (BuyActivityItem item : itemList) {
            TabLayout.Tab tab = tabLayout.newTab();
            View customView = LayoutInflater.from(DonHangActivity.this).inflate(R.layout.item_activity_cart_buy, null);
            ImageView tabIcon = customView.findViewById(R.id.textViewBuyActivity);
            TextView tabText = customView.findViewById(R.id.imgBuyActivity);
            tabIcon.setImageResource(item.getImageBuyAcvitity());
            tabText.setText(item.getNameBuyActivity());
            tab.setCustomView(customView);
            tabLayout.addTab(tab);
        }
    }

}

