package com.business.order_trip;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSettingActivity extends AppCompatActivity  {
    private static final String TAG = "ProfileSettingActivity";
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference reference;
    RelativeLayout rl_setting_email;

    private FirebaseAuth auth;
    private FirebaseUser user;
    TextView set_email, set_phone_number, set_payout_method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String email = user.getEmail();
        set_email = (TextView) findViewById(R.id.email);
        set_email.setText(email);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone_number = dataSnapshot.child("phone_number").getValue().toString();
                set_phone_number = (TextView) findViewById(R.id.phone_number);
                set_phone_number.setText(phone_number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        rl_setting_email = findViewById(R.id.rl_setting_email);
        rl_setting_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingActivity.this, EmailVerifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingActivity.this, MainActivity.class);
                intent .putExtra("openF4",4);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        Button btn_log_out = (Button) findViewById(R.id.btn_log_out);
        btn_log_out.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoginManager.getInstance().logOut();
                auth.signOut();
                Intent myIntent = new Intent(ProfileSettingActivity.this, SplashActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        RelativeLayout button1 = (RelativeLayout) findViewById(R.id.rl_setting_email);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(ProfileSettingActivity.this, EmailVerifyActivity.class);
                startActivity(myIntent);
            }
        });

        RelativeLayout button2 = (RelativeLayout) findViewById(R.id.rl_setting_phone);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(ProfileSettingActivity.this, ProfileSettingPhoneActivity.class);
                startActivity(myIntent);
            }
        });

        RelativeLayout button3 = (RelativeLayout) findViewById(R.id.rl_setting_payout);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(ProfileSettingActivity.this, ProfileSettingPayoutActivity.class);
                startActivity(myIntent);
            }
        });

//        RelativeLayout button4 = (RelativeLayout) findViewById(R.id.rl_setting_note);
//        button4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent myIntent = new Intent(ProfileSettingActivity.this, ProfileSettingNoteActivity.class);
//                startActivity(myIntent);
//            }
//        });
    }
}
