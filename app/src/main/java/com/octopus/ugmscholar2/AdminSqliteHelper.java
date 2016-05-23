package com.octopus.ugmscholar2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by octopus on 23/05/16.
 */
public class AdminSqliteHelper extends SQLiteOpenHelper {

    public AdminSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bookmark " +
                "(id IINTEGER PRIMARY KEY, title TEXT, imgURL TEXT, tgl TEXT, author TEXT, directURL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABEL IF EXISTS bookmark");
        db.execSQL("CREATE TABLE bookmark " +
                "(id IINTEGER PRIMARY KEY, title TEXT, imgURL TEXT, tgl TEXT, author TEXT, directURL TEXT)");
    }
}
