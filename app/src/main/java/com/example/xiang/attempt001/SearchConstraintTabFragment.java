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
 * Created by xiang on 2017-09-16.
 */

public class SearchConstraintTabFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager searchViewPager;
    private OnSearchViewPagerInteractionsListener myListener;
    public static SearchFieldPagerAdapter myFieldPagerAdapter;
    ArrayList<String> tabName;

    public SearchConstraintTabFragment newInstance(String navigation)
    {
        SearchConstraintTabFragment tabFragment = new SearchConstraintTabFragment();
        Bundle args = new Bundle();
        args.putString(SearchConstraintViewPagerConstants.SEARCH_FRAG_TAB,navigation);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HomeScreen.homeScreenShadow.setVisibility(View.INVISIBLE);
        View view = inflater.inflate(R.layout.search_constraint_tab_fragment,container,false);
        tabLayout = (TabLayout)view.findViewById(R.id.SearchConstraintTabs);


        searchViewPager = (ViewPager)view.findViewById(R.id.SearchConstraintTabContainer);

        tabName = new ArrayList<String>();
        for(int i = 0; i<2; i++)
        {
            if(i==0)
            {
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.by_name)));
                tabName.add(getString(R.string.by_name));
            }
            if(i==1)
            {
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.by_store)));
                tabName.add(getString(R.string.by_store));
            }
        }

        myFieldPagerAdapter = new SearchFieldPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        searchViewPager.setAdapter(myFieldPagerAdapter);
        searchViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchViewPager.setCurrentItem(tab.getPosition());
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
        myFieldPagerAdapter = new SearchFieldPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        searchViewPager.setAdapter(myFieldPagerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSearchViewPagerInteractionsListener)
        {
            myListener = (OnSearchViewPagerInteractionsListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString()+"must implement OnSearchViewPagerInteractionsListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }
}
