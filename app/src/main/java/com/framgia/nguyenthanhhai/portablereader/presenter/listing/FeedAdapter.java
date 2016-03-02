package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.feedItem = mFeedList.get(position);
        holder.itemView.setOnClickListener(holder);
        holder.titleTextView.setText(holder.feedItem.getmTitle());
        if(holder.feedItem.isRead()) {
            holder.titleTextView.setTextColor(textColorRead);
        } else {
            holder.titleTextView.setTextColor(textColorUnread);
        }
        holder.authorTextView.setText(holder.feedItem.getmAuthor() == null ? "author not stated" : holder.feedItem.getmAuthor());
        holder.pubDateTextView.setText(DateDifferenceConverter.getDateDifference(holder.feedItem.getmPubDate()));
        holder.descriptionTextView.setText(holder.feedItem.getmDesc().replaceAll("\\s+", " "));
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

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.text_title);
            authorTextView = (TextView) view.findViewById(R.id.text_author);
            pubDateTextView = (TextView) view.findViewById(R.id.text_pub_date);
            descriptionTextView = (TextView) view.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View v) {
            mFeedFragmentView.onFeedClicked(feedItem);
        }
    }
}
