package com.framgia.nguyenthanhhai.portablereader.presenter.detail;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.framgia.nguyenthanhhai.portablereader.ui.activity.BaseActivity;
import com.framgia.nguyenthanhhai.portablereader.ui.customtabs.CustomTabActivityHelper;
import com.framgia.nguyenthanhhai.portablereader.util.DateDifferenceConverter;
import com.framgia.nguyenthanhhai.portablereader.util.TextFormatter;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    static final String EXTRA_IMAGE = "com.framgia.nguyenthanhhai.extraImage";
    static final String EXTRA_FEED = "com.framgia.nguyenthanhhai.extraFeed";
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    static final int TYPE_TEXT = 0;
    static final int TYPE_IMAGE = 1;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private CustomTabsClient mClient;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsIntent customTabsIntent;
    private FeedItem mFeedItem;
    private int layoutType;
    private boolean mShouldOverrideTransition = true;

    public static void navigate(AppCompatActivity activity, View transitionImage, FeedItem feedItem) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_IMAGE, feedItem.getImage());
        intent.putExtra(EXTRA_FEED, feedItem);
        if (transitionImage != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTransition();
        if (getIntent().getStringExtra(EXTRA_IMAGE) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            setContentView(R.layout.activity_detail);
            layoutType = TYPE_IMAGE;
        } else {
            setContentView(R.layout.activity_detail_text);
            layoutType = TYPE_TEXT;
        }
        bindViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mShouldOverrideTransition) supportFinishAfterTransition();
        else finish();
    }

    @Override
    protected void bindViews() {
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                mShouldOverrideTransition = state == State.EXPANDED;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.read_article));
        setupChromeCustomTab();
        mFeedItem = (FeedItem) getIntent().getSerializableExtra(EXTRA_FEED);
        if (layoutType == TYPE_IMAGE) {
            mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            mCollapsingToolbarLayout.setTitle(mFeedItem.getTitle());
            mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
            final ImageView imageView = (ImageView) findViewById(R.id.image);
            Glide.with(this).load(mFeedItem.getImage())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            imageView.setImageBitmap(resource);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    applyPalette(palette);
                                }
                            });
                        }
                    });
        }
        TextView titleTextView = (TextView) findViewById(R.id.text_title);
        titleTextView.setText(mFeedItem.getTitle());
        TextView descTextView = (TextView) findViewById(R.id.text_description);
        descTextView.setText(TextFormatter.removeMultipleLineBreaks(mFeedItem.getDescription()));
        TextView authorTextView = (TextView) findViewById(R.id.text_author);
        authorTextView.setText(mFeedItem.getAuthor() == null ? "author not stated" : mFeedItem.getAuthor());
        TextView pubDateTextView = (TextView) findViewById(R.id.text_pub_date);
        pubDateTextView.setText(DateDifferenceConverter.getDateDifference(mFeedItem.getPubDate()));
        View linkButton = findViewById(R.id.button_link);
        View favoriteButton = findViewById(R.id.button_favorite);
        View shareButton = findViewById(R.id.button_share);
        linkButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_link:
                openLinkWithChromeCustomTab();
                break;
            case R.id.button_favorite:
                break;
            case R.id.button_share:
                break;
        }
    }

    private void openLinkWithChromeCustomTab() {
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(mFeedItem.getLink()),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        int primary = ContextCompat.getColor(this, R.color.colorPrimary);
        mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
        String hexColor = String.format("#%06X", (0xFFFFFF & palette.getMutedColor(primary)));
        int color = Color.parseColor(hexColor);
        customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(color)
                .setShowTitle(true)
                .build();
    }

    private void setActivityTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void setupChromeCustomTab() {

    }
}
