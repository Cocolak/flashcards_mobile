package com.cocolak.flashcards;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<String> decksList;
    Spinner decksSpinner;
    EditText frontEditText,  backEditText;
    Button backButton, confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        decksList = dbHelper.getDecksNames();
        decksSpinner = findViewById(R.id.decksSpinner);
        frontEditText = findViewById(R.id.frontEditText);
        backEditText = findViewById(R.id.backEditText);
        confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.backButton);

        createSpinner();

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

                dbHelper.TABLE_NAME = dbHelper.normal_deck_name_to_coded(selected_deck);
                dbHelper.addFlashcard(frontText, backText);
                frontEditText.getText().clear();
                backEditText.getText().clear();
            }
        });
    }

    public void createSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_spinner_item, decksList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        decksSpinner.setAdapter(adapter);
    }
}