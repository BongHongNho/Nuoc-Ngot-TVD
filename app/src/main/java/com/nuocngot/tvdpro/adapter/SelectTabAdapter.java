package com.nuocngot.tvdpro.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nuocngot.tvdpro.R;

import java.util.ArrayList;

public class SelectTabAdapter extends RecyclerView.Adapter<SelectTabAdapter.ViewHolder> {
    private ArrayList<SelectTab> selectTabs;
    private Context context;

    public SelectTabAdapter(Context context, ArrayList<SelectTab> selectTabs) {
        this.context = context;
        this.selectTabs = selectTabs;
    }

    @NonNull
    @Override
    public SelectTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_tablayout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTabAdapter.ViewHolder holder, int position) {
        SelectTab selectTab = selectTabs.get(position);
        holder.tabName.setText(selectTab.getTabName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectTabs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tabName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tabName = itemView.findViewById(R.id.tvtabTitle);
        }
    }

}
