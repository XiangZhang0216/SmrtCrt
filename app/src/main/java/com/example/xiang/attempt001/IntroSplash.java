package com.example.xiang.attempt001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Locale;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-05-07.
 */

public class IntroSplash extends AppCompatActivity{
    private static final int DELAY = 1000;
    boolean InOrOut;
    SharedPreferences loggedSharedPreferencesReceived;
    private SharedPreferences loginInfoSharedPreferences;
    private SharedPreferences loginBackPreferences;
    private CompositeSubscription loggingBackInSubscription;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_intro);
        TextView introText = (TextView)findViewById(R.id.introTitle);
        introText.setTypeface(Typeface.createFromAsset(getAssets(),"SourceSansPro-Bold.ttf"));
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
        loggingBackInSubscription = new CompositeSubscription();
        loggedSharedPreferencesReceived = getSharedPreferences("LoggedInOrOut", 0);
        InOrOut = loggedSharedPreferencesReceived.getBoolean("InOut",false);

        if(InOrOut)
        {
            loginInfoSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String PreferredEmail = loginInfoSharedPreferences.getString("PreferredEmail","");
            String PreferredPassword = loginInfoSharedPreferences.getString("PreferredPassword","");
            if (PreferredEmail == "")
            {
                Boolean fbSessionCheck = loginInfoSharedPreferences.getBoolean("FbSessionOngoing",false);
                if (fbSessionCheck)
                {
                    LoginManager.getInstance().logOut();
                    SharedPreferences.Editor fbEdit = loginInfoSharedPreferences.edit();
                    fbEdit.putBoolean("FbSessionOngoing",false);
                    fbEdit.apply();
                    new Handler().postDelayed(new Runnable(){

                        @Override
                        public void run()
                        {
                            Intent i = new Intent(IntroSplash.this, NewLogin.class);
                            boolean CrazyDiamond = false;
                            i.putExtra("Crazy Diamond", CrazyDiamond);
                            startActivity(i);
                            IntroSplash.this.finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                    },DELAY);
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent j = new Intent(IntroSplash.this, HomeScreen.class);
                            boolean guest = true;
                            j.putExtra("FromAnon", guest);
                            startActivity(j);
                            IntroSplash.this.finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }, DELAY);
                }
            }
            else
            {
                loggingBackInSubscription.add(NetworkUtil.getRetrofit(PreferredEmail,PreferredPassword).login()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError));

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent (IntroSplash.this, HomeScreen.class);
                        boolean guest = false;
                        intent.putExtra("FromAnon",guest);
                        intent.putExtra("Returning",true);
                        startActivity(intent);
                        IntroSplash.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                },DELAY);
            }

        }

        else
        {
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run()
                {
                    Intent i = new Intent(IntroSplash.this, NewLogin.class);
                    boolean CrazyDiamond = false;
                    i.putExtra("Crazy Diamond", CrazyDiamond);
                    startActivity(i);
                    IntroSplash.this.finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            },DELAY);
        }


    }//End of onCreate

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private void handleResponse(Response response)
    {
        loginBackPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor backEditor = loginBackPreferences.edit();
        backEditor.putString(NewLoginConstants.TOKEN,response.getToken());
        backEditor.putString(NewLoginConstants.EMAIL,response.getMessage());
        backEditor.apply();
    }
    private void handleError(Throwable error)
    {
        if(error instanceof HttpException)
        {
            Gson gson = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                if(response.getMessage().contains("User"))
                {
                    Snackbar.make(findViewById(R.id.introView), R.string.user_not_found,Snackbar.LENGTH_SHORT).show();
                }
                else if(response.getMessage().contains("Invalid"))
                {
                    Snackbar.make(findViewById(R.id.introView), R.string.invalid_credentials,Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.introView),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.introView),R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
    }

}//End of AppCompatActivity
