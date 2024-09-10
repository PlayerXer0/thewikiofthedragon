package com.example.thewikiofthedragon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Character character);
    }

    private List<Character> characterList;
    private OnItemClickListener listener;

    public CharacterAdapter(List<Character> characterList, OnItemClickListener listener) {
        this.characterList = characterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        holder.bind(characterList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        private TextView characterName;
        private ImageView characterImage;

        CharacterViewHolder(View itemView) {
            super(itemView);
            characterName = itemView.findViewById(R.id.characterNameTextView);
            characterImage = itemView.findViewById(R.id.characterImageView);
        }

        void bind(final Character character, final OnItemClickListener listener) {
            characterName.setText(character.getName());
            characterImage.setImageResource(character.getImageResId()); // Asigna la imagen del personaje
            itemView.setOnClickListener(v -> listener.onItemClick(character));
        }
    }
}