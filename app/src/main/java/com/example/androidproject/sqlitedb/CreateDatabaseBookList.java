package com.example.androidproject.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabaseBookList extends SQLiteOpenHelper {
    public static String TB_BOOKLIST = "BookList";
    public static String TB_BOOKLIST_LISTID = "listID";
    public static String TB_BOOKLIST_BOOKID = "bookID";
    public CreateDatabaseBookList(@Nullable Context context) {
        super(context, "Mob2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbList = "CREATE TABLE " + TB_BOOKLIST + " (" +
                TB_BOOKLIST_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_BOOKLIST_BOOKID + " INTEGER," +
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
