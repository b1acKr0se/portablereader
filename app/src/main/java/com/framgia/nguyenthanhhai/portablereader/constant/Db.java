package com.framgia.nguyenthanhhai.portablereader.constant;

public class Db {
    public static final String DATABASE_NAME = "rss.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_FEED = "Feed";
    public static final String TABLE_READ = "Read";
    public static final String TABLE_FAVORITE = "Favorite";
    //table feed columns
    public static final String FEED_ID = "_id";
    public static final String FEED_TITLE = "title";
    public static final String FEED_DESC = "description";
    public static final String FEED_LINK = "link";
    public static final String FEED_PUB_DATE = "pub_date";
    public static final String FEED_CATEGORY = "category";
    public static final String FEED_AUTHOR = "author";
    public static final String FEED_IMAGE = "image";
    public static final String FEED_SAVE_DATE = "save_date";

    //table read columns
    public static final String READ_ID = "_id";
    public static final String READ_TITLE = "title";

    //table favorite columns
    public static final String FAVORITE_ID = "_id";
    public static final String FAVORITE_TITLE = "title";

    public static final String CREATE_TABLE_FEED = "CREATE TABLE " + TABLE_FEED
            + "(" + FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FEED_TITLE + " TEXT, " + FEED_DESC + " TEXT, " + FEED_LINK + " TEXT, "
            + FEED_PUB_DATE + " TEXT, " + FEED_CATEGORY + " TEXT, " + FEED_SAVE_DATE + " TEXT, "
            + FEED_AUTHOR + " TEXT, " + FEED_IMAGE + " TEXT)";
    public static final String CREATE_TABLE_READ = "CREATE TABLE " + TABLE_READ
            + "(" + READ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + READ_TITLE + " TEXT)";
    public static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_FAVORITE
            + "(" + FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FAVORITE_TITLE + " TEXT)";

}
