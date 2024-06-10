package com.cocolak.flashcards;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DecksAdapter extends RecyclerView.Adapter<DecksAdapter.MyViewHolder> {
    Context context;
    ArrayList<DeckModel> deckModels;
    private OnItemButtonClickListener listener;

    public interface OnItemButtonClickListener {
        void onItemButtonClick(String item);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, countTextView;
        Button removeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public DecksAdapter(Context context, ArrayList<DeckModel> deckModels, OnItemButtonClickListener listener) {
        this.context = context;
        this.deckModels = deckModels;
        this.listener = listener;
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

        String item = deckModels.get(position).getDeckName();

        holder.itemView.setOnClickListener(v -> {
            String deck_name = holder.nameTextView.getText().toString();
            Intent intent = new Intent(context, SessionActivity.class);
            intent.putExtra("deck_name", deck_name);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(holder.removeButton.getVisibility() == View.GONE) {
                    AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(500);
                    holder.removeButton.setVisibility(View.VISIBLE);
                    holder.removeButton.startAnimation(animation);
                } else {
                    holder.removeButton.setVisibility(View.GONE);
                }
                return true;
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemButtonClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deckModels.size();
    }
}
