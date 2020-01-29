package com.business.order_trip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.order_trip.adapters.DeliveryRequest_Adapter;
import com.business.order_trip.adapters.Saved_Trip_Adapter;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.TripModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveryRequestActivity extends AppCompatActivity {
    ArrayList<TripModel> arrayList = new ArrayList<>();
    DeliveryRequest_Adapter adapter;
    ListView listView;
    View view;
    ImageButton back, next;
    TextView mWarning;

    DatabaseReference reference;
    String order_id, destination;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_request);
        initUI();
    }

    void initUI() {

        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");

        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        reference.orderByChild("timestamp").limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order = dataSnapshot.getValue(OrderModel.class);
                destination = order.getTo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mWarning = findViewById(R.id.warning);

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(DeliveryRequestActivity.this, SummaryActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        next = (ImageButton) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(DeliveryRequestActivity.this, AwaitingOffersActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        // ListView Open
        listView = findViewById(R.id.lv_delivery_request);
        initArray();
    }

    private void initArray() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TripModel trip = snapshot.getValue(TripModel.class);

                    assert trip != null;

                    if(trip.getFrom().equals(destination)){
                        arrayList.add(trip);
                    }
                }
                adapter = new DeliveryRequest_Adapter(DeliveryRequestActivity.this, arrayList, order_id);
                if(adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                    mWarning.setText("There are no any mathed user.");
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
