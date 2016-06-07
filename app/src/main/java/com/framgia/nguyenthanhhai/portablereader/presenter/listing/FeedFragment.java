package com.framgia.nguyenthanhhai.portablereader.presenter.listing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.data.local.FeedDao;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.framgia.nguyenthanhhai.portablereader.presenter.detail.DetailActivity;
import com.framgia.nguyenthanhhai.portablereader.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class FeedFragment extends Fragment implements IFeedFragmentView {
    static final String BUNDLE_URL = "BUNDLE_URL";
    static final int INVALID_ID = -1;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private FeedPresenter mFeedPresenter;
    private Subscription mFeedSubscription;
    private FeedAdapter mFeedAdapter;
    private List<FeedItem> mFeedList = new ArrayList<>();
    private String mUrl;
    private int position = INVALID_ID;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(BUNDLE_URL);
        mFeedPresenter = new FeedPresenter(getContext(), this, mUrl);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mFeedAdapter = new FeedAdapter(getContext(), mFeedList, this);
        mRecyclerView.setAdapter(mFeedAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFeedSubscription = mFeedPresenter.displayFeed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (position != INVALID_ID) {
            mFeedAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFeedSubscription != null && mFeedSubscription.isUnsubscribed()) {
            mFeedSubscription.unsubscribe();
        }
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
        mFeedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFeedClicked(View view, FeedItem feedItem) {
        new FeedDao(getContext()).insertRead(feedItem);
        position = mFeedList.indexOf(feedItem);
        feedItem.setReadStatus(true);
        view.findViewById(R.id.text_desc).setVisibility(View.GONE);
        DetailActivity.navigate((AppCompatActivity) getActivity(), view.findViewById(R.id.image_feed), feedItem);
    }
}
