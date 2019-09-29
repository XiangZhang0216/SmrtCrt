package com.example.xiang.attempt001;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by xiang on 2017-05-18.
 */

public class HotDealsSectionsPagerAdapter extends FragmentStatePagerAdapter {
    int myNumOfTabs;
    ArrayList<String> tabName;

    //Constructors
    public HotDealsSectionsPagerAdapter(FragmentManager fm, int numOfTabs, ArrayList<String> tabName)
    {
        super(fm);
        this.myNumOfTabs = numOfTabs;
        this.tabName = tabName;
    }

    @Override
    public Fragment getItem(int position) {
        HotDealsContentFragment contentFragment = new HotDealsContentFragment();
        return contentFragment.newInstance(tabName.get(position));
    }

    @Override
    public int getCount() {
        return myNumOfTabs;
    }
}
