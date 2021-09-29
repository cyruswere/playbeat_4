package com.cyruswere.playbeat_4.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cyruswere.playbeat_4.models.Result;
import com.cyruswere.playbeat_4.ui.ResultDetailFragment;

import java.util.List;

public class ResultPagerAdapter extends FragmentPagerAdapter {
    private List<Result> mResults;

    public ResultPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Result> results) {
        super(fm, behavior);
        mResults = results;
    }

    @Override
    public Fragment getItem(int position) {
        return ResultDetailFragment.newInstance(mResults.get(position));
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mResults.get(position).getArtistName();
    }
}


