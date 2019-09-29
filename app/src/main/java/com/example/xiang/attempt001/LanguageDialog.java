package com.example.xiang.attempt001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import java.util.Locale;

/**
 * Created by xiang on 2017-09-02.
 */

public class LanguageDialog extends android.app.DialogFragment {
    View myView;
    public static final String TAG = LanguageDialog.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Rect displayRect = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRect);
        myView = inflater.inflate(R.layout.dialog_language, container, false);
        SharedPreferences languagePreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor languageEditor = languagePreference.edit();
        RadioGroup languageGroup = (RadioGroup) myView.findViewById(R.id.LanguageRadioGroup);
        languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.languageEN:
                        Locale englishLocale = new Locale("en");
                        languageEditor.putString("language","en");
                        languageEditor.apply();
                        Resources enres = getResources();
                        DisplayMetrics endm = enres.getDisplayMetrics();
                        Configuration enconf = enres.getConfiguration();
                        enconf.locale = englishLocale;
                        enres.updateConfiguration(enconf,endm);


                        break;
                    case R.id.languageFR:
                        Locale frenchLocale = new Locale("fr");
                        languageEditor.putString("language","fr");
                        languageEditor.apply();
                        Resources frres = getResources();
                        DisplayMetrics frdm = frres.getDisplayMetrics();
                        Configuration frconf = frres.getConfiguration();
                        frconf.locale = frenchLocale;
                        frres.updateConfiguration(frconf,frdm);
                        break;

                    case R.id.languageIT:
                        Locale italianLocale = new Locale("it");
                        languageEditor.putString("language","it");
                        languageEditor.apply();
                        Resources itres = getResources();
                        DisplayMetrics itdm = itres.getDisplayMetrics();
                        Configuration itconf = itres.getConfiguration();
                        itconf.locale = italianLocale;
                        itres.updateConfiguration(itconf,itdm);

                        break;

                    case R.id.languageZH:
                        Locale chineseLocale = new Locale("zh");
                        languageEditor.putString("language","zh");
                        languageEditor.apply();
                        Resources zhres = getResources();
                        DisplayMetrics zhdm = zhres.getDisplayMetrics();
                        Configuration zhconf = zhres.getConfiguration();
                        zhconf.locale = chineseLocale;
                        zhres.updateConfiguration(zhconf,zhdm);

                        break;

                    default:
                        Locale defaultLocale = new Locale("en");
                        languageEditor.putString("language","en");
                        languageEditor.apply();
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = defaultLocale;
                        res.updateConfiguration(conf,dm);

                        break;
                }


                Intent refresh = new Intent(myView.getContext(),NewLogin.class);
                startActivity(refresh);
                getActivity().finish();


            }
        });
        myView.setMinimumWidth((int)(displayRect.width()*0.8f));
        return myView;
    }
}

