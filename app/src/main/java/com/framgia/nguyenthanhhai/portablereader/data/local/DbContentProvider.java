package com.framgia.nguyenthanhhai.portablereader.data.local;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbContentProvider {
    public SQLiteDatabase database;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;

    public DbContentProvider(Context context) {
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