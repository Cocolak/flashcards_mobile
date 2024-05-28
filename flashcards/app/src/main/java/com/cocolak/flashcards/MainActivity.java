package com.cocolak.flashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<DeckModel> deckModels;
    DecksAdapter adapter;

    RecyclerView decksList;
    Button addButton;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decksList = findViewById(R.id.decksList);
        addButton = findViewById(R.id.addButton);
        startButton = findViewById(R.id.startButton);

        loadData();

        adapter = new DecksAdapter(this, deckModels);
        decksList.setAdapter(adapter);
        decksList.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addActivityIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addActivityIntent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sessionActivityIntent = new Intent(MainActivity.this, SessionActivity.class);
                startActivity(sessionActivityIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadData();
        refreshData();
    }

    public void loadData() {
        deckModels = new ArrayList<>();
        List<List<String>> tablesNames = dbHelper.getTablesNames();

        for(int i = 0; i<tablesNames.size();i++) {
            deckModels.add(new DeckModel(tablesNames.get(i).get(0), tablesNames.get(i).get(1)));
        }
    }
    public void refreshData() {
        adapter.notifyDataSetChanged();
    }
}