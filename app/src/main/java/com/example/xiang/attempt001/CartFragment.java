package com.example.xiang.attempt001;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Locale;


/**
 * Created by xiang on 2017-06-10.
 */

public class CartFragment extends Fragment {
    View myView;
    int comma;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences languagePreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String lanPref = languagePreference.getString("language","");
        if(lanPref.length()!=0) {
            Locale defaultLocale = new Locale(lanPref);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = defaultLocale;
            res.updateConfiguration(conf, dm);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_cart));
        myView = inflater.inflate(R.layout.cart, container, false);

        SharedPreferences cartpref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        if(HomeScreen.fromAnon==false)
        {        String cartcontent = cartpref.getString("RenewedCart",null);
            if (cartcontent!=null) {
                if(cartcontent.length()==0)
                {
                    Toast.makeText(getContext(), R.string.you_have_no_items, Toast.LENGTH_LONG).show();
                }
                else
                {
                    for (int commacount = 0; commacount < cartcontent.length(); commacount++) {
                        if (cartcontent.charAt(commacount) == ',') {
                            comma++;
                        }
                    }
                    Toast.makeText(getContext(), getString(R.string.you_have) + (comma + 1) + getString(R.string.items), Toast.LENGTH_LONG).show();
                    String[]cartcontentarr = cartcontent.split(",");
                    Product[]precompare = new Product[HomeScreen.groupArray.length];
                    for (int c = 0; c< HomeScreen.groupArray.length;c++)
                    {
                        precompare[c]=HomeScreen.groupArray[c];
                    }
                    int[]qtyArr = new int[HomeScreen.groupArray.length];
                    for (int d = 0;d<HomeScreen.groupArray.length;d++)
                    {
                        qtyArr[d]=0;
                    }

                    for(int s = 0; s<HomeScreen.groupArray.length;s++) {
                        for (int g = 0; g < cartcontentarr.length; g++) {
                            if(precompare[s].getId().replaceAll(cartcontentarr[g],"").length()==0)
                            {
                                qtyArr[s]++;
                            }
                        }

                    }

                    RecyclerView cartItems = (RecyclerView)myView.findViewById(R.id.CartRecyclerView);
                    cartItems.setNestedScrollingEnabled(false);
                    String qty = getString(R.string.qty);
                    CartAdapter cartAdapter = new CartAdapter(getActivity(),precompare,qtyArr,qty);
                    cartItems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    cartItems.setAdapter(cartAdapter);
                }
            }}
        if(HomeScreen.fromAnon==true)
        {
            String cartcontent = cartpref.getString("AnonRenewedCart",null);
            if(cartcontent==null)
            {
                Toast.makeText(getContext(),R.string.you_have_no_items,Toast.LENGTH_LONG).show();
            }
            if(cartcontent!=null)
            {
                if(cartcontent.length()==0)
                {
                    Toast.makeText(getContext(),R.string.you_have_no_items, Toast.LENGTH_LONG).show();
                }
                else
                {
                    for (int commacount = 0; commacount < cartcontent.length(); commacount++) {
                        if (cartcontent.charAt(commacount) == ',') {
                            comma++;
                        }
                    }

                    Toast.makeText(getContext(), getString(R.string.you_have) + (comma + 1) + getString(R.string.items), Toast.LENGTH_LONG).show();


                    String[]cartcontentarr = cartcontent.split(",");
                    Product[]precompare = new Product[HomeScreen.groupArray.length];
                    for (int c = 0; c< HomeScreen.groupArray.length;c++)
                    {
                        precompare[c]=HomeScreen.groupArray[c];
                    }
                    int[]qtyArr = new int[HomeScreen.groupArray.length];
                    for (int d = 0;d<HomeScreen.groupArray.length;d++)
                    {
                        qtyArr[d]=0;
                    }

                    for(int s = 0; s<HomeScreen.groupArray.length;s++) {
                        for (int g = 0; g < cartcontentarr.length; g++) {
                            if(precompare[s].getId().replaceAll(cartcontentarr[g],"").length()==0)
                            {
                                qtyArr[s]++;
                            }
                        }

                    }

                    RecyclerView cartItems = (RecyclerView)myView.findViewById(R.id.CartRecyclerView);
                    cartItems.setNestedScrollingEnabled(false);
                    String qty = getString(R.string.qty);
                    CartAdapter cartAdapter = new CartAdapter(getActivity(),precompare,qtyArr,qty);
                    cartItems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    cartItems.setAdapter(cartAdapter);

                }

            }
        }

        return myView;
    }
}
