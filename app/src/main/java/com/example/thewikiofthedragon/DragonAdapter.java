package com.example.thewikiofthedragon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;


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
        Dragon dragon = dragonList.get(position);
        holder.bind(dragon, listener);

        // Lanzar la actividad de detalle al hacer clic en un dragón
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DragonDetailActivity.class);
            intent.putExtra("dragonName", dragon.getName());
            intent.putExtra("dragonImage", dragon.getImageResId());
            intent.putExtra("dragonBiography", dragon.getBiography());
            v.getContext().startActivity(intent);
        });
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
