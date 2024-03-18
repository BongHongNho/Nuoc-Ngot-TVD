package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nuocngot.tvdpro.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Category> categoryArrayList = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public int getCount() {
        return categoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false);
        TextView tvName = view.findViewById(R.id.tvSettings);
        tvName.setText(categoryArrayList.get(position).getCategoryName());
        return view;
    }
}
