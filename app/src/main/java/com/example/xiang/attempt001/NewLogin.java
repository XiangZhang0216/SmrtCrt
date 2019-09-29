package com.example.xiang.attempt001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Locale;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NewLogin extends AppCompatActivity {
    private NewLoginFragment newLoginFragment;
    private SharedPreferences loginInfoSharedPreferences;
    private Product[] anonProductArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences languagePreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lanPref = languagePreference.getString("language","");
        if(lanPref.length()!=0) {
            Locale defaultLocale = new Locale(lanPref);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = defaultLocale;
            res.updateConfiguration(conf, dm);
        }
        setContentView(R.layout.activity_new_login);


        if(savedInstanceState==null)
        {
            if (newLoginFragment==null)
            {
                android.support.v4.app.FragmentManager fmLogin = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftLogin = fmLogin.beginTransaction();
                NewLoginFragment newLoginFragment = new NewLoginFragment();
                ftLogin.replace(R.id.loginFragmentFrame, newLoginFragment);
                ftLogin.commit();
            }
        }

    }

    public void enterBackDoor(View view)
    {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected())
        {
            NewLoginFragment.anonProgress.setProgressStyle(R.style.AppCompatAlertDialogStyle);
            NewLoginFragment.anonProgress.setMessage(getString(R.string.logging_in_as_guest_user));
            NewLoginFragment.anonProgress.setCancelable(false);
            NewLoginFragment.anonProgress.show();
            CompositeSubscription anonListSubscription = new CompositeSubscription();
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setConditionString("MU");
            queryCondition.setReturncycleint(123);
            anonListSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").getProductByCategory("MU")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleAnonProductListResponse, this::handleProductListError));

        }
        else {

            Snackbar.make(findViewById(R.id.loginFragmentFrame), R.string.no_internet, Snackbar.LENGTH_SHORT).show();

        }


    }
    private void handleAnonProductListResponse(Product[] productList)
    {

        anonProductArray = new Product[productList.length];
        for(int pArr = 0; pArr<productList.length; pArr++)
        {
            anonProductArray[pArr] = productList[pArr];
        }

        ProductDataSource.storedArray = new Product[anonProductArray.length];
        for(int c = 0; c< anonProductArray.length; c++)
        {
            ProductDataSource.storedArray[c]=anonProductArray[c];
        }

        loginInfoSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor loginInfoEditor = loginInfoSharedPreferences.edit();
        loginInfoEditor.putString("PreferredEmail", "");
        loginInfoEditor.putString("PreferredPassword","");
        loginInfoEditor.apply();
        Intent i = new Intent(this, HomeScreen.class);
        boolean guest = true;
        i.putExtra("FromAnon",guest);
        i.putExtra("FromFacebook",false);
        i.putExtra("ProductsfromLogin",anonProductArray);
        i.putExtra("ProductArrayLength",anonProductArray.length);
        startActivity(i);
        NewLogin.this.finish();


    }
    private void handleUserProductListResponse(Product[] userProductList)
    {


            anonProductArray = new Product[userProductList.length];
            for(int pArr = 0; pArr<userProductList.length; pArr++)
            {
                anonProductArray[pArr] = userProductList[pArr];
            }
        ProductDataSource.storedArray = new Product[anonProductArray.length];
        for(int c = 0; c< anonProductArray.length; c++)
        {
            ProductDataSource.storedArray[c]=anonProductArray[c];
        }

        NewLoginFragment.anonProgress.dismiss();

        LogInDialog logInDialog = new LogInDialog();
        FragmentManager newLogInDialogueFM = getSupportFragmentManager();
        logInDialog.show(newLogInDialogueFM, LogInDialog.TAG);


    }
    private void handleProductListError(Throwable error)
    {
        NewLoginFragment.anonProgress.dismiss();
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Snackbar.make(findViewById(R.id.loginFragmentFrame),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.loginFragmentFrame),R.string.network_error,Snackbar.LENGTH_SHORT).show();
        }
    }

    public void logInDialogue (View view)
    {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected())
        {
            NewLoginFragment.anonProgress.setProgressStyle(R.style.AppCompatAlertDialogStyle);
            NewLoginFragment.anonProgress.setMessage(getString(R.string.preparing_sign_in));
            NewLoginFragment.anonProgress.setCancelable(false);
            NewLoginFragment.anonProgress.show();
            CompositeSubscription userListSubscription = new CompositeSubscription();
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setConditionString("MU");
            queryCondition.setReturncycleint(456);
            userListSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").getProductByCategory("MU")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleUserProductListResponse, this::handleProductListError));

        }
        else
        {
            Snackbar.make(findViewById(R.id.loginFragmentFrame), R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }
    }
    public void loginChangeLanguage(View view)
    {
        LanguageDialog languageDialog = new LanguageDialog();
        android.app.FragmentManager selectDialogFM = getFragmentManager();
        languageDialog.show(selectDialogFM,SelectDialog.TAG);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SharedPreferences languagePreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lanPref = languagePreference.getString("language","");
        if(lanPref.length()!=0) {
            Locale defaultLocale = new Locale(lanPref);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = defaultLocale;
            res.updateConfiguration(conf, dm);
        }
        setContentView(R.layout.activity_new_login);


            if (newLoginFragment==null)
            {
                android.support.v4.app.FragmentManager fmLogin = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftLogin = fmLogin.beginTransaction();
                NewLoginFragment newLoginFragment = new NewLoginFragment();
                ftLogin.replace(R.id.loginFragmentFrame, newLoginFragment);
                ftLogin.commit();

            }

    }
    @Override
   public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
