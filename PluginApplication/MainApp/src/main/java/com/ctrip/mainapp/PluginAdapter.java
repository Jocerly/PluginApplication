package com.ctrip.mainapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PluginAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<File> files;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public PluginAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setFiles(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_plugin, parent, false);
        return new PluginViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PluginViewHolder viewHolder = (PluginViewHolder) holder;
        final File file = files.get(position);
        if (file != null) {
            viewHolder.btnPluginName.setText(file.getName());
            viewHolder.btnPluginName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.ItemClick(file);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }

    class PluginViewHolder extends RecyclerView.ViewHolder {
        public Button btnPluginName;

        public PluginViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPluginName = itemView.findViewById(R.id.btnPluginName);
        }
    }

    public interface OnItemClickListener {
        void ItemClick(File file);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
