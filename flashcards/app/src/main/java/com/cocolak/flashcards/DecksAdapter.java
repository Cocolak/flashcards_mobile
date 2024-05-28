package com.cocolak.flashcards;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DecksAdapter extends RecyclerView.Adapter<DecksAdapter.MyViewHolder> {
    Context context;
    ArrayList<DeckModel> deckModels;

    public DecksAdapter(Context context, ArrayList<DeckModel> deckModels) {
        this.context = context;
        this.deckModels = deckModels;
    }

    @NonNull
    @Override
    public DecksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_deck, parent, false);
        return new DecksAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DecksAdapter.MyViewHolder holder, int position) {
        holder.nameTextView.setText(deckModels.get(position).getDeckName());
        holder.countTextView.setText(deckModels.get(position).getDeckNumber());
    }

    @Override
    public int getItemCount() {
        return deckModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, countTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
        }
    }
}
