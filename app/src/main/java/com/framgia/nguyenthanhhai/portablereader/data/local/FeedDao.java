package com.framgia.nguyenthanhhai.portablereader.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.framgia.nguyenthanhhai.portablereader.constant.Db;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.framgia.nguyenthanhhai.portablereader.util.DateDifferenceConverter;

import java.util.ArrayList;
import java.util.List;

public class FeedDao extends DbContentProvider {

    public FeedDao(Context context) {
        super(context);
    }

    public void insertFeeds(List<FeedItem> list, String category) {
        database.delete(Db.TABLE_FEED,  Db.FEED_CATEGORY + " = ?",  new String[]{category});
        for (FeedItem item : list) {
            insertFeed(item);
        }
    }

    public boolean insertFeed(FeedItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db.FEED_TITLE, item.getTitle());
        contentValues.put(Db.FEED_DESC, item.getDescription());
        contentValues.put(Db.FEED_LINK, item.getLink());
        contentValues.put(Db.FEED_PUB_DATE, item.getPubDate());
        contentValues.put(Db.FEED_AUTHOR, item.getAuthor());
        contentValues.put(Db.FEED_IMAGE, item.getImage());
        contentValues.put(Db.FEED_CATEGORY, item.getCategory());
        try {
            database.insertOrThrow(Db.TABLE_FEED, null, contentValues);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isInDb(FeedItem item) {
        String selection = Db.FEED_TITLE + " = ? AND " + Db.FEED_AUTHOR + " = ?";
        String[] selectionArgs = new String[]{item.getTitle(), item.getAuthor()};
        Cursor cursor = database.query(Db.TABLE_FEED,
                null, selection, selectionArgs, null, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    public void insertRead(FeedItem item) {
        if (!isRead(item)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Db.READ_TITLE, item.getTitle());
            try {
                database.insertOrThrow(Db.TABLE_READ, null, contentValues);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertFavorite(FeedItem item) {
        if (!isFavorited(item)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Db.FAVORITE_TITLE, item.getTitle());
            try {
                database.insertOrThrow(Db.TABLE_FAVORITE, null, contentValues);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFavorited(FeedItem item) {
        String selection = Db.FAVORITE_TITLE + " = ?";
        String[] selectionArgs = new String[]{item.getTitle()};
        Cursor cursor = database.query(Db.TABLE_FAVORITE,
                null, selection, selectionArgs, null, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    public boolean isRead(FeedItem item) {
        String selection = Db.READ_TITLE + " = ?";
        String[] selectionArgs = new String[]{item.getTitle()};
        Cursor cursor = database.query(Db.TABLE_READ,
                null, selection, selectionArgs, null, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    public List<FeedItem> getFeedList(String category) {
        String selection = Db.FEED_CATEGORY + " = ?";
        String[] selectionArgs = new String[]{category};
        Cursor cursor = database.query(Db.TABLE_FEED,
                null, selection, selectionArgs, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<FeedItem> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                FeedItem feedItem = new FeedItem(cursor);
                feedItem.setReadStatus(isRead(feedItem));
                feedItem.setFavorite(isFavorited(feedItem));
                list.add(feedItem);
            }
            cursor.close();
            return list;
        }
        cursor.close();
        return null;
    }

    public void removeOutdatedFeed() {
        Cursor cursor = database.query(Db.TABLE_FEED,
                null, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                FeedItem feedItem = new FeedItem(cursor);
                isOutdated(feedItem);
            }
        }
    }

    public void isOutdated(FeedItem feedItem) {
        if (DateDifferenceConverter.isOutdated(feedItem.getPubDate())) {
            remove(feedItem);
        }
    }

    public void remove(FeedItem feedItem) {
        database.delete(Db.TABLE_FEED, Db.FEED_ID + " = ?", new String[]{String.valueOf(feedItem.getId())});
    }
}
