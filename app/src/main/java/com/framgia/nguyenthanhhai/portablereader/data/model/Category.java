package com.framgia.nguyenthanhhai.portablereader.data.model;

public class Category {
    private int mId;
    private String mName;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public class CategoryBuilder {
        private Category mCategory;

        public CategoryBuilder() {
            mCategory = new Category();
        }

        public CategoryBuilder id(int id) {
            mCategory.setmId(id);
            return this;
        }

        public CategoryBuilder name(String name) {
            mCategory.setmName(name);
            return this;
        }

        public Category build() {
            return mCategory;
        }
    }
}
