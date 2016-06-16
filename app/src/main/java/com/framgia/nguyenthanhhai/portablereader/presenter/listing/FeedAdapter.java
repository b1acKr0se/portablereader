package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;
import com.framgia.nguyenthanhhai.portablereader.util.DateDifferenceConverter;
import com.framgia.nguyenthanhhai.portablereader.util.ScreenUtils;
import com.framgia.nguyenthanhhai.portablereader.util.TextFormatter;

import java.io.File;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private List<FeedItem> mFeedList;
    private Context mContext;
    private IFeedFragmentView mFeedFragmentView;
    private int textColorRead, textColorUnread;
    private int lastAnimatedPosition = -1;

    public FeedAdapter(Context context, List<FeedItem> list, IFeedFragmentView feedFragmentView) {
        this.mContext = context;
        this.mFeedList = list;
        this.mFeedFragmentView = feedFragmentView;
        this.textColorRead = ContextCompat.getColor(mContext, R.color.readTitle);
        this.textColorUnread = ContextCompat.getColor(mContext, R.color.unreadTitle);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_with_image, parent, false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed, parent, false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFeedList.get(position).getImage() != null) {
            return TYPE_IMAGE;
        }
        return TYPE_TEXT;
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT) {
            return;
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(1000)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        if (viewHolder instanceof ImageViewHolder) {
            final ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
            imageViewHolder.feedItem = mFeedList.get(position);
            imageViewHolder.itemView.setOnClickListener(imageViewHolder);
            imageViewHolder.titleTextView.setText(imageViewHolder.feedItem.getTitle());
            if (imageViewHolder.feedItem.isRead()) {
                imageViewHolder.titleTextView.setTextColor(textColorRead);
            } else {
                imageViewHolder.titleTextView.setTextColor(textColorUnread);
            }
            imageViewHolder.authorTextView.setText(imageViewHolder.feedItem.getAuthor() == null ? "author not stated" : imageViewHolder.feedItem.getAuthor());
            imageViewHolder.pubDateTextView.setText(DateDifferenceConverter.getDateDifference(imageViewHolder.feedItem.getPubDate()));
            imageViewHolder.descriptionTextView.setText(TextFormatter.removeMultipleLineBreaks(imageViewHolder.feedItem.getDescription()));
            Glide.with(mContext)
                    .load(imageViewHolder.feedItem.getImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            imageViewHolder.descriptionTextView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imageViewHolder.descriptionTextView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imageViewHolder.imageView);
        } else if (viewHolder instanceof TextViewHolder) {
            final TextViewHolder textViewHolder = (TextViewHolder) viewHolder;
            textViewHolder.feedItem = mFeedList.get(position);
            textViewHolder.itemView.setOnClickListener(textViewHolder);
            textViewHolder.titleTextView.setText(textViewHolder.feedItem.getTitle());
            if (textViewHolder.feedItem.isRead()) {
                textViewHolder.titleTextView.setTextColor(textColorRead);
            } else {
                textViewHolder.titleTextView.setTextColor(textColorUnread);
            }
            textViewHolder.authorTextView.setText(textViewHolder.feedItem.getAuthor() == null ? "author not stated" : textViewHolder.feedItem.getAuthor());
            textViewHolder.pubDateTextView.setText(DateDifferenceConverter.getDateDifference(textViewHolder.feedItem.getPubDate()));
            textViewHolder.descriptionTextView.setText(TextFormatter.removeMultipleLineBreaks(textViewHolder.feedItem.getDescription()));
        }
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    protected class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FeedItem feedItem;
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView pubDateTextView;
        private TextView descriptionTextView;
        private ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_title);
            authorTextView = (TextView) view.findViewById(R.id.text_author);
            pubDateTextView = (TextView) view.findViewById(R.id.text_pub_date);
            descriptionTextView = (TextView) view.findViewById(R.id.text_desc);
            imageView = (ImageView) view.findViewById(R.id.image_feed);
        }

        @Override
        public void onClick(View v) {
            mFeedFragmentView.onFeedClicked(v, feedItem);
        }
    }

    protected class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FeedItem feedItem;
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView pubDateTextView;
        private TextView descriptionTextView;

        public TextViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_title);
            authorTextView = (TextView) view.findViewById(R.id.text_author);
            pubDateTextView = (TextView) view.findViewById(R.id.text_pub_date);
            descriptionTextView = (TextView) view.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View v) {
            mFeedFragmentView.onFeedClicked(v, feedItem);
        }
    }
}
