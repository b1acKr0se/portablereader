package com.framgia.nguyenthanhhai.portablereader.data.local;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

abstract class DbContentProvider {
    SQLiteDatabase database;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;

    DbContentProvider(Context context) {
        this.mContext = context;
        mDatabaseHelper = DatabaseHelper.getDatabaseHelper(mContext);
        open();
    }

    public void open() throws SQLException {
        if (mDatabaseHelper == null)
            mDatabaseHelper = DatabaseHelper.getDatabaseHelper(mContext);
        database = mDatabaseHelper.getWritableDatabase();
    }
}