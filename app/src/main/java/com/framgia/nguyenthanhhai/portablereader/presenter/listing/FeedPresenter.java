package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;
import android.os.Handler;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class FeedPresenter implements IFeedPresenter {
    private IFeedFragmentView mFeedFragmentView;
    private FeedCrawler mFeedCrawler;

    public FeedPresenter(Context context, IFeedFragmentView feedFragmentView, String url) {
        this.mFeedCrawler = new FeedCrawler(context, url);
        this.mFeedFragmentView = feedFragmentView;
    }

    @Override
    public Subscription displayFeed() {
        return mFeedCrawler.getFeedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mFeedFragmentView.showLoading();
                    }
                })
                .subscribe(new Subscriber<List<FeedItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFeedFragmentView.hideLoading();
                    }

                    @Override
                    public void onNext(final List<FeedItem> feedItems) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mFeedFragmentView.hideLoading();
                                mFeedFragmentView.showFeedList(feedItems);
                            }
                        }, 200);
                    }
                });
    }
}
