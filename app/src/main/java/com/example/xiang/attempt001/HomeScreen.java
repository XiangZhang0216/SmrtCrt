package com.example.xiang.attempt001;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnHotDealsViewPagerInteractionsListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,OnSearchViewPagerInteractionsListener {

    //For fragment use, use SupportMapFragment, for activity, use MapFragment
    SupportMapFragment sMapFragment;
    GoogleMap myMap;
    LocationRequest myLocationRequest;
    GoogleApiClient myGoogleApiClient;
    Location myLocation;
    public static Typeface FrontBold;
    public static final String TAG = HomeScreen.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String myToken;
    private static String myEmail;
    private static String myName;
    private static String myName2;
    private static User receivedUser;
    private CompositeSubscription subscription = new CompositeSubscription();
    public static boolean fromAnon;
    boolean revisiting;
    public static boolean fromFacebook;
    boolean loggedIn = true;
    SharedPreferences loggedInSharedPreferences;
    SharedPreferences currentName;
    static String fbName;
    static String fbLast;
    static String fbUrl;
    static Product[]homeScreenProductArray;
    static int serialL;
    public static int chainNow;
    public static int chainTarget;
    public static Product[] chainArray;
    public static String[] readytoChain;
    public static Product[] groupArray;
    public static Product scannedProductRelay;
    public static ProgressDialog HomeScreenProgress;
    public static View contenthomescreen;
    public static TextView cartCounter;
    public static int currentscrollersize;
    public static String currentCategory;
    public static int returncycle;
    public static View homeScreenShadow;
    private static int notificationTestId = 10172654;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        FrontBold = Typeface.createFromAsset(getAssets(),"Roboto-Medium.ttf");
        sMapFragment = SupportMapFragment.newInstance();
        receivedUser = new User();
        setContentView(R.layout.activity_home_screen);
        contenthomescreen=(View)findViewById(R.id.content_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeScreenShadow = (View)findViewById(R.id.HomeScreenShadow);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myToken = sharedPreferences.getString(NewLoginConstants.TOKEN,"");
        myEmail = sharedPreferences.getString(NewLoginConstants.EMAIL,"");


        String lanPref = sharedPreferences.getString("language","");
        if(lanPref.length()!=0) {
            Locale defaultLocale = new Locale(lanPref);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = defaultLocale;
            res.updateConfiguration(conf, dm);
        }

        Intent receiveGuest = getIntent();
        fromAnon = receiveGuest.getBooleanExtra("FromAnon",true);


        Bundle guestTest = getIntent().getExtras();
        if (guestTest!=null)
        {
            Log.i(TAG,"receiving bundle");
            fromAnon = guestTest.getBoolean("FromAnon");
            revisiting = receiveGuest.getBooleanExtra("Returning",false);
            fromFacebook = guestTest.getBoolean("FromFacebook");
            Serializable serialTest = guestTest.getSerializable("ProductsfromLogin");
            serialL = guestTest.getInt("ProductArrayLength");
            homeScreenProductArray = new Product[serialL];
            ProductDataSource.storedArray = new Product[serialL];
            for(int c = 0; c< serialL; c++)
            {
                homeScreenProductArray[c]=((Product[])serialTest)[c];
                ProductDataSource.storedArray[c]=((Product[])serialTest)[c];
            }

            if(!fromAnon&&!fromFacebook) {

                NotificationCompat.Builder introNotifyTest = new NotificationCompat.Builder(this);
                introNotifyTest.setAutoCancel(true);
                introNotifyTest.setSmallIcon(R.mipmap.ic_launcher);
                introNotifyTest.setTicker("SmrtCrt welcomes you.");
                introNotifyTest.setWhen(System.currentTimeMillis());
                introNotifyTest.setContentTitle("Welcome to SmrtCrt!");
                introNotifyTest.setContentText("Enjoy shopping whenever, wherever.");
                Intent notifyIntent = new Intent(this, HomeScreen.class);
                PendingIntent notifyPendingIntent  = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                introNotifyTest.setContentIntent(notifyPendingIntent);

                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(notificationTestId,introNotifyTest.build());


                if(revisiting)
                {
                    SharedPreferences revisitPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    myName2 = revisitPreferences.getString("CurrentName","Hahahano");
                }
                else {
                    Log.i(TAG, "sending subscription");
                    subscription.add(NetworkUtil.getRetrofit(myToken).getProfile(myEmail)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(this::handleResponse, this::handleError));
                }
            }
            if(fromFacebook)
            {
                SharedPreferences fbsession = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor fbEdit = fbsession.edit();
                fbEdit.putBoolean("FbSessionOngoing",true);
                fbEdit.apply();
                fbName = guestTest.getString("FacebookName");
               fbLast = guestTest.getString("FacebookSurn");
                fbUrl = guestTest.getString("FacebookURL");
                Toast.makeText(this,getString(R.string.welcome)+fbName+" "+fbLast+".",Toast.LENGTH_LONG).show();

            }
            else if(fromAnon)
            {
                Toast.makeText(this, R.string.welcome_guest_user, Toast.LENGTH_LONG).show();

            }
        }

            loggedInSharedPreferences = getSharedPreferences("LoggedInOrOut",0);
            SharedPreferences.Editor loggedEditor = loggedInSharedPreferences.edit();
            loggedEditor.clear();
            loggedEditor.putBoolean("InOut",loggedIn);
            loggedEditor.commit();
        logInEnter(loggedIn);

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(R.string.home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            HomeFragment hm = new HomeFragment();
            ft.replace(R.id.content_home_screen_frame, hm);
            ft.commit();
        }

        if(fromAnon) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setImageResource(R.drawable.join);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        CreateAccountDialog newDialogue = new CreateAccountDialog();
                        FragmentManager newDialogueFM = getSupportFragmentManager();
                        newDialogue.show(newDialogueFM, CreateAccountDialog.TAG);
                }
            });

        }
        else
        {FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.GONE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {}
            });
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sMapFragment.getMapAsync(this);
        Bundle StarPlatnium = getIntent().getExtras();
        if (StarPlatnium != null)
        {
            boolean Ora = StarPlatnium.getBoolean("Crazy Diamond");
            if (Ora)
            {
                android.support.v4.app.FragmentManager fmnearbystoresB = getSupportFragmentManager();
                if (!sMapFragment.isAdded()) {
                    fmnearbystoresB.beginTransaction().replace(R.id.map, sMapFragment).commit();
                } else {
                    fmnearbystoresB.beginTransaction().show(sMapFragment).commit();
                }
                getSupportActionBar().setTitle(R.string.stores_in_development);
                android.support.v4.app.FragmentManager fmB = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftB = fmB.beginTransaction();
                NearbyStoresFragment hmB = new NearbyStoresFragment();
                ftB.replace(R.id.content_home_screen_frame, hmB);
                ftB.commit();
            }
        }

        cartCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_cart));
        cartCounter.setTypeface(FrontBold);
        cartCounter.setTextColor(getResources().getColor(R.color.blue_light));
        cartCounter.setGravity(Gravity.CENTER_VERTICAL);
        String cartcontent = null;
        if(fromAnon)
        {
            cartcontent = sharedPreferences.getString("AnonRenewedCart",null);
        }
        if(!fromAnon&&!fromFacebook)
        {
            cartcontent = sharedPreferences.getString("RenewedCart",null);
        }

        if(cartcontent==null)
        {
            cartCounter.setText("0");
        }
        else
        {
            int comma = 0;
            if(cartcontent.length()==0)
            {
                cartCounter.setText("0");
            }
            else {

                for (int commacount = 0; commacount < cartcontent.length(); commacount++) {
                    if (cartcontent.charAt(commacount) == ',') {
                        comma++;
                    }
                }
                comma++;

                cartCounter.setText(""+comma);
            }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        Log.i(TAG,"OptionsMenuCreated");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        TextView userHeader = (TextView)findViewById(R.id.username_header);
        TextView emailHeader = (TextView)findViewById(R.id.email_header);
        CircleImageView profilePic = (CircleImageView)findViewById(R.id.CircleProfilePic);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                //startActivity(gotoSearch);
                homeScreenShadow.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle(R.string.product_search);
                android.support.v4.app.FragmentManager fmsearch = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftsearch = fmsearch.beginTransaction();
                SearchFragment fsearch = new SearchFragment().newInstances(query);
                ftsearch.replace(R.id.content_home_screen_frame, fsearch);
                ftsearch.commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });


