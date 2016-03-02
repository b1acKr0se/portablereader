package com.framgia.nguyenthanhhai.portablereader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.framgia.nguyenthanhhai.portablereader.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private List<String> mItems = new ArrayList<>();
    private Context mContext;

    public CategoryAdapter(Context context) {
        mContext = context;
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(String category) {
        mItems.add(category);
    }

    public void addItems(List<String> categories) {
        mItems.addAll(categories);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.
                    item_spinner, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
    }
}
