package com.framgia.nguyenthanhhai.portablereader.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.framgia.nguyenthanhhai.portablereader.constant.Db;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mDatabaseInstance;

    public DatabaseHelper(Context context) {
        super(context, Db.DATABASE_NAME, null, Db.DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getDatabaseHelper(Context context) {
        if (mDatabaseInstance == null)
            mDatabaseInstance = new DatabaseHelper(context);
        return mDatabaseInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Db.CREATE_TABLE_FEED);
        db.execSQL(Db.CREATE_TABLE_READ);
        db.execSQL(Db.CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Db.TABLE_FEED);
        db.execSQL("DROP TABLE IF EXISTS " + Db.TABLE_READ);
        db.execSQL("DROP TABLE IF EXISTS " + Db.TABLE_FAVORITE);
        onCreate(db);
    }
}
