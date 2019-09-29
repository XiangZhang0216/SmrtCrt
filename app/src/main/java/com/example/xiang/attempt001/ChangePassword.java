package com.example.xiang.attempt001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import java.util.Locale;


public class ChangePassword extends AppCompatActivity implements ChangePasswordFragment.Listener{
    public static ChangePasswordFragment newChangePasswordFragment;
    public static String receivedEmail;
    public static String receivedToken;
    public static Bundle savey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savey = new Bundle();
        savey = savedInstanceState;
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
        setContentView(R.layout.activity_change_password);


        receivedEmail = getIntent().getStringExtra(NewLoginConstants.EMAIL);
        receivedToken = getIntent().getStringExtra(NewLoginConstants.TOKEN);


        if(savedInstanceState == null)
        {
                Bundle bundle = new Bundle();
                bundle.putString(NewLoginConstants.EMAIL,receivedEmail);
                bundle.putString(NewLoginConstants.TOKEN,receivedToken);
                FragmentManager fmChange = getSupportFragmentManager();
                FragmentTransaction ftChange = fmChange.beginTransaction();
                 newChangePasswordFragment = new ChangePasswordFragment();
                newChangePasswordFragment.setArguments(bundle);
                ftChange.replace(R.id.ChangePasswordFrame,newChangePasswordFragment);
                ftChange.commit();

        }
    }
    @Override
    public void onPasswordChanged()
    {

        newChangePasswordFragment.changeMessage.setTextColor(getResources().getColor(R.color.green_light));
        newChangePasswordFragment.changeMessage.setText(getString(R.string.password_changed_successfully));
        newChangePasswordFragment.changeMessage.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences nameClear = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor nameClearEdit = nameClear.edit();
                nameClearEdit.remove("CurrentName");
                nameClearEdit.remove("ReturningName");
                nameClearEdit.remove("ReturningEmail");
                nameClearEdit.remove("ReturningId");
                nameClearEdit.remove("RenewedCart");
                nameClearEdit.remove("RenewedSuggestions");
                nameClearEdit.apply();
                ProductDataSource.returningUsername = null;
                ProductDataSource.returningUserEmail = null;
                ProductDataSource.returningUserId = null;

                SharedPreferences loggedInSharedPreferencesExiting;
                loggedInSharedPreferencesExiting = getSharedPreferences("LoggedInOrOut",0);
                SharedPreferences.Editor loggedEditorExiting = loggedInSharedPreferencesExiting.edit();
                loggedEditorExiting.clear();
                loggedEditorExiting.putBoolean("InOut",false);
                loggedEditorExiting.commit();

                Intent i = new Intent(ChangePassword.this, NewLogin.class);
                boolean CrazyDiamond = false;
                i.putExtra("Crazy Diamond", CrazyDiamond);
                startActivity(i);
                ChangePassword.this.finish();
            }
        }, 1800);
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
        setContentView(R.layout.activity_change_password);
        receivedEmail = getIntent().getStringExtra(NewLoginConstants.EMAIL);
        receivedToken = getIntent().getStringExtra(NewLoginConstants.TOKEN);

                Bundle bundle = new Bundle();
                bundle.putString(NewLoginConstants.EMAIL, receivedEmail);
                bundle.putString(NewLoginConstants.TOKEN, receivedToken);
                FragmentManager fmChange = getSupportFragmentManager();
                FragmentTransaction ftChange = fmChange.beginTransaction();
                newChangePasswordFragment = new ChangePasswordFragment();
                newChangePasswordFragment.setArguments(bundle);
                ftChange.replace(R.id.ChangePasswordFrame, newChangePasswordFragment);
                ftChange.commit();

    }
}
