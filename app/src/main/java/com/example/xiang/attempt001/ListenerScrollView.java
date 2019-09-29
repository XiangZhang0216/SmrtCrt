package com.example.xiang.attempt001;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-09-10.
 */

public class ListenerScrollView extends ScrollView {
        View globalview;
        public static boolean onlyonce;
        public ListenerScrollView(Context context)
        {
            super(context);
        }
        public ListenerScrollView(Context context, AttributeSet attributeSet)
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

                    onlyonce = true;
                    final ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    if (activeNetwork != null && activeNetwork.isConnected()) {
                        if (HomeFragment.scrollLoadingProgress != null) {
                            if(HomeScreen.returncycle==-1)
                            {
                                Snackbar.make(view, getContext().getString(R.string.this_department_has_no_more_products),Snackbar.LENGTH_SHORT).show();
                                onlyonce = false;
                                HomeFragment.scrollLoadingProgress.setVisibility(View.INVISIBLE);
                            }
                            else {
                                HomeFragment.scrollLoadingProgress.setVisibility(View.VISIBLE);
                                CompositeSubscription testSubscription = new CompositeSubscription();
                                testSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts(HomeScreen.currentCategory + "RETURNCYCLE" + HomeScreen.returncycle)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(this::handleReturnResponse, this::handleReturnError));
                            }

                        }

                    } else {
                        Snackbar.make(view, getContext().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                    }

            } else {
                if (HomeFragment.scrollLoadingProgress != null) {
                    HomeFragment.scrollLoadingProgress.setVisibility(View.INVISIBLE);
                }
                super.onScrollChanged(l, t, oldl, oldt);
            }
        }

            private void handleReturnResponse(Product[] returnProductList)
    {
        if(onlyonce) {
            if(returnProductList.length==0)
            {
                Snackbar.make(globalview,getContext().getString(R.string.this_department_has_no_more_products),Snackbar.LENGTH_SHORT).show();
                HomeFragment.scrollLoadingProgress.setVisibility(View.INVISIBLE);
                HomeScreen.returncycle = -1;
                onlyonce = false;
                return;
            }
            int positionArrayBegin = HomeScreen.returncycle;
            int[]positionArray = new int[returnProductList.length];
            positionArray[0]=positionArrayBegin;
            for (int i =1;i<returnProductList.length;i++) {
                positionArray[i] = positionArray[i - 1] + 1;
            }
            if(returnProductList.length>=10) {
                HomeScreen.returncycle = HomeScreen.returncycle+10;
            }
            else if (returnProductList.length<10 && returnProductList.length>0)
            {
                HomeScreen.returncycle = HomeScreen.returncycle+returnProductList.length;
            }
            else
            {
                HomeScreen.returncycle = -1;
            }

            ProductDataSource.storedArray = (Product[]) ArrayUtils.addAll(ProductDataSource.storedArray, returnProductList);
            for(int j = 0; j< returnProductList.length;j++) {
                HomeFragment.homeOfferAdapter.add(returnProductList[j],positionArray[j]);
            }
            onlyonce=false;
        }

    }
    private void handleReturnError(Throwable error)
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
