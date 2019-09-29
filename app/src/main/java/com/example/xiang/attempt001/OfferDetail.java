package com.example.xiang.attempt001;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class OfferDetail extends AppCompatActivity {
    Product product;
    Product[] relatedProductsArray;
    String[] allWords;
    int keyWordMidPoint;
    int keyWordUpCounter;
    RecyclerView relatedItemsRecyclerView;
    RelatedItemsDataAdapter relatedItemsDataAdapter;
    TextView offerQtyInCart;
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
        setContentView(R.layout.activity_offer_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.offer_detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.offer_detail_appbar_layout);
        product =(Product) (getIntent().getExtras().getSerializable("DetailObject"));
        ProgressBar offerProgress = (ProgressBar)findViewById(R.id.offerdetailprogress);
        ImageView offerImage = (ImageView)findViewById(R.id.OfferDetailImage);
        new DownloadImageTask(offerImage,offerProgress).execute(product.getImageLink());
        Button share = (Button)findViewById(R.id.shareProduct);
        Button wishlist = (Button)findViewById(R.id.addToWishList);
        Button add = (Button)findViewById(R.id.addProduct);
        Button buynow = (Button)findViewById(R.id.buyNow);
        Button compare = (Button)findViewById(R.id.compareProduct);
        buttonEffect(share);
        buttonEffect(add);
        SharedPreferences cartPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = cartPref.getString(NewLoginConstants.TOKEN,"");
        String email = cartPref.getString(NewLoginConstants.EMAIL,"");
        String id = cartPref.getString("ReturningId","");

        //Here are the contents of details page
        TextView offerNameDisplayed = (TextView)findViewById(R.id.OfferDetailName);
        offerNameDisplayed.setText(product.getItemName());
        TextView offerPriceDisplayed = (TextView)findViewById(R.id.OfferPrice);
        offerPriceDisplayed.setText("$"+product.getPrice());
        TextView offerSummaryDisplayed = (TextView)findViewById(R.id.OfferSummary);
        offerSummaryDisplayed.setText(product.getSummary());
        Button gogoduckRedirectButton = (Button)findViewById(R.id.gogoduckredirectbutton);
        gogoduckRedirectButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String redirectsite = "http://catalog.directory/products/search/"+product.getId();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectsite));
                startActivity(browserIntent);
            }
        });

        TextView offerDescriptionDisplayed = (TextView)findViewById(R.id.OfferDescription);
        offerDescriptionDisplayed.setText(product.getDescription());

        TextView offerColor = (TextView)findViewById(R.id.OfferColor);
        offerColor.setText(product.getColor());

        TextView offerCondition = (TextView)findViewById(R.id.OfferCondition);
        offerCondition.setText(product.getCondition());

        TextView offerAvailability = (TextView)findViewById(R.id.OfferAvailability);
        offerAvailability.setText(product.getAvailability());

        TextView offerAge = (TextView)findViewById(R.id.OfferAgeGroup);
        offerAge.setText(product.getAgeGroup());

        TextView offerBrand = (TextView)findViewById(R.id.OfferBrand);
        offerBrand.setText(product.getBrand());

        TextView offerGender = (TextView)findViewById(R.id.OfferGender);
        offerGender.setText(product.getGender());

        TextView offerProductType = (TextView)findViewById(R.id.OfferProductType);
        offerProductType.setText(product.getProductType());

        offerQtyInCart = (TextView)findViewById(R.id.OfferInCartQty);
        relatedProductsArray = new Product[0];
        relatedItemsRecyclerView = (RecyclerView)findViewById(R.id.offerDetailSquareBar);

        relatedItemsDataAdapter = new RelatedItemsDataAdapter(this, relatedProductsArray, (View) findViewById(R.id.content_offer_detail));
        relatedItemsRecyclerView.setHasFixedSize(false);
        relatedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedItemsRecyclerView.setAdapter(relatedItemsDataAdapter);
        relatedItemsRecyclerView.setNestedScrollingEnabled(false);
        String[]allNameKeywords = new String[0];
        if(product.getItemName()!=null) {
            allNameKeywords = product.getItemName().split(" ");
            if (allNameKeywords.length > 0) {
                keyWordMidPoint = allNameKeywords.length - 1;
            } else {
                keyWordMidPoint = 0;
            }
        }
        String[] allCategoryKeywords = new String[0];
        if (product.getCategory()!=null) {
            if (product.getCategory().contains(">")) {
                allCategoryKeywords = product.getCategory().split(" > ");
            } else {
                allCategoryKeywords = new String[1];
                allCategoryKeywords[0] = product.getCategory();
            }
        }

        ArrayUtils.reverse(allCategoryKeywords);
        allWords = new String[0];
        allWords = ArrayUtils.addAll(allNameKeywords,allCategoryKeywords);
        keyWordUpCounter = 0;

        final ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()) {

            CompositeSubscription relatedSubscription = new CompositeSubscription();
            relatedSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts("MFn"+allWords[0])
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this:: handleRelatedResponse, this::handleRelatedError));

        }
        else
        {

        }

        share.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), R.string.coming_in_future_updates, Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = getString(R.string.take_a_look_at_this_smrtcrt_product)+ " " +
                        ""+product.getItemName() +" " +
                        ""+ getString(R.string.website) + "http://catalog.directory/view/product/" + product.getId();
                String shareSub = getString(R.string.smrtcrt_product);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(shareIntent,getString(R.string.share_using)));
            }
        });
        wishlist.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.coming_in_future_updates, Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (HomeScreen.fromAnon)
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
                   int size =0;// = user.getCartSize();
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
                    anonUser.addToCart(product.getId(),1);
                    size = anonUser.getCartSize();

                        for (int i = 0; i < size; i++) {
                            //prepCartString[i]=user.getCart()[i];
                            cs += anonUser.getCart()[i];
                            if (i < (size - 1)) {
                                cs += ",";
                            }
                        }
                        anonEdit.remove("AnonRenewedCart");
                        anonEdit.putString("AnonRenewedCart", cs);
                        anonEdit.apply();

                    Toast.makeText(getApplicationContext(),getString(R.string.guest_user_cart_updated),Toast.LENGTH_SHORT).show();

                    String identicalcheck = cs;
                    String potentialcommaset = identicalcheck.replaceAll(product.getId(),"");
                    int potentialcommasetlength = potentialcommaset.length();
                    if(cs.equals(product.getId()))
                    {
                        int incrt = 1;
                        offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                        offerQtyInCart.setVisibility(View.VISIBLE);


                        //Changing navigation drawer number textview
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

                        return;
                    }

                    else if (cs.contains(product.getId())&&potentialcommaset.replaceAll(",","").length()==0)
                    {
                        int incrt = potentialcommasetlength+1;
                        offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                        offerQtyInCart.setVisibility(View.VISIBLE);

                        //Changing navigation drawer number textview
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

                        return;
                    }
                    else {
                        String halfdone = cs.replaceAll(product.getId() + ",", "");
                        String fulldone = halfdone.replaceAll("," + product.getId(), "");
                        int ogcount = 0;
                        for (int commacount = 0; commacount <cs.length(); commacount++) {
                            if (cs.charAt(commacount) == ',') {
                                ogcount++;
                            }
                        }
                        ogcount++;
                        int nowcount = 0;
                        for (int commanew = 0; commanew < fulldone.length(); commanew++) {
                            if (fulldone.charAt(commanew) == ',') {
                                nowcount++;
                            }
                        }
                        nowcount++;
                        int incrt = ogcount - nowcount;
                        if (incrt != 0) {
                            offerQtyInCart.setText(getString(R.string.in_cart) + incrt);
                            offerQtyInCart.setVisibility(View.VISIBLE);
                        }
                    }
                    //Changing navigation drawer number textview
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
                    return;
                    }
                HomeScreen.HomeScreenProgress = new ProgressDialog(OfferDetail.this);
                HomeScreen.HomeScreenProgress.setMessage(getString(R.string.adding_to_cart));
                HomeScreen.HomeScreenProgress.setCancelable(false);
                HomeScreen.HomeScreenProgress.show();
                SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor renewPrefEdit = renewPref.edit();
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
                user.addToCart(product.getId(),1);
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
            private void handleCartResponse(Response response)
            {
                CompositeSubscription renewCartSub = new CompositeSubscription();
                renewCartSub.add(NetworkUtil.getRetrofit(token).getProfile(email)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleRenewResponse, this::handleCartError));
            }
            private  void handleRenewResponse(User renewedUser)
            {
                SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor renewEditor = renewPref.edit();
                renewEditor.remove("RenewedCart");
                renewEditor.putString("RenewedCart",renewedUser.getCartstring());
                renewEditor.apply();


                HomeScreen.HomeScreenProgress.dismiss();

                Toast.makeText(getApplicationContext(),getString(R.string.cart_updated),Toast.LENGTH_SHORT).show();

                String identicalcheck = renewedUser.getCartstring();
                String potentialcommaset = identicalcheck.replaceAll(product.getId(),"");
                int potentialcommasetlength = potentialcommaset.length();
                if(renewedUser.getCartstring().equals(product.getId()))
                {
                    int incrt = 1;
                    offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                    offerQtyInCart.setVisibility(View.VISIBLE);

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


                    return;
                }

                else if(renewedUser.getCartstring().contains(product.getId())&&potentialcommaset.replaceAll(",","").length()==0)
                {
                    int incrt = potentialcommasetlength+1;
                    offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                    offerQtyInCart.setVisibility(View.VISIBLE);

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

                    return;
                }
                else {
                    String halfdone = renewedUser.getCartstring().replaceAll(product.getId() + ",", "");
                    String fulldone = halfdone.replaceAll("," + product.getId(), "");
                    int ogcount = 0;
                    for (int commacount = 0; commacount < renewedUser.getCartstring().length(); commacount++) {
                        if (renewedUser.getCartstring().charAt(commacount) == ',') {
                            ogcount++;
                        }
                    }
                    ogcount++;
                    int nowcount = 0;
                    for (int commanew = 0; commanew < fulldone.length(); commanew++) {
                        if (fulldone.charAt(commanew) == ',') {
                            nowcount++;
                        }
                    }
                    nowcount++;
                    int incrt = ogcount - nowcount;
                    if (incrt != 0) {
                        offerQtyInCart.setText(getString(R.string.in_cart) + incrt);
                        offerQtyInCart.setVisibility(View.VISIBLE);
                    }
                }

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

                return;


            }
            private void handleCartError(Throwable error)
            {
                HomeScreen.HomeScreenProgress.dismiss();
                if(error instanceof HttpException)
                {
                    Gson gson = new GsonBuilder().create();
                    try
                    {
                        String errorBody = ((HttpException)error).response().errorBody().string();
                        Response response = gson.fromJson(errorBody,Response.class);


                        Toast.makeText(getApplicationContext(),getString(R.string.internal_server_error),Toast.LENGTH_SHORT).show();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {

                    Toast.makeText(getApplicationContext(),getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                }
            }
        });
        buynow.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.coming_in_future_updates, Toast.LENGTH_SHORT).show();
            }
        });
        compare.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),R.string.coming_in_future_updates, Toast.LENGTH_SHORT).show();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0)
                {
                    collapsingToolbarLayout.setTitle(product.getItemName());
                    isShow = true;
                }
                else if(isShow)
                {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });


        if(HomeScreen.fromAnon)
        {
            SharedPreferences incartqtyPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String crt = incartqtyPref.getString("AnonRenewedCart","");
            String identicalcheck = crt;
            String potentialcommaset = identicalcheck.replaceAll(product.getId(),"");
            int potentialcommasetlength = potentialcommaset.length();
            if(crt.equals(product.getId()))
            {
                 int incrt = 1;
                offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                offerQtyInCart.setVisibility(View.VISIBLE);
                return;
            }

            else if (crt.contains(product.getId())&&potentialcommaset.replaceAll(",","").length()==0)
            {
                int incrt = potentialcommasetlength+1;
                offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                offerQtyInCart.setVisibility(View.VISIBLE);
                return;
            }
            else {
                String halfdone = crt.replaceAll(product.getId() + ",", "");
                String fulldone = halfdone.replaceAll("," + product.getId(), "");
                int ogcount = 0;
                for (int commacount = 0; commacount < crt.length(); commacount++) {
                    if (crt.charAt(commacount) == ',') {
                        ogcount++;
                    }
                }
                ogcount++;
                int nowcount = 0;
                for (int commanew = 0; commanew < fulldone.length(); commanew++) {
                    if (fulldone.charAt(commanew) == ',') {
                        nowcount++;
                    }
                }
                nowcount++;
                 int incrt = ogcount - nowcount;
                if (incrt != 0) {
                    offerQtyInCart.setText(getString(R.string.in_cart) + incrt);
                    offerQtyInCart.setVisibility(View.VISIBLE);
                }
            }

        }
        if(!HomeScreen.fromAnon)
        {
            SharedPreferences incartqtyPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String crt = incartqtyPref.getString("RenewedCart","");
            String identicalcheck = crt;
            String potentialcommaset = identicalcheck.replaceAll(product.getId(),"");
            int potentialcommasetlength = potentialcommaset.length();
            if(crt.equals(product.getId()))
            {
                 int incrt = 1;
                offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                offerQtyInCart.setVisibility(View.VISIBLE);
                return;
            }

            else if(crt.contains(product.getId())&&potentialcommaset.replaceAll(",","").length()==0)
            {
                int incrt = potentialcommasetlength+1;
                offerQtyInCart.setText(getString(R.string.in_cart)+ incrt);
                offerQtyInCart.setVisibility(View.VISIBLE);
                return;
            }
            else {
                String halfdone = crt.replaceAll(product.getId() + ",", "");
                String fulldone = halfdone.replaceAll("," + product.getId(), "");
                int ogcount = 0;
                for (int commacount = 0; commacount < crt.length(); commacount++) {
                    if (crt.charAt(commacount) == ',') {
                        ogcount++;
                    }
                }
                ogcount++;
                int nowcount = 0;
                for (int commanew = 0; commanew < fulldone.length(); commanew++) {
                    if (fulldone.charAt(commanew) == ',') {
                        nowcount++;
                    }
                }
                nowcount++;
                 int incrt = ogcount - nowcount;
                if (incrt != 0) {
                    offerQtyInCart.setText(getString(R.string.in_cart) + incrt);
                    offerQtyInCart.setVisibility(View.VISIBLE);
                }
            }

        }


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;
        ProgressBar bmProgress;
        public DownloadImageTask(ImageView bmImage,ProgressBar bmProgress)
        {
            this.bmImage = bmImage;
            this.bmProgress = bmProgress;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIconll = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIconll = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Image Error", e.getMessage());
                e.printStackTrace();
            }
            return mIconll;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmProgress.setVisibility(View.GONE);
        }
    }
    public static void buttonEffect (View button)
    {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void handleRelatedResponse(Product[] relatedResults)
    {

        for(int filtercount = 0; filtercount<relatedResults.length;filtercount++)
        {
            if(relatedResults[filtercount].getId().equals(product.getId()))
            {
                relatedResults = (Product[]) ArrayUtils.remove(relatedResults,filtercount);
                break;
            }
        }

        if(relatedProductsArray.length==0)
        {
            relatedProductsArray = (Product[]) ArrayUtils.addAll(relatedProductsArray, relatedResults);
            relatedItemsDataAdapter = new RelatedItemsDataAdapter(this, relatedProductsArray, (View) findViewById(R.id.content_offer_detail));
            relatedItemsRecyclerView.setHasFixedSize(false);
            relatedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            relatedItemsRecyclerView.setAdapter(relatedItemsDataAdapter);
            relatedItemsRecyclerView.setNestedScrollingEnabled(false);
        }
        else
        {
            relatedProductsArray = (Product[]) ArrayUtils.addAll(relatedProductsArray, relatedResults);
            relatedItemsDataAdapter.notifyDataSetChanged();
        }


        keyWordUpCounter++;
        if(keyWordUpCounter<allWords.length)
        {
            String NorC = "n";
            if(keyWordUpCounter>keyWordMidPoint)
            {
                NorC = "c";
            }
            final ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnected())
            {
                CompositeSubscription relatedSubscription = new CompositeSubscription();
                relatedSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts("MF"+ NorC+allWords[keyWordUpCounter])
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this:: handleRelatedResponse, this::handleRelatedError));
            }
            else
            {

            }

        }
        else
        {

        }

    }
    private void handleRelatedError(Throwable error)
    {

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
}
