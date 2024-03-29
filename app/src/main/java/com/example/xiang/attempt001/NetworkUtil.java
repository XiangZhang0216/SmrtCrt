package com.example.xiang.attempt001;

/**
 * Created by xiang on 2017-06-18.
 */

import android.util.Base64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class NetworkUtil {

    public static Retrofit2Interface getRetrofit(){

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(NewLoginConstants.BASE_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Retrofit2Interface.class);

    }

    public static Retrofit2Interface getRetrofit(String email, String password) {

        String credentials = email + ":" + password;
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("Authorization", basic)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        return new Retrofit.Builder()
                .baseUrl(NewLoginConstants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Retrofit2Interface.class);
    }

    public static Retrofit2Interface getRetrofit(String token) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("x-access-token", token)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        return new Retrofit.Builder()
                .baseUrl(NewLoginConstants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Retrofit2Interface.class);
    }

}