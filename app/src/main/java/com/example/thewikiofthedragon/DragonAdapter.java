package com.example.thewikiofthedragon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DragonAdapter extends RecyclerView.Adapter<DragonAdapter.DragonViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Dragon dragon);
    }

    private List<Dragon> dragonList;
    private OnItemClickListener listener;

    public DragonAdapter(List<Dragon> dragonList, OnItemClickListener listener) {
        this.dragonList = dragonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DragonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dragon, parent, false);
        return new DragonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonViewHolder holder, int position) {
        holder.bind(dragonList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dragonList.size();
    }

    static class DragonViewHolder extends RecyclerView.ViewHolder {
        private TextView dragonName;
        private ImageView dragonImage;

        DragonViewHolder(View itemView) {
            super(itemView);
            dragonName = itemView.findViewById(R.id.dragonNameTextView);
            dragonImage = itemView.findViewById(R.id.dragonImageView);
        }

        void bind(final Dragon dragon, final OnItemClickListener listener) {
            dragonName.setText(dragon.getName());
            dragonImage.setImageResource(dragon.getImageResId());
            itemView.setOnClickListener(v -> listener.onItemClick(dragon));
        }
    }
}
