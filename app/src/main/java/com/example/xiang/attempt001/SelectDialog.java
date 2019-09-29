package com.example.xiang.attempt001;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-08-12.
 */

public class SelectDialog extends android.app.DialogFragment{
    View myView;
    public static final String TAG = SelectDialog.class.getSimpleName();
    private  CompositeSubscription selectSubscription;
    private  String req;
    private  String selectionName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Rect displayRect = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRect);
        myView=inflater.inflate(R.layout.dialog_select, container,false);
        RadioGroup rGroup = (RadioGroup) myView.findViewById(R.id.SelectRadioGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                HomeScreen.returncycle = 10;
                switch (checkedId){
                    case R.id.categoryAll:
                        req = "MU";
                        selectionName = getString(R.string.all);
                        break;
                    case R.id.categoryA:
                        req = "MCa";
                        selectionName = getString(R.string.animals_amp_pet_supplies);
                        break;
                    case R.id.categoryB:
                        req = "MCb";
                        selectionName = getString(R.string.apparel_amp_accessories);
                        break;
                    case R.id.categoryC:
                        req = "MCc";
                        selectionName = getString(R.string.arts_amp_entertainment);
                        break;
                    case R.id.categoryD:
                        req = "MCd";
                        selectionName = getString(R.string.baby_amp_toddler);
                        break;
                    case R.id.categoryE:
                        req = "MCe";
                        selectionName = getString(R.string.business_amp_industrial);
                        break;
                    case R.id.categoryF:
                        req = "MCf";
                        selectionName = getString(R.string.cameras_amp_optics);
                        break;
                    case R.id.categoryG:
                        req = "MCg";
                        selectionName = getString(R.string.electronics);
                        break;
                    case R.id.categoryH:
                        req = "MCh";
                        selectionName = getString(R.string.food_beverages_amp_tobacco);
                        break;
                    case R.id.categoryI:
                        req = "MCi";
                        selectionName = getString(R.string.furniture);
                        break;
                    case R.id.categoryJ:
                        req = "MCj";
                        selectionName = getString(R.string.hardware);
                        break;
                    case R.id.categoryK:
                        req = "MCk";
                        selectionName = getString(R.string.health_amp_beauty);
                        break;
                    case R.id.categoryL:
                        req = "MCl";
                        selectionName = getString(R.string.home_amp_garden);
                        break;
                    case R.id.categoryM:
                        req = "MCm";
                        selectionName = getString(R.string.luggage_amp_bags);
                        break;
                    case R.id.categoryN:
                        req = "MCn";
                        selectionName = getString(R.string.mature);
                        break;
                    case R.id.categoryO:
                        req = "MCo";
                        selectionName = getString(R.string.media);
                        break;
                    case R.id.categoryP:
                        req = "MCp";
                        selectionName = getString(R.string.office_supplies);
                        break;
                    case R.id.categoryQ:
                        req = "MCq";
                        selectionName = getString(R.string.religious_amp_ceremonial);
                        break;
                    case R.id.categoryR:
                        req = "MCr";
                        selectionName = getString(R.string.software);
                        break;
                    case R.id.categoryS:
                        req = "MCs";
                        selectionName = getString(R.string.sporting_goods);
                        break;
                    case R.id.categoryT:
                        req = "MCt";
                        selectionName = getString(R.string.toys_amp_games);
                        break;
                    case R.id.categoryU:
                        req = "MCu";
                        selectionName = getString(R.string.vehicles_amp_parts);
                        break;
                    default:
                        req = "MU";
                        selectionName = getString(R.string.all);
                        break;
                }
                final ConnectivityManager conMgr = (ConnectivityManager)getActivity(). getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                if(activeNetwork != null && activeNetwork.isConnected())
                {


                    switch (req){
                        case "MU":
                                HomeScreen.currentCategory = "MRU";
                            break;
                        case "MCa":
                                 HomeScreen.currentCategory = "MRCa";
                            break;
                        case "MCb":
                                HomeScreen.currentCategory = "MRCb";
                            break;
                        case "MCc":
                                  HomeScreen.currentCategory = "MRCc";
                            break;
                        case "MCd":
                                  HomeScreen.currentCategory = "MRCd";
                            break;
                        case  "MCe":
                                  HomeScreen.currentCategory = "MRCe";
                            break;
                        case "MCf":
                                    HomeScreen.currentCategory = "MRCf";
                            break;
                        case  "MCg":
                                   HomeScreen.currentCategory = "MRCg";
                            break;
                        case  "MCh":
                                      HomeScreen.currentCategory = "MRCh";
                            break;
                        case "MCi":
                                   HomeScreen.currentCategory = "MRCi";
                            break;
                        case "MCj":
                                  HomeScreen.currentCategory = "MRCj";
                            break;
                        case "MCk":
                                    HomeScreen.currentCategory = "MRCk";
                            break;
                        case  "MCl":
                                   HomeScreen.currentCategory = "MRCl";
                            break;
                        case  "MCm":
                                    HomeScreen.currentCategory = "MRCm";
                            break;
                        case "MCn":
                                    HomeScreen.currentCategory = "MRCn";
                            break;
                        case  "MCo":
                                  HomeScreen.currentCategory = "MRCo";
                            break;
                        case  "MCp":
                                    HomeScreen.currentCategory = "MRCp";
                            break;
                        case "MCq":
                                    HomeScreen.currentCategory = "MRCq";
                            break;
                        case  "MCr":
                                    HomeScreen.currentCategory = "MRCr";
                            break;
                        case  "MCs":
                                    HomeScreen.currentCategory = "MRCs";
                            break;
                        case  "MCt":
                                   HomeScreen.currentCategory = "MRCt";
                            break;
                        case  "MCu":
                                       HomeScreen.currentCategory = "MRCu";
                            break;
                        default:
                            HomeScreen.currentCategory = "MRU";
                            break;
                    }


                    HomeScreen.HomeScreenProgress = new ProgressDialog(getActivity());
                    HomeScreen.HomeScreenProgress.setMessage(getString(R.string.searching_department)+ selectionName+"...");
                    HomeScreen.HomeScreenProgress.setCancelable(false);
                    HomeScreen.HomeScreenProgress.show();
                    selectSubscription = new CompositeSubscription();
                    QueryCondition queryCondition = new QueryCondition();
                    queryCondition.setConditionString(req);
                    queryCondition.setReturncycleint(777);
                    selectSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts(req)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(this::handleSelectResponse, this::handleSelectError));

                }
                else
                {
                    Snackbar.make(myView,getString(R.string.internet_is_required_to_search_products),Snackbar.LENGTH_LONG).show();
                }


            }
            private void handleSelectResponse(Product [] productList)
            {
                ProductDataSource.storedArray = new Product[productList.length];
                for (int c = 0; c< productList.length; c++)
                {
                    ProductDataSource.storedArray[c] = productList[c];
                }
                HomeFragment.LimitedOffersHeader.setText(getString(R.string.offered_deals)+selectionName);
                RecyclerView changeOffers = (RecyclerView)getActivity().findViewById(R.id.LimitedOffersRecyclerView);
                changeOffers.setNestedScrollingEnabled(false);
                HomeFragment.homeOfferAdapter = new OfferAdapter(getActivity(),productList,false);
                changeOffers.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                changeOffers.setAdapter(HomeFragment.homeOfferAdapter);
                HomeScreen.HomeScreenProgress.dismiss();
                dismiss();
            }
            private void handleSelectError(Throwable error)
            {
                HomeScreen.HomeScreenProgress.dismiss();
                if(error instanceof HttpException)
                {
                    Gson gson  = new GsonBuilder().create();
                    try
                    {
                        String errorBody = ((HttpException)error).response().errorBody().string();
                        Response response = gson.fromJson(errorBody,Response.class);
                        Snackbar.make(myView,getString(R.string.internal_server_error),Snackbar.LENGTH_SHORT).show();

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Snackbar.make(myView,getString(R.string.network_error),Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        myView.setMinimumWidth((int)(displayRect.width()*0.8f));
        return myView;
    }

}
