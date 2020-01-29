package com.business.order_trip;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.business.order_trip.adapters.CanceldTrip_Adapter;
import com.business.order_trip.adapters.UpcomingTrips_Adapter;
import com.business.order_trip.models.TripModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UpcomingTripsActivity extends AppCompatActivity {
    ArrayList<TripModel> arrayList = new ArrayList<>();
    UpcomingTrips_Adapter adapter;
    ListView listView;
    View view;
    String user_id;
    ImageButton back;
    DatabaseReference reference;
    TextView mWarning;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_trips);

        mWarning = findViewById(R.id.warning);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.lv_upcoming_trips);


        Intent intent = getIntent();
        user_id  = intent.getStringExtra("user_id");

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        long delayInMillis = 5000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        initArray();
                    }
                });
            }
        }, delayInMillis);

    }

    private void initArray() {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trips");
        reference.orderByChild("timestamp").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TripModel trip = snapshot.getValue(TripModel.class);

                    assert trip != null;
                    if(trip.getTrip_user_id().equals(user_id)){
                        if (trip.getStatus().equals("on")){
                            arrayList.add(trip);
                        }
                    }
                }

                adapter = new UpcomingTrips_Adapter(UpcomingTripsActivity.this, arrayList);

                if(adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                    mWarning.setText("There are any upcoming trips");
                }else{
                    listView.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
