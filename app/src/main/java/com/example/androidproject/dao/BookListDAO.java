package com.example.androidproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.androidproject.entity.BookList;
import com.example.androidproject.sqlitedb.CreateDatabaseBookList;

public class BookListDAO {
    SQLiteDatabase db;

    public BookListDAO(Context context) {
        CreateDatabaseBookList createDatabase = new CreateDatabaseBookList(context);
        db = createDatabase.open();
    }

    public void addBookList(BookList bookList, boolean storeState) {
        try {
            delBookList();
            ContentValues values = new ContentValues();
            values.put(CreateDatabaseBookList.TB_BOOKLIST_LISTID, bookList.getListID());
            values.put(CreateDatabaseBookList.TB_BOOKLIST_BOOKID, bookList.getBookID());
            values.put("storeState", storeState);
            db.insert(CreateDatabaseBookList.TB_BOOKLIST, null, values);
        } catch (Exception e) {
            Log.i("WTF", e.toString());
        }
    }

    public void delBookList() {
        db.delete(CreateDatabaseBookList.TB_BOOKLIST, null, null);
    }

    public boolean checkExistBookList() {
        String query = "SELECT * FROM " + CreateDatabaseBookList.TB_BOOKLIST;
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() != 0;
    }

    public BookList getBookList() {
        String query = "SELECT * FROM " + CreateDatabaseBookList.TB_BOOKLIST;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            BookList bookList = new BookList();
            bookList.setListID(cursor.getInt(0));
            bookList.setBookID(cursor.getInt(1));
            cursor.close();
            return bookList;
        } else {
            cursor.close();
            return null;
        }
    }

    public int getBookID() {
        String query = "SELECT bookID FROM " + CreateDatabaseBookList.TB_BOOKLIST_BOOKID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            return 0;
        }
    }
    public int getListID() {
        String query = "SELECT listID FROM " + CreateDatabaseBookList.TB_BOOKLIST_LISTID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            return 0;
        }
    }
    public boolean getStoreState() {
        String query = "SELECT storeState FROM " + CreateDatabaseBookList.TB_BOOKLIST;
        Cursor cursor = db.rawQuery(query, null);

        boolean flag;
        if (cursor.moveToFirst()) {
            flag = (cursor.getInt(0) == 1);
            cursor.close();
        } else {
            cursor.close();
            return false;
        }
        return flag;
    }

}
