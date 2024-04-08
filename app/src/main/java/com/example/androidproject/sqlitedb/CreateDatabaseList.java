package com.example.androidproject.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabaseList extends SQLiteOpenHelper {
    public static String TB_LIST = "List";
    public static String TB_LIST_LISTID = "listID";
    public static String TB_LIST_NAME = "name";
    public CreateDatabaseList(@Nullable Context context) {
        super(context, "Mob2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbList = "CREATE TABLE " + TB_LIST + " (" +
                TB_LIST_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_LIST_NAME + " TEXT," +
                "storeState BOOLEAN)";
        db.execSQL(tbList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }
}
