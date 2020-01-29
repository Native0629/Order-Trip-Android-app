package com.business.order_trip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailActivity extends AppCompatActivity {
    ImageButton back, profile_edit;
    CircleImageView ivAvatar;
    String imageUri, shopperCount, tripCount;
    int preShopperCount, preTripCount, upComingTripCount;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;
    int phoneStatus, emailStatus;
    TextView userName, joinDate, shopper_cnt, trip_cnt, upcoming_trip_cnt, pre_shopper_cnt, pre_trip_cnt;
    SimpleRatingBar shopperRate, tripRate;
    int social_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_more_);

        userName = (TextView) findViewById(R.id.user_name);
        joinDate = (TextView) findViewById(R.id.join_date);
        pre_shopper_cnt = (TextView) findViewById(R.id.pre_shopper_cnt);
        pre_trip_cnt = (TextView) findViewById(R.id.pre_trip_cnt);
        shopper_cnt = (TextView) findViewById(R.id.shopper_cnt);
        trip_cnt = (TextView) findViewById(R.id.trip_cnt);
//        upcoming_trip_cnt = (TextView) findViewById(R.id.upcoming_trip_cnt);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);

                userName.setText(user1.getUsername());
                joinDate.setText("Joined on " +  user1.getDate());
                imageUri = user1.getImageUri();
                social_type = user1.getSocial_type();

                if(!user1.getImageUri().equals("null")){
                    if (!ProfileDetailActivity.this.isFinishing()) {
                        Glide.with(ProfileDetailActivity.this).load(imageUri).into(ivAvatar);
                    }
                }

                phoneStatus = user1.getPhone_status();
                emailStatus = user1.getEmail_status();
                preShopperCount = user1.getPre_shopper_count();
                preTripCount = user1.getPre_trip_count();
                shopperCount = dataSnapshot.child("shopper_count").getValue().toString();
                tripCount = dataSnapshot.child("trip_count").getValue().toString();
//                upComingTripCount = user1.getUpcoming_trip_count();

                ImageView phone_check = (ImageView) findViewById(R.id.phone_check);
                if(phoneStatus == 1){
                    phone_check.setVisibility(View.VISIBLE);
                }else{
                    phone_check.setVisibility(View.GONE);
                }

                ImageView email_check = (ImageView) findViewById(R.id.email_check);
                if(emailStatus == 1){
                    email_check.setVisibility(View.VISIBLE);
                }else{
                    email_check.setVisibility(View.GONE);
                }

                pre_shopper_cnt.setText("(" + preShopperCount + ")");
                pre_trip_cnt.setText("(" + preTripCount + ")");
                shopper_cnt.setText(shopperCount);
                trip_cnt.setText(tripCount);
//                upcoming_trip_cnt.setText(upComingTripCount + " upcoming trips");

                shopperRate = findViewById(R.id.shopper_rate);
                if( preShopperCount == 0){
                    shopperRate.setRating(0);
                }else if(preShopperCount <= 1){
                    shopperRate.setRating(1);

                }else if(preShopperCount <= 3){
                    shopperRate.setRating(2);

                }else if(preShopperCount <= 5){
                    shopperRate.setRating(3);

                }else if(preShopperCount <= 8){
                    shopperRate.setRating(4);

                }else {
                    shopperRate.setRating(5);
                }

                tripRate = findViewById(R.id.trip_rate);
                if(preTripCount == 0){
                    tripRate.setRating(0);
                }else if(preTripCount <= 1){
                    tripRate.setRating(1);

                }else if(preTripCount <= 3){
                    tripRate.setRating(2);

                }else if(preTripCount <= 5){
                    tripRate.setRating(3);

                }else if(preTripCount <= 8){
                    tripRate.setRating(4);

                }else {
                    tripRate.setRating(5);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_edit = (ImageButton) findViewById(R.id.ib_edit);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(ProfileDetailActivity.this, ProfileEditActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        ivAvatar = findViewById(R.id.profile_image1);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!imageUri.equals("null")){
                    Intent inf=new Intent(ProfileDetailActivity.this, fullscreenActivity.class);
                    inf.putExtra("image_uri", imageUri);
                    startActivity(inf);
                }
            }
        });

//        RelativeLayout button2 = (RelativeLayout) findViewById(R.id.rl_upcoming_trips);
//        button2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                if (upComingTripCount != 0){
//                    Intent myIntent = new Intent(ProfileDetailActivity.this, UpcomingTripsActivity.class);
//                    myIntent.putExtra("user_id", user.getUid());
//                    startActivity(myIntent);
//                    finish();
//                }
//
//            }
//        });
    }
}
