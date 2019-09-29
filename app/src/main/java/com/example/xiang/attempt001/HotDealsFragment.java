package com.example.xiang.attempt001;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiang on 2017-05-14.
 */

public class HotDealsFragment extends Fragment implements OnHotDealsViewPagerInteractionsListener{
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.hot_deals, container, false);
        hotDealsFragment();
        return myView;
    }

    @Override
    public void onFragmentMessage(int TAG, String data) {

    }
    public void hotDealsFragment()
    {
        try
        {
            Bundle args = new Bundle();
            Class fragmentClass = HotDealsTabFragment.class;
            args.putString(HotDealsViewPagerConstants.FRAG_TAB,"Welcome");
            Fragment fragment = (Fragment)fragmentClass.newInstance();
            fragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.hotDealsFragmentLayout,fragment).commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
