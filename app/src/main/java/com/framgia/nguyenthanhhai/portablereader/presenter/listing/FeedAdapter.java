package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.framgia.nguyenthanhhai.portablereader.util.DateDifferenceConverter;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private List<FeedItem> mFeedList;
    private Context mContext;
    private IFeedFragmentView mFeedFragmentView;
    private int textColorRead, textColorUnread;

    public FeedAdapter(Context context, List<FeedItem> list, IFeedFragmentView feedFragmentView) {
        this.mContext = context;
        this.mFeedList = list;
        this.mFeedFragmentView = feedFragmentView;
        this.textColorRead = ContextCompat.getColor(mContext, R.color.readTitle);
        this.textColorUnread = ContextCompat.getColor(mContext, R.color.unreadTitle);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.feedItem = mFeedList.get(position);
        holder.itemView.setOnClickListener(holder);
        holder.titleTextView.setText(holder.feedItem.getTitle());
        if(holder.feedItem.isRead()) {
            holder.titleTextView.setTextColor(textColorRead);
        } else {
            holder.titleTextView.setTextColor(textColorUnread);
        }
        holder.authorTextView.setText(holder.feedItem.getAuthor() == null ? "author not stated" : holder.feedItem.getAuthor());
        holder.pubDateTextView.setText(DateDifferenceConverter.getDateDifference(holder.feedItem.getPubDate()));
        holder.descriptionTextView.setText(holder.feedItem.getDescription().replaceAll("\\s+", " "));
        Glide.with(mContext).load(holder.feedItem.getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.descriptionTextView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.descriptionTextView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FeedItem feedItem;
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView pubDateTextView;
        private TextView descriptionTextView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_title);
            authorTextView = (TextView) view.findViewById(R.id.text_author);
            pubDateTextView = (TextView) view.findViewById(R.id.text_pub_date);
            descriptionTextView = (TextView) view.findViewById(R.id.text_desc);
            imageView = (ImageView) view.findViewById(R.id.image_feed);
        }

        @Override
        public void onClick(View v) {
            mFeedFragmentView.onFeedClicked(feedItem);
        }
    }
}
