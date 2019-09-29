package com.example.xiang.attempt001;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by xiang on 2017-05-18.
 */

public class HotDealsContentFragment extends Fragment implements OnHotDealsViewPagerInteractionsListener{
    private OnHotDealsViewPagerInteractionsListener myListener;
    private TextView tabSelection;
    //Constructors
    public HotDealsContentFragment()
    {

    }
    public  static HotDealsContentFragment newInstance(String tabSelected)
    {
        HotDealsContentFragment content = new HotDealsContentFragment();
        Bundle args = new Bundle();
        args.putString(HotDealsViewPagerConstants.FRAG_CONTENT, tabSelected);
        content.setArguments(args);
        return content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_deals_content_fragment,container,false);
        tabSelection = (TextView)view.findViewById(R.id.tabSelection);
        RecyclerView hotDealsOffers = (RecyclerView)view.findViewById(R.id.HotDealsOfferRecyclerView);
        hotDealsOffers.setNestedScrollingEnabled(false);
        return view;
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
            throw new RuntimeException(context.toString()+"must implement OnHotDealsViewPagerInteractionsListener");
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
    public void onFragmentMessage(int TAG, String data) {

    }
}

