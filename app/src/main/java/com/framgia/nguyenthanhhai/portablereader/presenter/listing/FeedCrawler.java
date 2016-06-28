package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;

import com.framgia.nguyenthanhhai.portablereader.constant.Api;
import com.framgia.nguyenthanhhai.portablereader.constant.Category;
import com.framgia.nguyenthanhhai.portablereader.data.local.FeedDao;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

class FeedCrawler implements IFeedCrawler {
    private final OkHttpClient mOkhttpClient = new OkHttpClient();
    private String mUrl;
    private FeedDao mFeedDao;
    private String mCategory;

    FeedCrawler(Context context, String url) {
        this.mUrl = url;
        mFeedDao = new FeedDao(context);
    }

    @Override
    public Observable<List<FeedItem>> getFeedList() {
        return Observable.concat(disk(), network())
                .takeFirst(new Func1<List<FeedItem>, Boolean>() {
                    @Override public Boolean call(List<FeedItem> feedItems) {
                        return feedItems != null && !isOutdated(feedItems);
                    }
                });
    }

    private List<FeedItem> makeNetworkRequest() throws IOException, XmlPullParserException {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        Response response = mOkhttpClient.newCall(request).execute();
        String xml = response.body().string();
        List<FeedItem> list = XmlParser.parse(xml);
        mCategory = getCategoryFromUrl(mUrl);
        for (FeedItem item : list) {
            item.setCategory(mCategory);
            item.setReadStatus(mFeedDao.isRead(item));
        }
        return list;
    }

    private List<FeedItem> getCacheData() {
        return mFeedDao.getFeedList(getCategoryFromUrl(mUrl));
    }


    private Observable<List<FeedItem>> disk() {
        return Observable.create(new Observable.OnSubscribe<List<FeedItem>>() {
            @Override public void call(Subscriber<? super List<FeedItem>> subscriber) {
                subscriber.onNext(getCacheData());
                subscriber.onCompleted();
            }
        });
    }

    private Observable<List<FeedItem>> network() {
        Observable<List<FeedItem>> network = Observable.create(new Observable.OnSubscribe<List<FeedItem>>() {
            @Override public void call(Subscriber<? super List<FeedItem>> subscriber) {
                try {
                    List<FeedItem> list = makeNetworkRequest();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return network.doOnNext(new Action1<List<FeedItem>>() {
            @Override public void call(List<FeedItem> feedItems) {
                saveToDisk(feedItems);
            }
        });
    }

    private boolean isOutdated(List<FeedItem> list) {
        return list == null || list.size() == 0 || mFeedDao.isOutdated(list);
    }

    private void saveToDisk(List<FeedItem> list) {
        if (list != null && !list.isEmpty()) {
            mFeedDao.insertFeeds(list, mCategory);
        }
    }

    private String getCategoryFromUrl(String url) {
        String category = "";
        switch (url) {
            case Api.SCIENCE_AND_TECHNOLOGY:
                category = Category.SCIENCE_AND_TECHNOLOGY;
                break;
            case Api.ECONOMY:
                category = Category.ECONOMY;
                break;
            case Api.HEALTH:
                category = Category.HEALTH;
                break;
            case Api.ART_AND_ENTERTAINMENT:
                category = Category.ART_AND_ENTERTAINMENT;
                break;
        }
        return category;
    }
}
