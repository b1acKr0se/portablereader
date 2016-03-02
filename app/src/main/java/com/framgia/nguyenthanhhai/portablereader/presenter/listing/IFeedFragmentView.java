package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.view.View;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.List;

public interface IFeedFragmentView {
    void showLoading();
    void hideLoading();
    void showFeedList(List<FeedItem> list);
    void onFeedClicked(View view, FeedItem feedItem);
}
