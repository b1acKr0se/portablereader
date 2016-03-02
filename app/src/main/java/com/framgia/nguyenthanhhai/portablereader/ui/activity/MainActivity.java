package com.framgia.nguyenthanhhai.portablereader.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.framgia.nguyenthanhhai.portablereader.R;
import com.framgia.nguyenthanhhai.portablereader.constant.Api;
import com.framgia.nguyenthanhhai.portablereader.presenter.listing.FeedFragment;
import com.framgia.nguyenthanhhai.portablereader.ui.adapter.CategoryAdapter;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar mToolbar;
    private List<String> mCategoryList = Arrays.asList("Science & Technology", "Economy", "Health", "Arts & Entertainment");
    private int currentSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        switchFragment(Api.SCIENCE_AND_TECHNOLOGY);
    }

    @Override
    protected void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner,
                mToolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mToolbar.addView(spinnerContainer, lp);
        CategoryAdapter spinnerAdapter = new CategoryAdapter(this);
        spinnerAdapter.addItems(mCategoryList);
        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(MainActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (currentSelected != position) {
            changeCategory(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switchFragment(Api.SCIENCE_AND_TECHNOLOGY);
    }

    private void changeCategory(int position) {
        switch (position) {
            case 0:
                switchFragment(Api.SCIENCE_AND_TECHNOLOGY);
                currentSelected = 0;
                break;
            case 1:
                switchFragment(Api.ECONOMY);
                currentSelected = 1;
                break;
            case 2:
                switchFragment(Api.HEALTH);
                currentSelected = 2;
                break;
            case 3:
                switchFragment(Api.ART_AND_ENTERTAINMENT);
                currentSelected = 3;
                break;
        }
    }

    private void switchFragment(String url) {
        FeedFragment fragment = FeedFragment.newInstance(url);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
