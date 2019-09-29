package com.example.xiang.attempt001;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by xiang on 2017-09-16.
 */

public class SearchFieldPagerAdapter extends FragmentStatePagerAdapter {

    int myNumOfTabs;
    ArrayList<String> tabName;
    public SearchFieldPagerAdapter(FragmentManager fm, int numOfTabs, ArrayList<String>tabName)
    {
        super(fm);
        this.myNumOfTabs = numOfTabs;
        this.tabName = tabName;
    }
    @Override
    public Fragment getItem(int position) {
        SearchConstraintContentFragment contentFragment = new SearchConstraintContentFragment();
        return contentFragment.newInstance(tabName.get(position));

    }

    @Override
    public int getCount() {
        return myNumOfTabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
