package com.business.order_trip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.order_trip.adapters.SavedTripDetailAdapter;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.TripModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SavedTripDetailActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    ImageButton back;
    ArrayList<OrderModel> arrayList = new ArrayList<>();
    SavedTripDetailAdapter adapter;
    ListView listView;
    View ftView;
    TextView mDate, mFrom, mTo, mReward, mWarning;
    // firebase
    DatabaseReference reference;
    String trip_id, id, from, to, date, reward, trip_destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_trip_detail);

        progressBar = findViewById(R.id.progressBar);

        mWarning = findViewById(R.id.warning);
        mDate = findViewById(R.id.trip_date);
        mFrom = findViewById(R.id.trip_from);
        mTo = findViewById(R.id.trip_to);
        mReward = findViewById(R.id.reward);

        Intent intent = getIntent();
        trip_id = intent.getStringExtra("trip_id");

        reference = FirebaseDatabase.getInstance().getReference().child("Trips").child(trip_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TripModel trip1 = dataSnapshot.getValue(TripModel.class);

                date = trip1.getDate();
                from = trip1.getFrom();
                to = trip1.getTo();
                reward = trip1.getReward();

                mDate.setText(date);
                mFrom.setText(from);
                mTo.setText(to);
                mReward.setText("US$" + reward);

                trip_destination = mTo.getText().toString();
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

        ImageButton button1 = (ImageButton) findViewById(R.id.ib_more);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alert_open1();
            }
        });

        ImageButton button2 = (ImageButton) findViewById(R.id.ib_box);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                alert_share();
            }
        });
        // ListView Open
        listView = findViewById(R.id.lv_saved_trip_details);

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    oldestPostId = snapshot.getKey();
                    OrderModel order = snapshot.getValue(OrderModel.class);

                    String order_destination = order.getTo();
                    assert order_destination != null;
                    if (order_destination.equals(trip_destination)){
                        arrayList.add(order);
                    }
                }

                adapter = new SavedTripDetailAdapter(SavedTripDetailActivity.this, arrayList, trip_id);
                if(adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    mWarning.setText("There are no matched orders.");
                }else{
                    listView.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);

                    listView.setAdapter(adapter);

                    ViewGroup vg = listView;
                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, vg);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams par = listView.getLayoutParams();
                    par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
                    listView.setLayoutParams(par);
                    listView.requestLayout();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void alert_open1(){

       final Dialog dialog = new Dialog(SavedTripDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_trip_detail_cancel);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        LinearLayout dialog_btn1 = dialog.findViewById(R.id.ll_btn1);
        dialog_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    reference.child("status").setValue("canceled");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SavedTripDetailActivity.this, MainActivity.class);
                intent .putExtra("openF2",2);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
//        LinearLayout dialog_btn2 = dialog.findViewById(R.id.ll_btn2);
//        dialog_btn2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Toast.makeText(SavedTripDetailActivity.this,"Set pause notifications", Toast.LENGTH_SHORT).show();
//            }
//        });

        LinearLayout ll_cancel = dialog.findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener(){
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

    public void alert_share(){
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = "Your body here";
        String shareSub = "Your Subject here";
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
        startActivity(Intent.createChooser(myIntent, "Select the socials"));
    }
}
