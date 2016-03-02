package com.framgia.nguyenthanhhai.portablereader.data.model;

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
    private List<Category> mCategory;
    private String mAuthor;
    private String mImage;
    private boolean mIsRead;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
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

    public List<Category> getCategory() {
        return mCategory;
    }

    public void setCategory(List<Category> mCategory) {
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

    public boolean isRead() {
        return mIsRead;
    }

    public void setReadStatus(boolean read) {
        this.mIsRead = read;
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

        public FeedItemBuilder setCategory(List<Category> category) {
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

        public FeedItem build() {
            return mFeedItem;
        }
    }
}