if (fromAnon) {

    userHeader.setText(R.string.guest_user);
    emailHeader.setText(R.string.sign_up_to_experience_more_features);
}
        if(!fromAnon&&!fromFacebook)
        {
            String gotName;
            Log.i(TAG,"Writing name");
            if(revisiting)
            {
                SharedPreferences getCurrentName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                gotName = getCurrentName.getString("CurrentName","Fail");
            }
            else
            {
                gotName = receivedUser.getName();
            }

            if(ProductDataSource.returningUsername!=null)
            {
                userHeader.setText(ProductDataSource.returningUsername);
                Toast.makeText(this,getString(R.string.welcome)+ProductDataSource.returningUsername+".", Toast.LENGTH_LONG).show();
            }
            else
            {
                SharedPreferences getName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String n = getName.getString("ReturningName", getString(R.string.username_bug));
                userHeader.setText(n);
                Toast.makeText(this,getString(R.string.welcome)+n+".",Toast.LENGTH_LONG).show();
            }

            if (ProductDataSource.returningUserEmail!=null)
            {
                emailHeader.setText(ProductDataSource.returningUserEmail);
            }
            else
            {
                SharedPreferences getEmail = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String e = getEmail.getString("ReturningEmail",getString(R.string.this_is_a_visual_bug));
                emailHeader.setText(e);
            }

            SharedPreferences saveCurrentName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor currentNameEdit = saveCurrentName.edit();
            currentNameEdit.remove("CurrentName");
            currentNameEdit.putString("CurrentName", gotName);
            currentNameEdit.apply();

        }
        if(fromFacebook)
        {
            userHeader.setText(fbName + " " + fbLast);
            new DownloadFacebookImage(profilePic).execute(fbUrl);
            emailHeader.setText("");
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.qr_code_scan) {
            return true;
        }
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentManager fmnearbystores = getSupportFragmentManager();
        int id = item.getItemId();
        if (sMapFragment.isAdded()) {
            fmnearbystores.beginTransaction().hide(sMapFragment).commit();
        }
        if (id == R.id.nav_home) {
            homeScreenShadow.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle(R.string.home);
            android.support.v4.app.FragmentManager fmhome = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fthome = fmhome.beginTransaction();
            HomeFragment fhome = new HomeFragment();
            fthome.replace(R.id.content_home_screen_frame, fhome);
            fthome.commit();
        } else if (id == R.id.nav_hot_deals) {
            homeScreenShadow.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle(R.string.hot_deals);
            android.support.v4.app.FragmentManager fmhotdeals = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fthotdeals = fmhotdeals.beginTransaction();
            HotDealsFragment fhotdeals = new HotDealsFragment();
            fthotdeals.replace(R.id.content_home_screen_frame, fhotdeals);
            fthotdeals.commit();
        } else if (id == R.id.nav_nearby_stores) {

            Toast.makeText(this,getString(R.string.coming_in_future_updates),Toast.LENGTH_SHORT).show();

        }
            else if (id == R.id.nav_cart) {
            homeScreenShadow.setVisibility(View.VISIBLE);
            final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnected()) {


            String cartFetch;
            SharedPreferences getCartPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if(fromAnon)
            {
             cartFetch = getCartPref.getString("AnonRenewedCart","");
            }
            else
            {
                cartFetch = getCartPref.getString("RenewedCart","");
            }

            if((cartFetch==null)||(cartFetch.length()==0)||(cartFetch.length()==3))
            {
                getSupportActionBar().setTitle(R.string.my_cart);
                android.support.v4.app.FragmentManager fmcart = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ftcart = fmcart.beginTransaction();
                CartFragment fcart = new CartFragment();
                ftcart.replace(R.id.content_home_screen_frame, fcart);
                ftcart.commit();
            }
            if((cartFetch!=null)&&(cartFetch.length()>3))
            {
                HomeScreenProgress = new ProgressDialog(HomeScreen.this);
                HomeScreenProgress.setMessage(getString(R.string.loading_cart));
                HomeScreenProgress.setCancelable(false);
                HomeScreenProgress.show();
                int size = 0;
                for (int commacount = 0; commacount < cartFetch.length(); commacount++) {
                    if (cartFetch.charAt(commacount) == ',') {
                        size++;
                    }}
                size++;
                readytoChain = null;
                readytoChain = cartFetch.split(",",size);
                chainArray = new Product[size];
                chainNow = 0;
                chainTarget=size;

                   QueryCondition queryCondition = new QueryCondition();
                queryCondition.setConditionString("MG"+cartFetch);
                queryCondition.setReturncycleint(420);
                CompositeSubscription groupSub = new CompositeSubscription();
                groupSub.add(ProductNetworkUtil.getProductRetrofit("Token").getProductByCategory("MG"+cartFetch)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleGroupResponse, this::handleGroupError));
            }
            }
            else
            {
                Snackbar.make(findViewById(R.id.content_home_screen), R.string.internet_required_to_access_cart, Snackbar.LENGTH_SHORT).show();
            }



        } else if (id == R.id.nav_settings){
           // homeScreenShadow.setVisibility(View.VISIBLE);
            Intent settingTestIntent = new Intent(this, SettingsTest.class);
            settingTestIntent.putExtra("AnonSettingUser",fromAnon);
            if(!fromAnon&&!fromFacebook)
            {
                settingTestIntent.putExtra(NewLoginConstants.EMAIL,myEmail);
                settingTestIntent.putExtra(NewLoginConstants.TOKEN,myToken);
            }
            if(fromFacebook)
            {
                settingTestIntent.putExtra("FacebookSettingUser",fromFacebook);
            }
            startActivity(settingTestIntent);

        }
        else if (id == R.id.nav_camera)
        {
            Toast.makeText(this,getString(R.string.coming_in_future_updates),Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_gallery)
        {
            Toast.makeText(this,getString(R.string.coming_in_future_updates),Toast.LENGTH_SHORT).show();
        }
            else if (id == R.id.nav_profile){
            Toast.makeText(this,getString(R.string.coming_in_future_updates),Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.nav_signout)
        {

                    if(fromFacebook)
                    {
                        LoginManager.getInstance().logOut();
                    }
                    subscription.unsubscribe();
                    loggedIn = false;
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
                    currentCategory = null;
                    returncycle = 0;

                    logOutExit(loggedIn);
                    Intent i = new Intent(HomeScreen.this, NewLogin.class);
                    boolean CrazyDiamond = false;
                    i.putExtra("Crazy Diamond", CrazyDiamond);
                    startActivity(i);
                    HomeScreen.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //For QR Code Scanner
    public boolean startScanner(MenuItem view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setPrompt(getString(R.string.please_scan_a_smrtcrt_qr_code));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
        return true;
    }

    //For QR Code Scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.scanning_cancelled, Toast.LENGTH_LONG).show();
                } else {
                    String resultGetContentsString = result.getContents().toString();


                    if(  (resultGetContentsString.contains("catalog.directory/view/product/")) || (resultGetContentsString.contains("smrt-dash"))   )
                        {
                            String[] parts = resultGetContentsString.split("product/");
                            String fetchId = parts[1];

                            final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                            if(activeNetwork != null && activeNetwork.isConnected())
                            {
                                HomeScreenProgress = new ProgressDialog(HomeScreen.this);
                                HomeScreenProgress.setMessage(getString(R.string.processing));
                                HomeScreenProgress.setCancelable(false);
                                HomeScreenProgress.show();
                                myToken = "Placeholder";
                                String scanreq = "II"+fetchId;
                                String scantoken = "Token";
                                CompositeSubscription scanSubscription = new CompositeSubscription();
                                QueryCondition queryCondition = new QueryCondition();
                                queryCondition.setConditionString(scanreq);
                                queryCondition.setReturncycleint(15);
                                scanSubscription.add(ProductNetworkUtil.getProductRetrofit(scantoken).assortedProducts(scanreq)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(this::  handleScanResponse, this :: handleScanError));

                            }
                            else
                            {
                                Toast.makeText(this, R.string.internet_is_required_to_access_this_smrtcrt_product,Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(this, R.string.this_qr_code_does_not_belong_to_a_smrtcrt_product, Toast.LENGTH_LONG).show();
                        }

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

    }

    private void handleScanResponse(Product[] scannedProduct)
    {


        if (scannedProduct[0]!=null)
        {
            if(fromAnon)
            {
                SharedPreferences anonCartPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor anonEdit = anonCartPref.edit();
                String anonOldCart = anonCartPref.getString("AnonRenewedCart",null);
                if((anonOldCart==null)||(anonOldCart.length()==3))
                {
                    anonEdit.remove("AnonRenewedCart");
                    anonEdit.putString("AnonRenewedCart","");
                }

                String  cs = "";
                User anonUser = new User();

                int size =0;
                anonOldCart = anonCartPref.getString("AnonRenewedCart","");

                if (anonOldCart.length()!=0) {
                    for (int commacount = 0; commacount < anonOldCart.length(); commacount++) {
                        if (anonOldCart.charAt(commacount) == ',') {
                            size++;
                        }}
                    size++;
                    String[] parts = anonOldCart.split(",", size);
                    for (int i = 0; i < size; i++) {
                        anonUser.addToCart(parts[i],1);
                    }
                }
                anonUser.addToCart(scannedProduct[0].getId(),1);
                size = anonUser.getCartSize();

                for (int i = 0; i < size; i++) {

                    cs += anonUser.getCart()[i];
                    if (i < (size - 1)) {
                        cs += ",";
                    }
                }
                anonEdit.remove("AnonRenewedCart");
                anonEdit.putString("AnonRenewedCart", cs);
                anonEdit.apply();

                HomeScreenProgress.dismiss();

                Intent gotoDetails = new Intent (getApplicationContext(), OfferDetail.class);
                Bundle offerDetailsBundle = new Bundle();
                offerDetailsBundle.putSerializable("DetailObject",scannedProduct[0]);
                gotoDetails.putExtras(offerDetailsBundle);
                startActivity(gotoDetails);

                //Changing nagivation drawer number text
                String cartcontent = anonCartPref.getString("AnonRenewedCart",null);
                if(cartcontent==null)
                {
                    HomeScreen.cartCounter.setText("0");
                }
                else
                {
                    int comma = 0;
                    if(cartcontent.length()==0)
                    {
                        HomeScreen.cartCounter.setText("0");
                    }
                    else {

                        for (int commacount = 0; commacount < cartcontent.length(); commacount++) {
                            if (cartcontent.charAt(commacount) == ',') {
                                comma++;
                            }
                        }
                        comma++;

                        HomeScreen.cartCounter.setText(""+comma);
                    }
                }


                Toast.makeText(getApplicationContext(), R.string.guest_user_cart_updated,Toast.LENGTH_SHORT).show();
            }
            else{
                scannedProductRelay=new Product();
                scannedProductRelay=scannedProduct[0];
                SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor renewPrefEdit = renewPref.edit();
                String token = renewPref.getString(NewLoginConstants.TOKEN,"");
                String email = renewPref.getString(NewLoginConstants.EMAIL,"");
                String id = renewPref.getString("ReturningId","");
                String oldCart = renewPref.getString("RenewedCart",null);
                int size = 0;
                String cs = "";
                User user = new User();
                if (oldCart==null)
                {
                    renewPrefEdit.remove("RenewedCart");
                    renewPrefEdit.putString("RenewedCart","");
                }
                if(oldCart.length()!=0)
                {
                    for (int commacount = 0; commacount < oldCart.length(); commacount++) {
                        if (oldCart.charAt(commacount) == ',') {
                            size++;
                        }}
                    size++;
                    String[] parts = oldCart.split(",", size);
                    for (int i = 0; i < size; i++) {
                        user.addToCart(parts[i],1);
                    }
                }
                user.addToCart(scannedProduct[0].getId(),1);
                size = user.getCartSize();

                for (int i = 0; i < size; i++) {
                    cs += user.getCart()[i];
                    if (i < (size - 1)) {
                        cs += ",";
                    }
                }
                user.setDeveloperToolCode("cart");
                user.setCartstring(cs);
                user.setLookupid(id);
                CompositeSubscription cartSubscriptionTest = new CompositeSubscription();
                cartSubscriptionTest.add(NetworkUtil.getRetrofit(token).changePassword(email,user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleCartResponse, this::handleCartError));

            }

        }
        else
        {
            HomeScreenProgress.dismiss();
            Toast.makeText(this, R.string.something_went_wrong_please_try_again,Toast.LENGTH_LONG).show();
        }

    }
    private void handleScanError(Throwable error)
    {
        HomeScreenProgress.dismiss();
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Snackbar.make(findViewById(R.id.content_home_screen),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.content_home_screen),R.string.network_error,Snackbar.LENGTH_SHORT).show();
        }
    }
    private void handleCartResponse(Response response)
    {
        SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = renewPref.getString(NewLoginConstants.TOKEN,"");
        String email = renewPref.getString(NewLoginConstants.EMAIL,"");
        CompositeSubscription renewCartSub = new CompositeSubscription();
        renewCartSub.add(NetworkUtil.getRetrofit(token).getProfile(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleRenewResponse, this::handleCartError));
    }
    private void handleCartError(Throwable error)
    {

        HomeScreenProgress.dismiss();
        if(error instanceof HttpException)
        {
            Gson gson = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);

                Toast.makeText(getApplicationContext(),R.string.internal_server_error,Toast.LENGTH_SHORT).show();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_SHORT).show();
        }
    }
    private void handleRenewResponse(User renewedUser)
    {
        SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor renewEditor = renewPref.edit();
        renewEditor.remove("RenewedCart");
        renewEditor.putString("RenewedCart",renewedUser.getCartstring());
        renewEditor.apply();
        HomeScreenProgress.dismiss();
        Intent gotoDetails = new Intent (getApplicationContext(), OfferDetail.class);
        Bundle offerDetailsBundle = new Bundle();
        offerDetailsBundle.putSerializable("DetailObject",scannedProductRelay);
        gotoDetails.putExtras(offerDetailsBundle);
        startActivity(gotoDetails);

        //Changing nagivation drawer number text
        String cartcontent = renewPref.getString("RenewedCart",null);
        if(cartcontent==null)
        {
            HomeScreen.cartCounter.setText("0");
        }
        else
        {
            int comma = 0;
            if(cartcontent.length()==0)
            {
                HomeScreen.cartCounter.setText("0");
            }
            else {

                for (int commacount = 0; commacount < cartcontent.length(); commacount++) {
                    if (cartcontent.charAt(commacount) == ',') {
                        comma++;
                    }
                }
                comma++;

                HomeScreen.cartCounter.setText(""+comma);
            }
        }
        Toast.makeText(getApplicationContext(), R.string.cart_updated,Toast.LENGTH_SHORT).show();

    }

    //For Tabs in Hot Deals
    @Override
    public void onFragmentMessage(int TAG, String data) {

    }

    //For Google Maps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Do map stuff here.
        myMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                buildGoogleApiClient();
                myMap.setMyLocationEnabled(true);
                Toast.makeText(this, R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
                Toast.makeText(this, R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
            }
            else
            {
                checkLocationPermission();
            }
        }
        else
        {
            buildGoogleApiClient();
            myMap.setMyLocationEnabled(true);
            Toast.makeText(this, R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
            Toast.makeText(this, R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
        }
    }

    private void buildGoogleApiClient() {

        myGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        myGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);
        myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(1000);
        myLocationRequest.setFastestInterval(1000);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    @Override
    public void onLocationChanged(Location location) {

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.location_permission_needed)
                        .setMessage(R.string.your_location_can_help_us_find_the_stores_near_you)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                    }
                                }).create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(myGoogleApiClient!= null)
                        {
                            buildGoogleApiClient();
                        }
                        myMap.setMyLocationEnabled(true);
                        //This is where the app is run the first time ever on the device.
                        Toast.makeText(this,R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
                        Toast.makeText(this, R.string.please_make_sure_your_internet_connection_and_location_setting_are_turned_on, Toast.LENGTH_LONG).show();
                        Intent buffer = new Intent (HomeScreen.this, MapBufferActivity.class);
                        startActivity(buffer);
                    }
                }
                else
                {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private void handleResponse(User user)
    {
        Log.i(TAG,"handleResponse");
        myEmail = user.getEmail();
        receivedUser = user;
        receivedUser.setName(user.getName());
        receivedUser.setLookupid(user.getId());
        receivedUser.setCartstring(user.getCartstring());
        SharedPreferences cartsuggestionPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor cartsuggestEdit = cartsuggestionPref.edit();
        cartsuggestEdit.remove("RenewedCart");
        cartsuggestEdit.remove("RenewedSuggestions");
        cartsuggestEdit.putString("RenewedCart",user.getCartstring());
        cartsuggestEdit.putString("RenewedSuggestions",user.getSuggestions());
        cartsuggestEdit.apply();


    }
    private void handleError(Throwable error)
    {
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Snackbar.make(findViewById(R.id.content_home_screen),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.content_home_screen),R.string.network_error,Snackbar.LENGTH_SHORT).show();
        }
    }

    private void handleGroupResponse(Product[] group)
    {
        int stop=0;
        groupArray = null;
        groupArray = new Product[group.length];
        for(int g = 0; g<group.length;g++)
        {
            groupArray[g]=group[g];
        }
        HomeScreenProgress.dismiss();
        getSupportActionBar().setTitle("My Cart");
        android.support.v4.app.FragmentManager fmcart = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ftcart = fmcart.beginTransaction();
        CartFragment fcart = new CartFragment();
        ftcart.replace(R.id.content_home_screen_frame, fcart);
        ftcart.commit();
    }
    private void handleGroupError(Throwable error)
    {
        HomeScreenProgress.dismiss();
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Snackbar.make(findViewById(R.id.content_home_screen),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(findViewById(R.id.content_home_screen),R.string.network_error,Snackbar.LENGTH_SHORT).show();
        }
    }


    public class DownloadFacebookImage extends AsyncTask<String, Void, Bitmap>
    {
        CircleImageView bmImage;
        public DownloadFacebookImage(CircleImageView bmImage)
        {
            this.bmImage = bmImage;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIconll = null;
            try
            {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIconll = BitmapFactory.decodeStream(in);
            }
            catch(Exception  e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIconll;
        }
        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
     synchronized void logInEnter(boolean enterValue)
    {
        SharedPreferences loggedInSharedPreferences;
        loggedInSharedPreferences = getSharedPreferences("LoggedInOrOut", 0);
        SharedPreferences.Editor loggedEditor = loggedInSharedPreferences.edit();
        loggedEditor.clear();
        loggedEditor.putBoolean("InOut",enterValue);
        loggedEditor.commit();

    }
    synchronized void logOutExit(boolean exitValue)
    {
        SharedPreferences loggedInSharedPreferencesExiting;
        loggedInSharedPreferencesExiting = getSharedPreferences("LoggedInOrOut",0);
        SharedPreferences.Editor loggedEditorExiting = loggedInSharedPreferencesExiting.edit();
        loggedEditorExiting.clear();
        loggedEditorExiting.putBoolean("InOut",exitValue);
        loggedEditorExiting.commit();

    }

    public void gotoWebsite(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smrtcrt.com/"));
        startActivity(browserIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInEnter(loggedIn);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onSearchFragmentMessage(int TAG, String data) {

    }
}
