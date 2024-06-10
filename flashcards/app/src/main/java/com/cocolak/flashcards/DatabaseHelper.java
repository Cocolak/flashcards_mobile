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
    private  Context context;
    private static final String DATABASE_NAME = "FlashcardsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static String TABLE_NAME;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FRONT = "front";
    private static final String COLUMN_BACK = "back";
    public static final String INFO_TABLE_NAME = "info_table";
    public static final String INFO_COLUMN_ID = "id";
    public static final String INFO_COLUMN_NAME = "name";
    public static final String INFO_COLUMN_RELATED_TABLE = "related_table";

    public DatabaseHelper(@Nullable Context context, String table_name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        TABLE_NAME = table_name;
    }

    public DatabaseHelper(@Nullable Context context) {
        this(context, "empty");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INFO_TABLE_NAME);
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

        String createTable2 = "CREATE TABLE IF NOT EXISTS " + INFO_TABLE_NAME + " (" +
                INFO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INFO_COLUMN_NAME + " TEXT, " +
                INFO_COLUMN_RELATED_TABLE + " TEXT)";
        db.execSQL(createTable2);
    }

    public ArrayList<String> getDecksNames() {
        ArrayList<String> tableNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%' AND name NOT LIKE 'empty' AND name NOT LIKE 'info_table'"; // Old query
        String query = "SELECT i." + INFO_COLUMN_NAME + " FROM sqlite_master as m, " + INFO_TABLE_NAME + " as i WHERE type='table' AND m.name=i." + INFO_COLUMN_RELATED_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                tableNames.add(tableName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tableNames;
    }

    public void addDeck(String deck_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + INFO_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        String generated_deck_name;
        if (cursor.moveToFirst()) {
            int count_decks = cursor.getInt(0);
            generated_deck_name = "deck" + count_decks;
        } else {
            // TODO: DataBase Error
            return;
        }

        cursor.close();

        // Add table info
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INFO_COLUMN_NAME, deck_name);
        contentValues.put(INFO_COLUMN_RELATED_TABLE, generated_deck_name);
        long result = db.insert(INFO_TABLE_NAME, null, contentValues);

        // Create new table
        String createTable = "CREATE TABLE IF NOT EXISTS " + generated_deck_name + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FRONT + " TEXT, " +
                COLUMN_BACK + " TEXT)";
        db.execSQL(createTable);

    }

    public void removeDeck(String deck_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + deck_name);
        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
    }

    public List<List<String>> getDecksInfo() {
        List<List<String>> decksInfos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android_%' AND name NOT LIKE 'empty'"; // Old query
        String query = "SELECT i." + INFO_COLUMN_NAME + ", m.name FROM sqlite_master as m, " + INFO_TABLE_NAME + " as i WHERE type='table' AND m.name=i." + INFO_COLUMN_RELATED_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String fixedTableName = cursor.getString(0); // Normal table name
                String tableName = cursor.getString(1); // Generated table name
                String flashcardsNumber = Integer.toString(getFlashcardsNumber(tableName)); // Get number from db by coded/generated table name
                decksInfos.add(Arrays.asList(fixedTableName, flashcardsNumber)); // Pass preaty name and flashcards number
            } while (cursor.moveToNext());
        }

        cursor.close();
        return decksInfos;
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

    public String normal_deck_name_to_coded(String normal_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT m.name FROM sqlite_master as m, " + INFO_TABLE_NAME + " as i WHERE m.type='table' AND m.name=i." + INFO_COLUMN_RELATED_TABLE + " AND i." + INFO_COLUMN_NAME + "='" + normal_name + "';";
        Cursor cursor = db.rawQuery(query, null);

        String coded_name;
        if (cursor.moveToFirst()) {
            do {
                coded_name = cursor.getString(0);
            } while (cursor.moveToNext());
            return coded_name;
        }
        return "";
    }
}
