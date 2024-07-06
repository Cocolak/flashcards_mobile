package com.cocolak.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class SessionActivity extends AppCompatActivity {
    String deck_name;
    DatabaseHelper dbHelper;
    ArrayList<ArrayList<String>> todaySession;
    int i = 0;
    TextView deckTitle, leftFlashcardsNumber, frontText, backText;
    CardView frontCard, backCard;
    Button backButton, optionsButton, dknowButton, knowButton, showButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // Get dbHelper related to specific deck
        Intent intent = getIntent();
        deck_name = intent.getStringExtra("deck_name");
        dbHelper = new DatabaseHelper(this);
        dbHelper.TABLE_NAME = dbHelper.normal_deck_name_to_coded(deck_name); // Change TABLE_NAME from normal to coded

        todaySession = dbHelper.getTodaySession();
        frontCard = findViewById(R.id.frontCard);
        frontText = findViewById(R.id.frontText);
        deckTitle = findViewById(R.id.deckTitle);
        leftFlashcardsNumber = findViewById(R.id.leftFlashcardsNumber);
        backCard = findViewById(R.id.backCard);
        backText = findViewById(R.id.backText);
        backButton = findViewById(R.id.backButton);
        optionsButton = findViewById(R.id.optionsButton);
        showButton = findViewById(R.id.showButton);
        dknowButton = findViewById(R.id.dknowButton);
        knowButton = findViewById(R.id.knowButton);

        deckTitle.setText(deck_name);
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
                setNewData(false);
                loadFlashcard(); // next flashcard
            }
        });

        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewData(true);
                loadFlashcard(); // next flashcard
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions(v);
            }
        });

    }

    public void loadFlashcard() {
        if (i < todaySession.size()) {
            String leftCardsText = "Left: " + dbHelper.getLeftFlashcardsNumber(dbHelper.normal_deck_name_to_coded(deck_name));
            leftFlashcardsNumber.setText(leftCardsText);
            dknowButton.setVisibility(View.GONE);
            knowButton.setVisibility(View.GONE);
            backCard.setVisibility(View.INVISIBLE);
            showButton.setVisibility(View.VISIBLE);

            String frontTextText = todaySession.get(i).get(0);
            String backTextText = todaySession.get(i).get(1);

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
        backCard.setVisibility(View.VISIBLE);
        showButton.setVisibility(View.GONE);
    }

    public void setNewData(Boolean isRight) {
        String flashcard_name = frontText.getText().toString();
        dbHelper.setupFlashcard(isRight, flashcard_name);
    }

    private void showOptions(View v) {
        PopupMenu optionsMenu = new PopupMenu(SessionActivity.this, v);
        optionsMenu.getMenuInflater().inflate(R.menu.menu_add, optionsMenu.getMenu());

        optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.nav_remove) {
                    // TODO: You sure?
                    String flashcard_front = frontText.getText().toString();
                    dbHelper.removeFlashcard(flashcard_front);
                    loadFlashcard();
                }
                return true;
            }
        });

        optionsMenu.show();
    }
}