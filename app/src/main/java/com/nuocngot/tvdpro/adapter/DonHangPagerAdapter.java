package com.nuocngot.tvdpro.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nuocngot.tvdpro.fragment.ChoLayHangFragment;
import com.nuocngot.tvdpro.fragment.ChoXacNhanFragment;
import com.nuocngot.tvdpro.fragment.DaGiaoFragment;
import com.nuocngot.tvdpro.fragment.DaHuyFragment;
import com.nuocngot.tvdpro.fragment.DangGiaoFragment;

public class DonHangPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 5;

    public DonHangPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChoXacNhanFragment();
            case 1:
                return new ChoLayHangFragment();
            case 2:
                return new DangGiaoFragment();
            case 3:
                return new DaGiaoFragment();
            case 4:
                return new DaHuyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
