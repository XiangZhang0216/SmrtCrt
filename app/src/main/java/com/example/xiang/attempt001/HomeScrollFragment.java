package com.example.xiang.attempt001;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by xiang on 2017-09-04.
 */

public class HomeScrollFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mIntResource1;

    public HomeScrollFragment()
    {

    }

    public static HomeScrollFragment newInstance(int mIntResource1)
    {
        HomeScrollFragment homeScrollFragment = new HomeScrollFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,mIntResource1);
        homeScrollFragment.setArguments(args);
        return homeScrollFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            mIntResource1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homescroll,container,false);
        ImageView image = (ImageView)view.findViewById(R.id.homescrollimage);
        image.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        image.setImageDrawable(getResources().getDrawable(mIntResource1));
        return view;
    }

}
