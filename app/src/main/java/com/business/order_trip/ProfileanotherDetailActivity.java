package com.business.order_trip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ProfileanotherDetailActivity extends AppCompatActivity {
    ImageButton back, profile_edit;
    CircleImageView ivAvatar;
    String imageUri, shopperCount, tripCount;
    int preShopperCount, preTripCount, upComingTripCount;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    int phoneStatus, emailStatus;
    TextView userName, joinDate, shopper_cnt, trip_cnt, upcoming_trip_cnt, pre_shopper_cnt, pre_trip_cnt, status;
    SimpleRatingBar shopperRate, tripRate;
    int social_type;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileanother_detail);


        Intent intent = getIntent();
        user_id  = intent.getStringExtra("user_id");

        userName = (TextView) findViewById(R.id.user_name);
        joinDate = (TextView) findViewById(R.id.join_date);
        pre_shopper_cnt = (TextView) findViewById(R.id.pre_shopper_cnt);
        pre_trip_cnt = (TextView) findViewById(R.id.pre_trip_cnt);
        shopper_cnt = (TextView) findViewById(R.id.shopper_cnt);
        trip_cnt = (TextView) findViewById(R.id.trip_cnt);
        upcoming_trip_cnt = (TextView) findViewById(R.id.upcoming_trip_cnt);
        status = (TextView) findViewById(R.id.status);
        ivAvatar = findViewById(R.id.profile_image);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);

                userName.setText(user1.getUsername());
                joinDate.setText("Joined on " +  user1.getDate());
                imageUri = user1.getImageUri();
                social_type = user1.getSocial_type();

                if(!user1.getImageUri().equals("null")){
                    if (!ProfileanotherDetailActivity.this.isFinishing()) {
                        Glide.with(ProfileanotherDetailActivity.this).load(imageUri).into(ivAvatar);
                    }
                }

                phoneStatus = user1.getPhone_status();
                emailStatus = user1.getEmail_status();
                preShopperCount = user1.getPre_shopper_count();
                preTripCount = user1.getPre_trip_count();
                shopperCount = dataSnapshot.child("shopper_count").getValue().toString();
                tripCount = dataSnapshot.child("trip_count").getValue().toString();
                upComingTripCount = user1.getUpcoming_trip_count();

                if (preShopperCount == 0 || preTripCount == 0){
                    status.setText("NO REVIEWS YET");
                }else status.setVisibility(View.GONE);


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
                upcoming_trip_cnt.setText(upComingTripCount + " upcoming trips");

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


        ivAvatar = findViewById(R.id.profile_image1);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!imageUri.equals("null")){
                    Intent inf=new Intent(ProfileanotherDetailActivity.this, fullscreenActivity.class);
                    inf.putExtra("image_uri", imageUri);
                    startActivity(inf);
                }
            }
        });

        RelativeLayout button2 = (RelativeLayout) findViewById(R.id.rl_upcoming_trips);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (upComingTripCount != 0) {
                    Intent myIntent = new Intent(ProfileanotherDetailActivity.this, UpcomingTripsActivity.class);
                    myIntent.putExtra("user_id", user_id);
                    startActivity(myIntent);
                    finish();
                }
            }
        });

        ImageButton button3 = (ImageButton) findViewById(R.id.ib_more);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alert_open();
            }
        });
    }

    public void alert_open(){

        final Dialog dialog = new Dialog(ProfileanotherDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_profile_order_details_report);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        LinearLayout dialog_btn1 = dialog.findViewById(R.id.ll_btn1);
        dialog_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(ProfileanotherDetailActivity.this, MySupportRequestActivity.class);
                startActivity(myIntent);
                finish();

            }
        });

        LinearLayout dialog_btn2 = dialog.findViewById(R.id.ll_btn3);
        dialog_btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        dialog.show();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *1f), dialog.getWindow().getAttributes().height);
    }
}
