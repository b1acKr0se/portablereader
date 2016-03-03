package com.framgia.nguyenthanhhai.portablereader.presenter.detail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.framgia.nguyenthanhhai.portablereader.ui.activity.BaseActivity;
import com.framgia.nguyenthanhhai.portablereader.util.DateDifferenceConverter;
import com.framgia.nguyenthanhhai.portablereader.util.TextFormatter;

public class DetailActivity extends BaseActivity {
    static final String EXTRA_IMAGE = "com.framgia.nguyenthanhhai.extraImage";
    static final String EXTRA_FEED = "com.framgia.nguyenthanhhai.extraFeed";
    static final int TYPE_TEXT = 0;
    static final int TYPE_IMAGE = 1;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private int layoutType;

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
    protected void bindViews() {
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.read_article));
        FeedItem item = (FeedItem) getIntent().getSerializableExtra(EXTRA_FEED);
        if (layoutType == TYPE_IMAGE) {
            mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            mCollapsingToolbarLayout.setTitle(item.getTitle());
            mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
            final ImageView imageView = (ImageView) findViewById(R.id.image);
            Glide.with(this).load(item.getImage())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imageView) {
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
        titleTextView.setText(item.getTitle());
        TextView descTextView = (TextView) findViewById(R.id.text_description);
        descTextView.setText(TextFormatter.removeMultipleLineBreaks(item.getDescription()));
        TextView authorTextView = (TextView) findViewById(R.id.text_author);
        authorTextView.setText(item.getAuthor());
        TextView pubDateTextView = (TextView) findViewById(R.id.text_pub_date);
        pubDateTextView.setText(DateDifferenceConverter.getDateDifference(item.getPubDate()));
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
        mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(ContextCompat.getColor(this, android.R.color.white));
        int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(this, R.color.colorAccent));
        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

    private void setActivityTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }
}
