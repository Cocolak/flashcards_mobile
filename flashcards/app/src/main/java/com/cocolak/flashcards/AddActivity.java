package com.cocolak.flashcards;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    EditText frontEditText;
    EditText backEditText;
    Button cancelButton;
    Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        frontEditText = findViewById(R.id.frontEditText);
        backEditText = findViewById(R.id.backEditText);
        cancelButton = findViewById(R.id.cancelButton);
        confirmButton = findViewById(R.id.confirmButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
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

                dbHelper.addFlashcard(frontText, backText);
            }
        });
    }
}