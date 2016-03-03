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
import rx.functions.Func0;

public class FeedCrawler implements IFeedCrawler {
    private final OkHttpClient mOkhttpClient = new OkHttpClient();
    private String mUrl;
    private Context mContext;

    public FeedCrawler(Context context, String url) {
        this.mUrl = url;
        mContext = context;
    }

    @Override
    public Observable<List<FeedItem>> getFeedList() {
        return Observable.defer(new Func0<Observable<List<FeedItem>>>() {
            @Override
            public Observable<List<FeedItem>> call() {
                try {
                    return Observable.just(get());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Observable.just(getCacheData());
                }
            }
        });
    }

    private List<FeedItem> get() throws IOException, XmlPullParserException {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        Response response = mOkhttpClient.newCall(request).execute();
        String xml = response.body().string();
        List<FeedItem> list = XmlParser.parse(xml);
        String category = getCategoryFromUrl(mUrl);
        for (FeedItem item : list) {
            item.setCategory(category);
        }
        if (!list.isEmpty()) {
            new FeedDao(mContext).insertFeeds(list, category);
        }
        return list;
    }

    private List<FeedItem> getCacheData() {
        return new FeedDao(mContext).getFeedList(getCategoryFromUrl(mUrl));
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
