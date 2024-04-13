package com.nuocngot.tvdpro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.ViewPagerAdapter;
import com.nuocngot.tvdpro.fragment.CategoryFragment;
import com.nuocngot.tvdpro.fragment.HomeFragment;
import com.nuocngot.tvdpro.fragment.NotiFragment;
import com.nuocngot.tvdpro.fragment.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_THEME_MODE = "theme_mode";
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFragment(new HomeFragment());
        checkInternet(this);
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
                } else if(itemId == R.id.nav_menu_settings){
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_settings);
                } else if(itemId == R.id.nav_menu_category){
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_category);
                }
                else if(itemId == R.id.nav_menu_noti){
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new NotiFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_noti);
                }
                else if(itemId == R.id.nav_menu_home){
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_converterhome);
                }
                else if(itemId == R.id.nav_info){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Thông tin phiên bản");
                    builder.setMessage("Sinh viên: Nguyễn Huy Phước Tấn\nMã sinh viên: PH28818\nSinh viên: Vi Văn Hậu\nMã sinh viên: PH38983");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                drawerLayout.closeDrawer(navigationView);
                return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.theme_mode) {
            showSelectTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static void checkInternet(Context context) {
        if (isConnected(context)) {

        } else {
            showErrorInternet(context);
        }
    }

    private static void showErrorInternet(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lỗi mạng");
        builder.setMessage("Vui lòng kiểm tra lại kết nối internet");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finishAffinity();
            }
        });
        builder.show();
    }

    private void showSelectTheme() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Chọn chế độ");
        String[] themeModes = new String[]{"Theo hệ thống", "Chế độ sáng", "Chế độ tối"};
        int selectedTheme = getSavedThemeMode();
        builder.setSingleChoiceItems(themeModes, getIndexForTheme(selectedTheme), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedMode = getThemeModeFromIndex(which);
                AppCompatDelegate.setDefaultNightMode(selectedMode);
                saveThemeMode(selectedMode);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private int getIndexForTheme(int themeMode) {
        switch (themeMode) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                return 0;
            case AppCompatDelegate.MODE_NIGHT_NO:
                return 1;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return 2;
            default:
                return 0;
        }
    }

    private int getThemeModeFromIndex(int index) {
        switch (index) {
            case 0:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            case 1:
                return AppCompatDelegate.MODE_NIGHT_NO;
            case 2:
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }

    private void saveThemeMode(int themeMode) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_THEME_MODE, themeMode);
        editor.apply();
    }

    private int getSavedThemeMode() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt(PREF_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
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