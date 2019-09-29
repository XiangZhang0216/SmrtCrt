package com.example.xiang.attempt001;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by xiang on 2017-09-16.
 */

public class SearchConstraintContentFragment extends Fragment implements  OnSearchViewPagerInteractionsListener{
    private OnSearchViewPagerInteractionsListener myListener;
    public static TextView youhavesearched;
    public static ProgressBar searchProgress;
    public static RecyclerView SearchRecyclerView;
    public static SearchOfferAdapter searchAdapter;
    public static RelativeLayout emptySearchLayout;
    public static SearchScrollView SearchBigScroll;

    public SearchConstraintContentFragment()
    {

    }

    public static SearchConstraintContentFragment newInstance(String tabSelected)
    {
        SearchConstraintContentFragment content = new SearchConstraintContentFragment();
        Bundle args = new Bundle();
        args.putString(SearchConstraintViewPagerConstants.SEARCH_FRAG_CONTENT,tabSelected);

        content.setArguments(args);
        return content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_constraint_content_fragment, container, false);
        SearchBigScroll = (SearchScrollView)view.findViewById(R.id.SearchConstraintBigScroll);
        youhavesearched = (TextView) view.findViewById(R.id.YouHaveSearched);
        youhavesearched.setText(getString(R.string.you_searched_for) + SearchFragment.query);
        searchProgress = (ProgressBar) view.findViewById(R.id.SearchLoadingProgress);
        emptySearchLayout = (RelativeLayout)view.findViewById(R.id.emptySearchLayout);
        SearchRecyclerView = (RecyclerView) view.findViewById(R.id.SearchRecyclerView);
        SearchRecyclerView.setNestedScrollingEnabled(false);
        searchAdapter = new SearchOfferAdapter(getActivity(),SearchFragment.searchProductList,false);
        SearchRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        SearchRecyclerView.setAdapter(searchAdapter);

        if (SearchFragment.searchProductList.length == 0 )
        {
            searchProgress.setVisibility(View.INVISIBLE);
            emptySearchLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchBigScroll.setFocusable(true);
        SearchBigScroll.setFocusableInTouchMode(true);SearchBigScroll.setScrollY(SearchFragment.ScrollerHeight);
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
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSearchFragmentMessage(int TAG, String data) {

    }

    }



