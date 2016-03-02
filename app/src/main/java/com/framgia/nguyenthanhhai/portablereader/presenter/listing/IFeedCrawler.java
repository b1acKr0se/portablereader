package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.List;

import rx.Observable;

public interface IFeedCrawler {
    Observable<List<FeedItem>> getFeedList();
}
