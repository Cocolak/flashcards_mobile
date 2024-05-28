package com.cocolak.flashcards;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SessionActivity extends AppCompatActivity {
    String table_tag;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<ArrayList<String>> randomSession;
    int i = 0;
    TextView frontText;
    TextView backText;
    Button dknowButton;
    Button knowButton;
    Button showButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);


        randomSession = dbHelper.getRandomSession();
        frontText = findViewById(R.id.fronText);
        backText = findViewById(R.id.backText);
        showButton = findViewById(R.id.showButton);
        dknowButton = findViewById(R.id.dknowButton);
        knowButton = findViewById(R.id.knowButton);
        backButton = findViewById(R.id.backButton);

        loadFlashcard();

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });

        dknowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlashcard();
            }
        });

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlashcard();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadFlashcard() {
        if (i < randomSession.size()) {
            dknowButton.setVisibility(View.INVISIBLE);
            knowButton.setVisibility(View.INVISIBLE);
            backText.setVisibility(View.INVISIBLE);
            showButton.setVisibility(View.VISIBLE);

            String frontTextText = "Front: " + randomSession.get(i).get(0);
            String backTextText = "Back: " + randomSession.get(i).get(1);

            frontText.setText(frontTextText);
            backText.setText(backTextText);
            i++;
        } else {
            Toast.makeText(SessionActivity.this, "You completed session.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void showAnswer() {
        dknowButton.setVisibility(View.VISIBLE);
        knowButton.setVisibility(View.VISIBLE);
        backText.setVisibility(View.VISIBLE);
        showButton.setVisibility(View.INVISIBLE);
    }

}