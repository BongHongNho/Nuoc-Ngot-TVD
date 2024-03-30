package com.nuocngot.tvdpro.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.ViewPagerAdapter;
import com.nuocngot.tvdpro.fragment.CategoryFragment;
import com.nuocngot.tvdpro.fragment.HomeFragment;
import com.nuocngot.tvdpro.fragment.NotiFragment;
import com.nuocngot.tvdpro.fragment.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager2 viewPager2;
    private DrawerLayout drawerLayout;

    private Map<Integer, Fragment> fragmentMap;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewPager);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        loadFragment(new HomeFragment());
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.menu_converterhome, new HomeFragment());
        fragmentMap.put(R.id.menu_category, new CategoryFragment());
        fragmentMap.put(R.id.menu_noti, new NotiFragment());
        fragmentMap.put(R.id.menu_settings, new SettingsFragment());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentMap.get(R.id.menu_converterhome))
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        int itemId = menuItem.getItemId();
                        if (fragmentMap.containsKey(itemId)) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragmentMap.get(itemId))
                                    .commit();
                            return true;
                        }
                        return false;
                    }
                });

        viewPager2 = findViewById(R.id.viewPager);
        Fragment[] fragments = new Fragment[]{
                new HomeFragment(),
                new CategoryFragment(),
                new NotiFragment(),
                new SettingsFragment()
        };
        viewPager2.setAdapter(new ViewPagerAdapter(this, fragments));
        viewPager2.setCurrentItem(0);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_converterhome);
                } else if (position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_category);
                } else if (position == 2) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_noti);
                } else if (position == 3) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_settings);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_menu_cart) {
                    Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
                    startActivity(intent);
                }
                if(itemId == R.id.nav_menu_settings){
                   fragmentManager.beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.back)
                .setTitle("Nhắc nhở")
                .setMessage("Bạn muôn thoát khỏi ứng dụng ?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        }
                )
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}