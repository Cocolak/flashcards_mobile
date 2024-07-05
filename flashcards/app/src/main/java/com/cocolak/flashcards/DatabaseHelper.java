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
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private  Context context;
    private static final String DATABASE_NAME = "FlashcardsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static String TABLE_NAME;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FRONT = "front";
    private static final String COLUMN_BACK = "back";
    public static final String COLUMN_LVL = "lvl";
    public static final String COLUMN_DATE = "date";

    // Info Table
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
                COLUMN_BACK + " TEXT, " +
                COLUMN_LVL + " TEXT, " +
                COLUMN_DATE + " TEXT)";
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
                COLUMN_BACK + " TEXT," +
                COLUMN_LVL + " TEXT, " +
                COLUMN_DATE + " TEXT)";
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
                String leftFlashcardsNumber = Integer.toString(getLeftFlashcardsNumber(tableName)); // Get left flashcards number from db by coded/generated table name
                decksInfos.add(Arrays.asList(fixedTableName, flashcardsNumber, leftFlashcardsNumber)); // Pass pretty name, flashcards number and left flashcards number
            } while (cursor.moveToNext());
        }

        cursor.close();
        return decksInfos;
    }

    public void addFlashcard(String front, String back) {
        Date d = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FRONT, front);
        contentValues.put(COLUMN_BACK, back);
        contentValues.put(COLUMN_LVL, "0");
        contentValues.put(COLUMN_DATE, Long.toString(d.getTime()));

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result != -1) {
            Toast.makeText(context, "Flashcard added successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Adding flashcard failed.", Toast.LENGTH_SHORT).show();
        }

    }
    public int getFlashcardsNumber(String TABLE_NAME) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_NAME; // Take all flashcards from deck
        Cursor cursor = db.rawQuery(query, null);

        int flashcardsNumber = cursor.getCount(); // Count these flashcards

        cursor.close();
        return flashcardsNumber;
    }

    public int getLeftFlashcardsNumber(String TABLE_NAME) {
        Date d = new Date();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "<='" +d.getTime() + "';"; // Take all left flashcards for now
        Cursor cursor = db.rawQuery(query, null);

        int leftFlashcardsNumber = cursor.getCount(); // Count these flashcards

        cursor.close();
        return leftFlashcardsNumber;
    }

    public ArrayList<ArrayList<String>> getTodaySession() {
        Date d = new Date();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT front, back FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "<='" +d.getTime() + "' ORDER BY " + COLUMN_DATE + " ASC;";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ArrayList<String>> todaySession = new ArrayList<>();
        while(cursor.moveToNext()) {
            ArrayList<String> listRow = new ArrayList<>();
            listRow.add(cursor.getString(0));
            listRow.add(cursor.getString(1));
            todaySession.add(listRow);
        }

        cursor.close();
        return todaySession;
    }

    public void setupFlashcard(Boolean isRight, String flashcard_front_name) {
        Date d = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> flashcardInfo = getFlashcardInfo(flashcard_front_name);
        int actualLvl = Integer.parseInt(flashcardInfo.get(3));

        // Creating delayList with recursion delays
        // 5min, 30min, 6h, 1d, 4d, 2w, 1m, 3m, 6m
        List<Long> delayList = new ArrayList<Long>();
        delayList.add((long) 0); // 0ms
        delayList.add((long) 5 * 60 * 1000); // 5min
        delayList.add((long) 30 * 60 * 1000); // 30min
        delayList.add((long) 6 * 60 * 60 * 1000); // 6h
        delayList.add((long) 24 * 60 * 60 * 1000); // 1d
        delayList.add((long) 4 * 24 * 60 * 60 * 1000); // 4d
        delayList.add((long) 14 * 24 * 60 * 60 * 1000); // 2w (14d)
        delayList.add((long) 30 * 24 * 60 * 60 * 1000); // 1m
        delayList.add((long) 3 * 30 * 24 * 60 * 60 * 1000); // 3m
        delayList.add((long) 6 * 30 * 24 * 60 * 60 * 1000); // 6m

        // Adding new lvl and date
        String newLvl;
        String newDate;
        long timeDelay;
        if (isRight) {
            newLvl = Integer.toString(actualLvl + 1);
            timeDelay = delayList.get(Integer.parseInt(newLvl));
            newDate = Long.toString(d.getTime() + timeDelay);
        } else {
            if (actualLvl <= 2) {
                newLvl = "0";
            } else {
                newLvl = Integer.toString(actualLvl - 2);
            }
            timeDelay = delayList.get(Integer.parseInt(newLvl));
            newDate = Long.toString(d.getTime() + timeDelay);
        }

        // Updates values in database
        ContentValues values = new ContentValues();
        values.put(COLUMN_LVL, newLvl);
        values.put(COLUMN_DATE, newDate);

        db.update(TABLE_NAME, values, COLUMN_FRONT + "=?", new String[]{flashcard_front_name});
    }

    public ArrayList<String>  getFlashcardInfo(String flashcard_front_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FRONT + "='" + flashcard_front_name + "';";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> flashcardInfo = new ArrayList<>();
        if(cursor.moveToNext()) {
            for (int i=0; i < 10 ; i++) {
                try {
                    flashcardInfo.add(cursor.getString(i));
                } finally {
                    continue;
                }
            }
        }
        cursor.close();
        return flashcardInfo;
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
