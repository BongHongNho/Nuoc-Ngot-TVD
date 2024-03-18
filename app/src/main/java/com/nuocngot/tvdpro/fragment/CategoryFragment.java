package com.nuocngot.tvdpro.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.Category;
import com.nuocngot.tvdpro.adapter.CategoryAdapter;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    public ArrayList<Category> categoryArrayList = new ArrayList<>();

    public ListView listViewCa;

    public FloatingActionButton fabAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listViewCa = view.findViewById(R.id.listViewCa);
        fabAdd = view.findViewById(R.id.fabAdd);
        categoryArrayList.add(new Category("Coca Cola"));
        categoryArrayList.add(new Category("Pepsi"));
        categoryArrayList.add(new Category("Fanta"));
        categoryArrayList.add(new Category("Sprite"));
        categoryArrayList.add(new Category("Sting"));
        categoryArrayList.add(new Category("7Up"));
        categoryArrayList.add(new Category("Mirinda"));
        categoryArrayList.add(new Category("Mountain Dew"));
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList);
        listViewCa.setAdapter(categoryAdapter);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Floating", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}