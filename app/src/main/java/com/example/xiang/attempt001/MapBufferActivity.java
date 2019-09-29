package com.example.xiang.attempt001;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

public class MapBufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SupportMapFragment sMapFragment;
        sMapFragment = SupportMapFragment.newInstance();
        setContentView(R.layout.activity_map_buffer);


        new Handler().postDelayed(new Runnable(){

            @Override
            public void run()
            {
                Intent a = new Intent(MapBufferActivity.this,HomeScreen.class);
                boolean CrazyDiamond = true;
                a.putExtra("Crazy Diamond", CrazyDiamond);
                startActivity(a);
                MapBufferActivity.this.finish();
            }

        },1000);
    }
    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
