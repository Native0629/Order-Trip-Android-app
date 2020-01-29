package com.business.order_trip;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.business.order_trip.fragments.HomeFragment;
import com.business.order_trip.fragments.MessageFragment;
import com.business.order_trip.fragments.ProfileFragment;
import com.business.order_trip.fragments.SavedFragment;
import com.business.order_trip.fragments.TripFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = "MainActivity";

    FragmentTransaction transaction;
    FrameLayout frameLayout;
    LinearLayout ly_tab;
    LinearLayout tab1, tab2, tab3, tab4, tab5;
    public int index_before = 0, index_cur = 0;
    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        frameLayout = (FrameLayout)findViewById(R.id.frame_container);
        ly_tab = (LinearLayout)findViewById(R.id.ly_tab);

        tab1= (LinearLayout) findViewById(R.id.tab_home);
        tab2 = (LinearLayout) findViewById(R.id.tab_save);
        tab3= (LinearLayout) findViewById(R.id.tab_trip);
        tab4= (LinearLayout) findViewById(R.id.tab_message);
        tab5= (LinearLayout) findViewById(R.id.tab_profile);

        ImageView img_home = (ImageView)findViewById(R.id.img_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_activity(0);
            }
        });
        ImageView img_saved = (ImageView)findViewById(R.id.img_saved);
        img_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_activity(1);
            }
        });
        ImageView img_trip = (ImageView)findViewById(R.id.img_trip);
        img_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_activity(2);
            }
        });
        ImageView img_message = (ImageView)findViewById(R.id.img_message);
        img_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_activity(3);
            }
        });
        ImageView img_profile = (ImageView)findViewById(R.id.img_profile);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_activity(4);
            }
        });

        if(extras!=null && extras.containsKey("openF2")) {
//            String open_select = extras.getString("openF2");
            Fragment fragment1 = new TripFragment();
            Bundle bundle=new Bundle();
            bundle.putString("message", "From Activity");
            fragment1.setArguments(bundle);
            tab1.setBackgroundColor(Color.parseColor("#00000000"));
            tab3.setBackgroundResource(R.color.colorPrimary);

            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment1);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if(extras!=null && extras.containsKey("openF4")){
            refresh_activity(4);

        }else if(extras!=null && extras.containsKey("openF3")){
            refresh_activity(2);

        } else {
            refresh_activity(0);
        }
    }
    public void refresh_activity(int index) {
        index_cur = index;
        index_before = index_cur;

        tab1.setBackgroundColor(Color.parseColor("#00000000"));
        tab2.setBackgroundColor(Color.parseColor("#00000000"));
        tab3.setBackgroundColor(Color.parseColor("#00000000"));
        tab4.setBackgroundColor(Color.parseColor("#00000000"));
        tab5.setBackgroundColor(Color.parseColor("#00000000"));

        switch (index) {
            case 0:
                selectFragment(new HomeFragment());
                tab1.setBackgroundResource(R.color.colorPrimary);
                break;
            case 1:
                selectFragment(new SavedFragment());
                tab2.setBackgroundResource(R.color.colorPrimary);
                break;
            case 2:
                selectFragment(new TripFragment());
                tab3.setBackgroundResource(R.color.colorPrimary);
                break;
            case 3:
                selectFragment(new MessageFragment());//MessageFragment,  UsersFragment
                tab4.setBackgroundResource(R.color.colorPrimary);
                break;
            case 4:
                selectFragment(new ProfileFragment());
                tab5.setBackgroundResource(R.color.colorPrimary);
                break;
            default: break;
        }
    }
    private void selectFragment(Fragment fragment) {

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        long t = System.currentTimeMillis();

        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you really finish this app?");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }
}
