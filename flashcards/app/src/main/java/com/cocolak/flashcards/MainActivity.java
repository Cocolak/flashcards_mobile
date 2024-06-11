package com.cocolak.flashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DecksAdapter.OnItemButtonClickListener {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<DeckModel> deckModels;
    DecksAdapter adapter;

    RecyclerView decksList;

    FloatingActionButton cardButton, deckButton;

    // Dialog Box Variables
    Dialog addDeckDialog;
    EditText deckNameText;
    Button cancelButton, confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Main Variables
        decksList = findViewById(R.id.decksList);
        cardButton = findViewById(R.id.newCardButton);
        deckButton = findViewById(R.id.newDeckButton);

        // Dialog Box Variables
        addDeckDialog = new Dialog(MainActivity.this);
        addDeckDialog.setContentView(R.layout.dialog_box_add_deck);
        addDeckDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addDeckDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_box_add_deck_bg));
        addDeckDialog.setCancelable(false);

        deckNameText = addDeckDialog.findViewById(R.id.deckNameText);
        cancelButton = addDeckDialog.findViewById(R.id.cancelButton);
        confirmButton = addDeckDialog.findViewById(R.id.confirmButton);

        // Loading data
        loadData();

        adapter = new DecksAdapter(this, deckModels, this);
        decksList.setAdapter(adapter);
        decksList.setLayoutManager(new LinearLayoutManager(this));

        // OnClickListeners
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addActivityIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addActivityIntent);
            }
        });

        deckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeckDialog.show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeckDialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = deckNameText.getText().toString().trim();
                dbHelper.addDeck(deckName);
                Toast.makeText(MainActivity.this, "Deck Added Successfully", Toast.LENGTH_SHORT).show();
                refreshData();
                addDeckDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
    @Override
    public void onItemButtonClick(String item) {
        // TODO: If alert dialog yes then remove deck else do nothing
        String deckName = dbHelper.normal_deck_name_to_coded(item);
        dbHelper.removeDeck(deckName);
        refreshData();
    }

    public void loadData() {
        deckModels = new ArrayList<>();
        List<List<String>> tablesNames = dbHelper.getDecksInfo();
        for(int i = 0; i<tablesNames.size();i++) {
            deckModels.add(new DeckModel(tablesNames.get(i).get(0), tablesNames.get(i).get(1)));
        }
    }
    public void refreshData() {
        deckModels.clear();
        List<List<String>> tablesNames = dbHelper.getDecksInfo();
        for(int i = 0; i<tablesNames.size();i++) {
            deckModels.add(new DeckModel(tablesNames.get(i).get(0), tablesNames.get(i).get(1)));
        }
        adapter.notifyDataSetChanged();
    }
}