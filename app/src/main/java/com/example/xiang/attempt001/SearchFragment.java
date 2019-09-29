package com.example.xiang.attempt001;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-09-16.
 */

public class SearchFragment extends Fragment implements OnSearchViewPagerInteractionsListener {
    View myView;
    public  static String query;
    public static int returnCycle;
    public static Product[] searchProductList;
    public static int currentSearchScrollerSize;
    public static int ScrollerHeight;
    RelativeLayout initialSearchProgressLayout;
    ProgressBar initialSearchProgress;
    TextView initialSearchTextView;

    public SearchFragment newInstances(String query)
    {
        SearchFragment searchFragment = new SearchFragment();
        this.query = query;
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollerHeight = 0;
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.search, container, false);
        initialSearchProgressLayout =(RelativeLayout) myView.findViewById(R.id.InitialSearchProgressBarRelativeLayout);
        initialSearchProgress = (ProgressBar)myView.findViewById(R.id.InitialSearchProgressBar) ;
        initialSearchTextView=(TextView)myView.findViewById(R.id.InitialSearchTextView);
        returnCycle = 0;
        searchProductList = new Product[0];
        final ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {

            CompositeSubscription testSubscription = new CompositeSubscription();
            testSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts("MN" + SearchFragment.query+"RETURNCYCLE"+0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleInitialSearchReturnResponse, this::handleInitialSearchReturnError));
        }
        else
        {
            HomeScreen.homeScreenShadow.setVisibility(View.VISIBLE);
            initialSearchProgress.setVisibility(View.INVISIBLE);
            initialSearchTextView.setVisibility(View.VISIBLE);
        }

        return myView;
    }

    @Override
    public void onSearchFragmentMessage(int TAG, String data) {

    }

    public void searchFragment()
    {
        try {
            Bundle args = new Bundle();
            Class fragmentClass = SearchConstraintTabFragment.class;
            args.putString(SearchConstraintViewPagerConstants.SEARCH_FRAG_TAB, "Welcome");
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.SearchFragmentLayout,fragment).commit();
        }
        catch (Exception e)
                {
                        e.printStackTrace();
                }
    }
    private void handleInitialSearchReturnResponse(Product[] searchResults)
    {

        returnCycle = searchResults.length;
        searchProductList = (Product[]) ArrayUtils.addAll(SearchFragment.searchProductList, searchResults);
        initialSearchProgress.setVisibility(View.GONE);
        initialSearchTextView.setVisibility(View.GONE);
        initialSearchProgressLayout.setVisibility(View.GONE);
        searchFragment();

    }
    private void handleInitialSearchReturnError(Throwable error)
    {
        initialSearchProgressLayout.setVisibility(View.VISIBLE);
        initialSearchProgress.setVisibility(View.INVISIBLE);


        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                initialSearchTextView.setText(getString(R.string.internal_server_error));
                initialSearchTextView.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            initialSearchTextView.setText(getString(R.string.network_error));
            initialSearchTextView.setVisibility(View.VISIBLE);
        }
    }
}
