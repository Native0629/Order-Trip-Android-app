package com.business.order_trip.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.business.order_trip.R;

public class fullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ImageView mImageView = findViewById(R.id.widget45);

        Intent intent = getIntent();
        String image_uri = intent.getStringExtra("image_uri");
        Glide.with(getApplicationContext()).load(image_uri).into(mImageView);

    }
}
