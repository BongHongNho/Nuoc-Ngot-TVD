package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class stListViewAdapter extends BaseAdapter {
    public ArrayList<stAdpater> stAdpaters = new ArrayList<>();
    public Context context;

    public stListViewAdapter(Context context, ArrayList<stAdpater> stAdapters) {
        this.context = context;
        this.stAdpaters = stAdapters;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return stAdpaters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false);
        TextView stText = view.findViewById(R.id.tvSettings);
        stText.setText(stAdpaters.get(position).getNameSettings());
        return view;
    }
}
