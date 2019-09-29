package com.example.xiang.attempt001;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import java.util.Locale;


public class PasswordRetrieveActivity extends AppCompatActivity implements PasswordRetrieveFragment.Listener{

    private PasswordRetrieveFragment passwordRetrieveFragment;


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
        setContentView(R.layout.activity_password_retrieve);
        if(savedInstanceState==null)
        {
            if (passwordRetrieveFragment==null)
            {
                android.support.v4.app.FragmentManager fmRetrieve = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftRetrieve = fmRetrieve.beginTransaction();
                PasswordRetrieveFragment newPasswordRetrieveFragment = new PasswordRetrieveFragment();
                ftRetrieve.replace(R.id.PasswordRetrieveFrame,newPasswordRetrieveFragment);
                ftRetrieve.commit();
            }
        }

    }

    @Override
    public void onPasswordReset(String message) {
        Snackbar.make(findViewById(R.id.activity_password_retrieve),message,Snackbar.LENGTH_SHORT).show();
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

        setContentView(R.layout.activity_password_retrieve);

            if (passwordRetrieveFragment==null)
            {
                android.support.v4.app.FragmentManager fmRetrieve = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftRetrieve = fmRetrieve.beginTransaction();
                PasswordRetrieveFragment newPasswordRetrieveFragment = new PasswordRetrieveFragment();
                ftRetrieve.replace(R.id.PasswordRetrieveFrame,newPasswordRetrieveFragment);
                ftRetrieve.commit();
            }

    }
}
