package com.example.xiang.attempt001;
/**
 * Created by xiang on 2017-05-07.
 */
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class AppIntentService extends IntentService{
    private static final String TAG = "com.example.xiang.attempt001";
    public AppIntentService(){super ("AppIntentService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "The service has now started");
    }
}
