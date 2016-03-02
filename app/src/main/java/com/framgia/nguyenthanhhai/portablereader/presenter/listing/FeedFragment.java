package com.framgia.nguyenthanhhai.portablereader.presenter.listing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements IFeedFragmentView {
    static final String BUNDLE_URL = "BUNDLE_URL";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private List<FeedItem> mFeedList = new ArrayList<>();

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(String url) {
        FeedFragment fragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFeedList(List<FeedItem> list) {
        mFeedList.clear();
        mFeedList.addAll(list);
    }

    @Override
    public void onFeedClicked(FeedItem feedItem) {

    }
}
