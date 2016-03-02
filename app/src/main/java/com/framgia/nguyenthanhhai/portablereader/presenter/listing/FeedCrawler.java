package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

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

    public FeedCrawler(String url) {
        this.mUrl = url;
    }

    @Override
    public Observable<List<FeedItem>> getFeedList() {
        return Observable.defer(new Func0<Observable<List<FeedItem>>>() {
            @Override
            public Observable<List<FeedItem>> call() {
                try {
                    return Observable.just(get());
                } catch (XmlPullParserException | IOException e) {
                    return Observable.error(e);
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
        return XmlParser.parse(xml);
    }
}
