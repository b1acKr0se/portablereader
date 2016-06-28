package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

class FeedPresenter implements IFeedPresenter {
    private IFeedFragmentView mFeedFragmentView;
    private FeedCrawler mFeedCrawler;

    FeedPresenter(Context context, IFeedFragmentView feedFragmentView, String url) {
        this.mFeedCrawler = new FeedCrawler(context, url);
        this.mFeedFragmentView = feedFragmentView;
    }

    @Override
    public Subscription displayFeed() {
        return mFeedCrawler.getFeedList()
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mFeedFragmentView.showLoading();
                    }
                })
                .subscribe(new Subscriber<List<FeedItem>>() {
                    @Override public void onCompleted() {
                        mFeedFragmentView.hideLoading();
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        mFeedFragmentView.hideLoading();
                    }

                    @Override public void onNext(final List<FeedItem> feedItems) {
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
