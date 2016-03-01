package com.framgia.nguyenthanhhai.portablereader.data.model;

import java.util.List;

/**
 * Model for a feed item
 */
public class FeedItem {
    private int mId;
    private String mTitle;
    private String mDesc;
    private String mLink;
    private String mPubDate;
    private List<Category> mCategory;
    private String mAuthor;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmPubDate() {
        return mPubDate;
    }

    public void setmPubDate(String mPubDate) {
        this.mPubDate = mPubDate;
    }

    public List<Category> getmCategory() {
        return mCategory;
    }

    public void setmCategory(List<Category> mCategory) {
        this.mCategory = mCategory;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public class FeedItemBuilder {
        private final FeedItem mFeedItem;

        public FeedItemBuilder() {
            mFeedItem = new FeedItem();
        }

        public FeedItemBuilder setId(int id) {
            mFeedItem.setmId(id);
            return this;
        }

        public FeedItemBuilder setTitle(String title) {
            mFeedItem.setmTitle(title);
            return this;
        }

        public FeedItemBuilder setDescription(String desc) {
            mFeedItem.setmDesc(desc);
            return this;
        }

        public FeedItemBuilder setLink(String link) {
            mFeedItem.setmLink(link);
            return this;
        }

        public FeedItemBuilder setPubDate(String date) {
            mFeedItem.setmPubDate(date);
            return this;
        }

        public FeedItemBuilder setCategory(List<Category> category) {
            mFeedItem.setmCategory(category);
            return this;
        }

        public FeedItemBuilder setAuthor(String author) {
            mFeedItem.setmAuthor(author);
            return this;
        }

        public FeedItem build() {
            return mFeedItem;
        }
    }
}
