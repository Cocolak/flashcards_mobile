package com.cocolak.flashcards;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<String> decksList, typesList;
    Spinner decksSpinner, typesSpinner;
    EditText frontEditText,  backEditText;
    Button backButton, confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Create lists
        decksList = dbHelper.getDecksNames();
        typesList = new ArrayList<>();
        typesList.add("Basic");
        typesList.add("Basic (and reversed card)");

        decksSpinner = findViewById(R.id.decksSpinner);
        typesSpinner = findViewById(R.id.typesSpinner);
        frontEditText = findViewById(R.id.frontEditText);
        backEditText = findViewById(R.id.backEditText);
        confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.backButton);

        createDecksSpinner();
        createTypesSpinner();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frontText = frontEditText.getText().toString().trim();
                String backText = backEditText.getText().toString().trim();
                String selected_deck = decksSpinner.getSelectedItem().toString();
                long selected_type = typesSpinner.getSelectedItemId();

                dbHelper.TABLE_NAME = dbHelper.normal_deck_name_to_coded(selected_deck);
                dbHelper.addFlashcard(frontText, backText, selected_type);
                frontEditText.getText().clear();
                backEditText.getText().clear();
            }
        });
    }

    public void createDecksSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_spinner_item, decksList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        decksSpinner.setAdapter(adapter);
    }

    public void createTypesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_spinner_item, typesList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        typesSpinner.setAdapter(adapter);
    }
}