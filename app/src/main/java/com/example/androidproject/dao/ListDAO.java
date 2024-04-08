package com.example.androidproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.androidproject.entity.List;
import com.example.androidproject.sqlitedb.CreateDatabaseList;

public class ListDAO {
    SQLiteDatabase db;

    public ListDAO(Context context) {
        CreateDatabaseList createDatabase = new CreateDatabaseList(context);
        db = createDatabase.open();
    }

    public void addList(List list, boolean storeState) {
        try {
            delList();

            ContentValues values = new ContentValues();

            values.put(CreateDatabaseList.TB_LIST_LISTID, list.getListID());
            values.put(CreateDatabaseList.TB_LIST_NAME, list.getName());
            values.put("storeState", storeState);
            db.insert(CreateDatabaseList.TB_LIST, null, values);
        } catch (Exception e) {
            Log.i("WTF", e.toString());
        }
    }

    public void delList() {
        db.delete(CreateDatabaseList.TB_LIST, null, null);
    }

    public boolean checkExistList() {
        String query = "SELECT * FROM " + CreateDatabaseList.TB_LIST;
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() != 0;
    }

    public List getList() {
        String query = "SELECT * FROM " + CreateDatabaseList.TB_LIST;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            List list = new List();
            list.setListID(cursor.getInt(0));
            list.setName(cursor.getString(1));
            cursor.close();
            return list;
        } else {
            cursor.close();
            return null;
        }
    }

    public int getID() {
        String query = "SELECT listID FROM " + CreateDatabaseList.TB_LIST_LISTID;
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

    public String getName() {
        String query = "SELECT email FROM " + CreateDatabaseList.TB_LIST_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        } else {
            cursor.close();
            return "";
        }
    }
    public boolean getStoreState() {
        String query = "SELECT storeState FROM " + CreateDatabaseList.TB_LIST;
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
