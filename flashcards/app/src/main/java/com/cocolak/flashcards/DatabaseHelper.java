package com.cocolak.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "FlashcardsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_third_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FRONT = "front";
    private static final String COLUMN_BACK = "back";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FRONT + " TEXT, " +
                COLUMN_BACK + " TEXT)";
        db.execSQL(createTable);
    }

    public List<List<String>> getTablesNames() {
        List<List<String>> tableNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                String flashcardsNumber = Integer.toString(getFlashcardsNumber(tableName));
                tableNames.add(Arrays.asList(tableName, flashcardsNumber));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tableNames;
    }

    public void addFlashcard(String front, String back) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FRONT, front);
        contentValues.put(COLUMN_BACK, back);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result != -1) {
            Toast.makeText(context, "Flashcard added successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Adding flashcard failed.", Toast.LENGTH_SHORT).show();
        }

    }
    public int getFlashcardsNumber(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int flashcardsNumber = cursor.getCount();
        cursor.close();

        return flashcardsNumber;
    }

    public ArrayList<ArrayList<String>> getRandomSession() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT front, back FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ArrayList<String>> randomSession = new ArrayList<>();
        while(cursor.moveToNext()) {
            ArrayList<String> listRow = new ArrayList<>();
            listRow.add(cursor.getString(0));
            listRow.add(cursor.getString(1));
            randomSession.add(listRow);
        }

        // Shuffle Array
        Collections.shuffle(randomSession, new Random(System.nanoTime()));

        cursor.close();
        return randomSession;
    }
}
