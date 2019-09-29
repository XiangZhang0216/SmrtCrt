package com.example.xiang.attempt001;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-09-25.
 */

public class SearchScrollView extends ScrollView {
    View globalview;
    public static boolean onlyonce;
    public SearchScrollView(Context context)
    {
        super(context);
    }
    public SearchScrollView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        globalview = view;
        d -= (getHeight() + getScrollY());
        if (d == 0) {

            //End of list

            if (SearchFragment.currentSearchScrollerSize!=0) {
                onlyonce = true;
                final ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    if (SearchConstraintContentFragment.searchProgress!=null) {
                        if(SearchFragment.returnCycle==-1)
                        {
                            Snackbar.make(view, getContext().getString(R.string.there_are_no_more_items_that_match_your_search),Snackbar.LENGTH_SHORT).show();
                            onlyonce = false;
                            SearchConstraintContentFragment.searchProgress.setVisibility(INVISIBLE);

                        }
                        else {
                            SearchConstraintContentFragment.searchProgress.setVisibility(View.VISIBLE);
                            CompositeSubscription testSubscription = new CompositeSubscription();
                            testSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts("MN" + SearchFragment.query + "RETURNCYCLE" + SearchFragment.returnCycle)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(this::handleSearchReturnResponse, this::handleSearchReturnError));
                        }

                    }

                } else {
                    Snackbar.make(view, getContext().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                }
            }

        } else {
            if (SearchConstraintContentFragment.searchProgress != null) {
                SearchConstraintContentFragment.searchProgress.setVisibility(View.INVISIBLE);
            }
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    private void handleSearchReturnResponse(Product[] returnProductList)
    {
        if(onlyonce) {
            if(returnProductList.length==0)
            {
                Snackbar.make(globalview,getContext().getString(R.string.there_are_no_more_items_that_match_your_search),Snackbar.LENGTH_SHORT).show();
                SearchConstraintContentFragment.searchProgress.setVisibility(View.INVISIBLE);
                SearchConstraintTabFragment.myFieldPagerAdapter.notifyDataSetChanged();
                SearchFragment.returnCycle = -1;
                onlyonce = false;
                return;
            }

            int positionArrayBegin = SearchFragment.returnCycle;
            int[]positionArray = new int[returnProductList.length];
            positionArray[0]=positionArrayBegin;
            for (int i =1;i<returnProductList.length;i++) {
                positionArray[i] = positionArray[i - 1] + 1;
            }
            if(returnProductList.length>=10) {
                SearchFragment.returnCycle =SearchFragment.returnCycle+10;
            }
            else if (returnProductList.length<10 && returnProductList.length>0)
            {
                SearchFragment.returnCycle =SearchFragment.returnCycle+returnProductList.length;
            }
            else
            {
                SearchFragment.returnCycle = -1;
            }

            SearchFragment.searchProductList = (Product[]) ArrayUtils.addAll(SearchFragment.searchProductList, returnProductList);
            for(int j = 0; j< returnProductList.length;j++) {
                SearchConstraintContentFragment.searchAdapter.add(returnProductList[j],positionArray[j]);
                int[] location = new int[2];
                SearchConstraintContentFragment.SearchBigScroll.getLocationInWindow(location);
                SearchFragment.ScrollerHeight =getHeight()+getScrollY();
                SearchConstraintTabFragment.myFieldPagerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"ScrollerHeight: "+SearchFragment.ScrollerHeight+ " location: "+ location[0]+ ", "+ location[1],Toast.LENGTH_SHORT).show();

            }
            onlyonce=false;
        }

    }
    private void handleSearchReturnError(Throwable error)
    {
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Snackbar.make(globalview,R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(globalview,R.string.network_error,Snackbar.LENGTH_SHORT).show();
        }
    }

}
