package com.nuocngot.tvdpro;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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
                }
                else if (position == 3) {
                    bottomNavigationView.setSelectedItemId(R.id.menu_settings);
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}