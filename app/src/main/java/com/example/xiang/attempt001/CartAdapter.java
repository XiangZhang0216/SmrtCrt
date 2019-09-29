package com.example.xiang.attempt001;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-06-11.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;
    Product[]cartData;
    int[]qtyData;
    LayoutInflater inflater;
    String localeqty;
    public CartAdapter(Context context, Product[] cartData, int[]qtyData,String qty)
    {
        this.context=context;
        this.cartData = cartData;
        this.qtyData = qtyData;
        this.localeqty = qty;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_item, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.CartViewHolder holder, int position) {
        holder.Position = position;
        holder.cartText.setText(cartData[position].getItemName());
        holder.cartQty.setText(localeqty+ qtyData[position]);
        holder.cartPrice.setText("$"+cartData[position].getPrice());
        new CartAdapter.DownloadImageTask(holder.cartImage,holder.cartProgress).execute(cartData[position].getImageLink());
    }

    @Override
    public int getItemCount() {
        return cartData.length;
    }

    class CartViewHolder extends RecyclerView.ViewHolder
    {
        int Position;
        TextView cartText;
        TextView cartQty;
        TextView cartPrice;
        ImageView cartImage;
        ProgressBar cartProgress;
        String imageurl;
        Button cartMoreButton;
        public CartViewHolder(View view)
        {
            super(view);
            cartText = (TextView)view.findViewById(R.id.cart_item_textview);
            cartQty = (TextView)view.findViewById(R.id.cart_item_quantity);
            cartPrice = (TextView)view.findViewById(R.id.cart_item_price);
            cartImage = (ImageView)view.findViewById(R.id.cart_item_imageview);
            cartProgress = (ProgressBar)view.findViewById(R.id.cartitemprogress);
            cartMoreButton = (Button) view.findViewById(R.id.cart_item_morebutton);
            buttonEffect(cartMoreButton);
            cartMoreButton.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (HomeScreen.fromAnon)
                    {
                        SharedPreferences anonDeletePref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
                        SharedPreferences.Editor anonDeleteEdit = anonDeletePref.edit();
                        String anonOldCart = anonDeletePref.getString("AnonRenewedCart","");
                        if((anonOldCart==null)||(anonOldCart.length()<=3))
                        {
                            anonDeleteEdit.remove("AnonRenewedCart");
                            anonDeleteEdit.putString("AnonRenewedCart","");
                        }
                        String cs = "";
                        User anonUser = new User();
                        int size = 0;
                        anonOldCart = anonDeletePref.getString("AnonRenewedCart","");
                        if(anonOldCart.length()==0)
                        {
                            Toast.makeText(v.getContext(),R.string.you_already_deleted_this, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(anonOldCart.length()!=0)
                        {
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

                        int deleteresult = anonUser.deleteFromCart(cartData[Position].getId(),qtyData[Position]);
                        size = anonUser.getCartSize();
                        for (int i = 0; i < size; i++) {
                            cs += anonUser.getCart()[i];
                            if (i < (size - 1)) {
                                cs += ",";
                            }
                        }

                        anonDeleteEdit.remove("AnonRenewedCart");
                        anonDeleteEdit.putString("AnonRenewedCart", cs);
                        anonDeleteEdit.apply();
                        if (deleteresult==1)
                        {
                            cartQty.setText(localeqty+0);
                            Toast.makeText(v.getContext().getApplicationContext(),cartData[Position].getItemName()+context.getString(R.string.has_been_deleted_from_cart),Toast.LENGTH_LONG).show();
                        }

                        String cartcontent = anonDeletePref.getString("AnonRenewedCart",null);
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

                    SharedPreferences deletePref = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
                    SharedPreferences.Editor deleteEdit = deletePref.edit();
                    String token = deletePref.getString(NewLoginConstants.TOKEN,"");
                    String email = deletePref.getString(NewLoginConstants.EMAIL,"");
                    String id = deletePref.getString("ReturningId","");
                    String oldCart = deletePref.getString("RenewedCart",null);
                    int size = 0;
                    String cs = "";
                    User user = new User();
                    if((oldCart==null)||(oldCart.length()<=3))
                    {
                        deleteEdit.remove("RenewedCart");
                        deleteEdit.putString("RenewedCart","");
                    }
                    if(oldCart.length()==0)
                    {
                        Toast.makeText(v.getContext(), R.string.you_already_deleted_this,Toast.LENGTH_SHORT).show();
                        return;
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
                    int deleteresult = user.deleteFromCart(cartData[Position].getId(),qtyData[Position]);
                    size = user.getCartSize();
                    for (int i = 0; i < size; i++) {
                        cs += user.getCart()[i];
                        if (i < (size - 1)) {
                            cs += ",";
                        }
                    }

                    if (deleteresult==1) {
                        user.setDeveloperToolCode("cart");
                        user.setCartstring(cs);
                        user.setLookupid(id);
                        HomeScreen.HomeScreenProgress = new ProgressDialog(v.getContext());
                        HomeScreen.HomeScreenProgress.setMessage(context.getString(R.string.removing_product_from_cart));
                        HomeScreen.HomeScreenProgress.setCancelable(false);
                        HomeScreen.HomeScreenProgress.show();
                        CompositeSubscription deleteSubscriptionTest = new CompositeSubscription();
                        deleteSubscriptionTest.add(NetworkUtil.getRetrofit(token).changePassword(email, user)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(this::handleDeleteResponse, this::handleDeleteError));
                    }
                    if(deleteresult==2)
                    {
                        Toast.makeText(view.getContext().getApplicationContext(), R.string.you_dont_have_enough,Toast.LENGTH_SHORT).show();
                    }
                    return;

                }
                private void handleDeleteResponse(Response response)
                {

                    SharedPreferences deletePref = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
                    SharedPreferences.Editor deleteEdit = deletePref.edit();
                    String token = deletePref.getString(NewLoginConstants.TOKEN,"");
                    String email = deletePref.getString(NewLoginConstants.EMAIL,"");
                    Toast.makeText(view.getContext().getApplicationContext(),cartData[Position].getItemName()+context.getString(R.string.has_been_deleted_from_cart),Toast.LENGTH_LONG).show();
                    CompositeSubscription renewSub = new CompositeSubscription();
                    renewSub.add(NetworkUtil.getRetrofit(token).getProfile(email)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(this::handleRenewResponse, this::handleDeleteError));
                }
                private void handleDeleteError(Throwable error)
                {
                    HomeScreen.HomeScreenProgress.dismiss();
                    if(error instanceof HttpException)
                    {
                        Gson gson = new GsonBuilder().create();
                        try
                        {
                            String errorBody = ((HttpException)error).response().errorBody().string();
                            Response response = gson.fromJson(errorBody,Response.class);

                            Toast.makeText(view.getContext().getApplicationContext(),R.string.internal_server_error,Toast.LENGTH_LONG).show();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(view.getContext().getApplicationContext(),R.string.network_error,Toast.LENGTH_LONG).show();
                    }
                }
                private void handleRenewResponse(User renewedUser)
                {
                    SharedPreferences renewPref = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
                    SharedPreferences.Editor renewEditor = renewPref.edit();
                    renewEditor.remove("RenewedCart");
                    renewEditor.putString("RenewedCart",renewedUser.getCartstring());
                    renewEditor.apply();
                    cartQty.setText(localeqty+0);

                    HomeScreen.HomeScreenProgress.dismiss();
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
                }
            });

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final ConnectivityManager conMgr = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    if(activeNetwork != null && activeNetwork.isConnected()) {

                        Intent gotoDetails = new Intent(v.getContext(), OfferDetail.class);
                        Bundle offerDetailsBundle = new Bundle();
                        offerDetailsBundle.putSerializable("DetailObject", cartData[Position]);
                        gotoDetails.putExtras(offerDetailsBundle);
                        context.startActivity(gotoDetails);
                    }
                    else
                    {
                        Snackbar.make(v,v.getContext().getString(R.string.no_internet),Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

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

}
