package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.List;

public interface IFeedFragmentView {
    void showLoading();
    void hideLoading();
    void showFeedList(List<FeedItem> list);
    void onFeedClicked(FeedItem feedItem);
}
