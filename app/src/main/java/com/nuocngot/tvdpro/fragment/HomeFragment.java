package com.nuocngot.tvdpro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.RecyclerViewAdapter;
import com.nuocngot.tvdpro.adapter.productAdapter;
import com.nuocngot.tvdpro.adapter.tabLayoutAdapter;
import com.nuocngot.tvdpro.adapter.viewDrinksAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageSwitcher imageSwitcher;

    private RecyclerView listView, recycleViewItems;
    private ArrayList<tabLayoutAdapter> dataList = new ArrayList<>();
    private ArrayList<productAdapter> productDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageSwitcher = view.findViewById(R.id.imageSwitcher);
        listView = view.findViewById(R.id.recycleView);
        recycleViewItems = view.findViewById(R.id.recycleViewItems);
        dataList.add(new tabLayoutAdapter("Bán chạy nhất"));
        dataList.add(new tabLayoutAdapter("Sản phẩm mới"));
        dataList.add(new tabLayoutAdapter("Được yêu thích"));
        dataList.add(new tabLayoutAdapter("Được xem nhiều"));
        productDataList.add(new productAdapter("Sản phẩm 1", 10000, R.drawable.an24));
        productDataList.add(new productAdapter("Sản phẩm 2", 10000, R.drawable.an26));
        productDataList.add(new productAdapter("Sản phẩm 3", 10000, R.drawable.an36));
        productDataList.add(new productAdapter("Sản phẩm 4", 10000, R.drawable.an24));
        productDataList.add(new productAdapter("Sản phẩm 5", 10000, R.drawable.an26));
        productDataList.add(new productAdapter("Sản phẩm 6", 10000, R.drawable.an36));
        productDataList.add(new productAdapter("Sản phẩm 7", 10000, R.drawable.an24));
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataList);
        listView.setAdapter(adapter);
        LinearLayoutManager layoutManagerItems = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleViewItems.setLayoutManager(layoutManagerItems);
        viewDrinksAdapter adapterItems = new viewDrinksAdapter(productDataList);
        recycleViewItems.setAdapter(adapterItems);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(requireContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        int[] images = {R.drawable.an24, R.drawable.an26, R.drawable.an36};
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            int currentIndex = 0;
            @Override
            public void run() {
                imageSwitcher.setImageResource(images[currentIndex]);
                currentIndex = (currentIndex + 1) % images.length;
                handler.postDelayed(this, 5000); // 5000 milliseconds = 5 seconds
            }
        });
        return view;
    }
}