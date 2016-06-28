package com.framgia.nguyenthanhhai.portablereader.data.model;

import android.database.Cursor;

import com.framgia.nguyenthanhhai.portablereader.constant.Db;

import java.io.Serializable;
import java.util.List;

/**
 * Model for a feed item
 */
public class FeedItem implements Serializable {
    private int mId;
    private String mTitle;
    private String mDesc;
    private String mLink;
    private String mPubDate;
    private String mCategory;
    private String mAuthor;
    private String mImage;
    private String mSaveDate;
    private boolean mIsRead;
    private boolean mIsFavorited;

    public FeedItem() {

    }

    public FeedItem(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndexOrThrow(Db.FEED_ID));
        mTitle = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_TITLE));
        mDesc = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_DESC));
        mLink = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_LINK));
        mAuthor = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_AUTHOR));
        mPubDate = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_PUB_DATE));
        mCategory = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_CATEGORY));
        mImage = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_IMAGE));
        mSaveDate = cursor.getString(cursor.getColumnIndexOrThrow(Db.FEED_SAVE_DATE));
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDesc;
    }

    public void setDescription(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String mPubDate) {
        this.mPubDate = mPubDate;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setImage(String imageUrl) {
        this.mImage = imageUrl;
    }

    public String getImage() {
        return mImage;
    }

    public String getSaveDate() {
        return mSaveDate;
    }

    public void setSaveDate(String mSaveDate) {
        this.mSaveDate = mSaveDate;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public void setReadStatus(boolean read) {
        this.mIsRead = read;
    }

    public boolean isFavorited() {
        return mIsFavorited;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorited = favorite;
    }

    public static class FeedItemBuilder {
        private final FeedItem mFeedItem;

        public FeedItemBuilder() {
            mFeedItem = new FeedItem();
            mFeedItem.setReadStatus(false);
        }

        public FeedItemBuilder setId(int id) {
            mFeedItem.setId(id);
            return this;
        }

        public FeedItemBuilder setTitle(String title) {
            mFeedItem.setTitle(title);
            return this;
        }

        public FeedItemBuilder setDescription(String desc) {
            mFeedItem.setDescription(desc);
            return this;
        }

        public FeedItemBuilder setLink(String link) {
            mFeedItem.setLink(link);
            return this;
        }

        public FeedItemBuilder setPubDate(String date) {
            mFeedItem.setPubDate(date);
            return this;
        }

        public FeedItemBuilder setCategory(String category) {
            mFeedItem.setCategory(category);
            return this;
        }

        public FeedItemBuilder setAuthor(String author) {
            mFeedItem.setAuthor(author);
            return this;
        }

        public FeedItemBuilder setStatus(boolean read) {
            mFeedItem.setReadStatus(read);
            return this;
        }

        public FeedItemBuilder setImage(String image) {
            mFeedItem.setImage(image);
            return this;
        }

        public FeedItemBuilder setSaveDate(String date) {
            mFeedItem.setSaveDate(date);
            return this;
        }

        public FeedItem build() {
            return mFeedItem;
        }
    }
}
