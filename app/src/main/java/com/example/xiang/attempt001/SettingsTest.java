package com.example.xiang.attempt001;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;


import java.util.List;
import java.util.Locale;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsTest extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
     static boolean fromAnon;
    static boolean fromFacebook;
    static boolean holdit2;
    static boolean fbholdit2;
    static String email1;
    static String token1;
    static String email2;
    static String token2;



    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {

                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);

            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();
        getSupportActionBar().setTitle(getString(R.string.settings));


       fromAnon =getIntent().getBooleanExtra("AnonSettingUser",true);
        fromFacebook = getIntent().getBooleanExtra("FacebookSettingUser",false);
        if(!fromAnon&&!fromFacebook)
        {
            token1= getIntent().getStringExtra(NewLoginConstants.TOKEN);
            email1=getIntent().getStringExtra(NewLoginConstants.EMAIL);
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {

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
        boolean holdit = fromAnon;
        holdit2 = holdit;
        boolean fbholdit = fromFacebook;
        fbholdit2 = fbholdit;
        if(!holdit2&&!fbholdit2)
        {
            email2=email1;
            token2 = token1;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(true);

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {

       return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || AccountPreferenceFragment.class.getName().equals(fragmentName)
                || LanguagePreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsTest.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsTest.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsTest.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows account preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AccountPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((AppCompatPreferenceActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_header_account));
            addPreferencesFromResource(R.xml.pref_account);
            Preference changePassword = (Preference)findPreference("change_password_pref");
            changePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                 if(HomeScreen.fromAnon)
                    {
                        Snackbar.make(getView(), R.string.guest_users_do_not_have_passwords, Snackbar.LENGTH_SHORT).show();
                    }
                    if (HomeScreen.fromFacebook)
                    {
                        Snackbar.make(getView(), R.string.facebook_users_do_not_have_passwords, Snackbar.LENGTH_SHORT).show();
                    }
                    if(!HomeScreen.fromAnon&&!HomeScreen.fromFacebook){
                        Intent i = new Intent(getActivity(),ChangePassword.class);
                        i.putExtra(NewLoginConstants.EMAIL,email2);
                        i.putExtra(NewLoginConstants.TOKEN,token2);
                        startActivity(i);

                    }
                    return false;
                }
            });
            setHasOptionsMenu(true);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsTest.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }


    /**
     * This fragment shows language preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LanguagePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((AppCompatPreferenceActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_header_language));
            addPreferencesFromResource(R.xml.pref_language);

            SharedPreferences languagePreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor languageEditor = languagePreference.edit();
            Preference changeLanguageEnglish = (Preference)findPreference("change_language_pref_en");
            changeLanguageEnglish.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    Locale englishLocale = new Locale("en");
                    languageEditor.putString("language","en");
                    languageEditor.apply();
                    Resources enres = getResources();
                    DisplayMetrics endm = enres.getDisplayMetrics();
                    Configuration enconf = enres.getConfiguration();
                    enconf.locale = englishLocale;
                    enres.updateConfiguration(enconf,endm);
                    if(holdit2)
                    {
                        Intent j = new Intent(getActivity(), HomeScreen.class);
                        boolean guest = true;
                        j.putExtra("FromAnon", guest);
                        startActivity(j);
                        getActivity().finish();
                    }

                    if(!holdit2&&!fbholdit2){
                        Intent intent = new Intent (getActivity(), HomeScreen.class);
                        boolean guest = false;
                        intent.putExtra("FromAnon",guest);
                        intent.putExtra("Returning",true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    return false;
                }
            });

            Preference changeLanguageFrench = (Preference)findPreference("change_language_pref_fr");
            changeLanguageFrench.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    Locale frenchLocale = new Locale("fr");
                    languageEditor.putString("language","fr");
                    languageEditor.apply();
                    Resources frres = getResources();
                    DisplayMetrics frdm = frres.getDisplayMetrics();
                    Configuration frconf = frres.getConfiguration();
                    frconf.locale = frenchLocale;
                    frres.updateConfiguration(frconf,frdm);
                    if(holdit2)
                    {
                        Intent j = new Intent(getActivity(), HomeScreen.class);
                        boolean guest = true;
                        j.putExtra("FromAnon", guest);
                        startActivity(j);
                        getActivity().finish();
                    }

                    if(!holdit2&&!fbholdit2){
                        Intent intent = new Intent (getActivity(), HomeScreen.class);
                        boolean guest = false;
                        intent.putExtra("FromAnon",guest);
                        intent.putExtra("Returning",true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    return false;
                }
            });

            Preference changeLanguageItalian = (Preference)findPreference("change_language_pref_it");
            changeLanguageItalian.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    Locale italianLocale = new Locale("it");
                    languageEditor.putString("language","it");
                    languageEditor.apply();
                    Resources itres = getResources();
                    DisplayMetrics itdm = itres.getDisplayMetrics();
                    Configuration itconf = itres.getConfiguration();
                    itconf.locale = italianLocale;
                    itres.updateConfiguration(itconf,itdm);
                    if(holdit2)
                    {
                        Intent j = new Intent(getActivity(), HomeScreen.class);
                        boolean guest = true;
                        j.putExtra("FromAnon", guest);
                        startActivity(j);
                        getActivity().finish();
                    }

                    if(!holdit2&&!fbholdit2){
                        Intent intent = new Intent (getActivity(), HomeScreen.class);
                        boolean guest = false;
                        intent.putExtra("FromAnon",guest);
                        intent.putExtra("Returning",true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    return false;
                }
            });

            Preference changeLanguageChinese = (Preference)findPreference("change_language_pref_zh");
            changeLanguageChinese.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    Locale chineseLocale = new Locale("zh");
                    languageEditor.putString("language","zh");
                    languageEditor.apply();
                    Resources zhres = getResources();
                    DisplayMetrics zhdm = zhres.getDisplayMetrics();
                    Configuration zhconf = zhres.getConfiguration();
                    zhconf.locale = chineseLocale;
                    zhres.updateConfiguration(zhconf,zhdm);
                    if(holdit2)
                    {
                        Intent j = new Intent(getActivity(), HomeScreen.class);
                        boolean guest = true;
                        j.putExtra("FromAnon", guest);
                        startActivity(j);
                        getActivity().finish();
                    }

                    if(!holdit2&&!fbholdit2){
                        Intent intent = new Intent (getActivity(), HomeScreen.class);
                        boolean guest = false;
                        intent.putExtra("FromAnon",guest);
                        intent.putExtra("Returning",true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    return false;
                }
            });
            setHasOptionsMenu(true);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsTest.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


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

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
