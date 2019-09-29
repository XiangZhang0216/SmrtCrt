package com.example.xiang.attempt001;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by xiang on 2017-05-18.
 */

public class HotDealsTabFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager myViewPager;
    private OnHotDealsViewPagerInteractionsListener myListener;
    private HotDealsSectionsPagerAdapter mySectionsPagerAdapter;
    ArrayList<String> tabName;

    //Constructors
    public HotDealsTabFragment()
    {

    }
    public static HotDealsTabFragment newInstance(String navigation)
    {
        HotDealsTabFragment tabFragment = new HotDealsTabFragment();
        Bundle args = new Bundle();
        args.putString(HotDealsViewPagerConstants.FRAG_TAB, navigation);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_deals_tab_fragment, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        myViewPager = (ViewPager)view.findViewById(R.id.container);
        tabName = new ArrayList<String>();

        for(int i = 0; i<4; i++ )
        {
            if(i==0)
            {
                tabLayout.addTab(tabLayout.newTab().setText(R.string.popular));
                tabName.add(getString(R.string.popular));
            }
            if (i==1)
            {
                tabLayout.addTab(tabLayout.newTab().setText(R.string.trending));
                tabName.add(getString(R.string.trending));
            }
            if (i==2)
            {
                tabLayout.addTab(tabLayout.newTab().setText(R.string.recommended));
                tabName.add(getString(R.string.recommended));
            }
            if (i==3)
            {
                tabLayout.addTab(tabLayout.newTab().setText(R.string.on_sale));
                tabName.add(getString(R.string.on_sale));
            }
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mySectionsPagerAdapter = new HotDealsSectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        myViewPager.setAdapter(mySectionsPagerAdapter);
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager.setCurrentItem(tab.getPosition());
                getChildFragmentManager().beginTransaction().addToBackStack(null).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mySectionsPagerAdapter = new HotDealsSectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        myViewPager.setAdapter(mySectionsPagerAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHotDealsViewPagerInteractionsListener)
        {
            myListener = (OnHotDealsViewPagerInteractionsListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "must implement OnHotDealsViewPagerInteractionsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
