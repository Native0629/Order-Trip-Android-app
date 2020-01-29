package com.business.order_trip.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.business.order_trip.helpers.AdressFromActivity;
import com.business.order_trip.helpers.AdressToActivity;
import com.business.order_trip.MainActivity;
import com.business.order_trip.R;
import com.business.order_trip.adapters.HomeTrip_Adapter;
import com.business.order_trip.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeTripFragment extends Fragment {


    static int ORDER_SEARCH_CATEGORY = 901;
    String to_city = "", from_city = "";
    EditText et_from, et_to;

    private ProgressBar progressBar;
    MainActivity mainActivity;
    ArrayList<OrderModel> arrayList = new ArrayList<>();
    HomeTrip_Adapter adapter;
    ListView listView;
    View view;
    TextView mWarning;

    public HomeTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_trip, container, false);
        mWarning = view.findViewById(R.id.warning);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        initUI();
        return view;
    }

    void initUI() {
        // City search
        et_from = (EditText) view.findViewById(R.id.ev_city_from);
        et_from.setKeyListener(null);
        et_from.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(getActivity(), AdressFromActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });

        et_to = (EditText) view.findViewById(R.id.ev_city_to);
        et_to.setKeyListener(null);
        et_to.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(getActivity(), AdressToActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });

        // ListView Open
        listView= view.findViewById(R.id.lv_home_trip);
        mainActivity = (MainActivity)getActivity();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == ORDER_SEARCH_CATEGORY) {
            if (resultCode == RESULT_OK) {
                if(!data.getStringExtra("city_to").equals("null")) {
                    to_city = data.getStringExtra("city_to");
                    et_to.setText(to_city);
                    initArray();
                }
            }
            else if(resultCode == RESULT_CANCELED){
                if(!data.getStringExtra("city_from").equals("null")){
                    from_city = data.getStringExtra("city_from");
                    et_from.setText(from_city);
                    initArray();
                }
            }
        }
    }

    private void initArray() {
        progressBar.setVisibility(View.GONE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.orderByChild("timestamp").limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel order = snapshot.getValue(OrderModel.class);

                    if (!order.getStatus().equals("deleted")){
                        assert order != null;
                        if (to_city != null || from_city !=null){
                            if(to_city == null && from_city != null){
                                if (order.getFrom().equals(from_city)){
                                    arrayList.add(order);
                                }
                            }else if(to_city != null && from_city == null){
                                if (order.getTo().equals(to_city)){
                                    arrayList.add(order);
                                }
                            }else{
                                if (order.getFrom().equals(from_city) && order.getTo().equals(to_city)){
                                    arrayList.add(order);
                                }
                            }
                        }else{
                            arrayList.add(order);
                        }
                    }
                }

                adapter = new HomeTrip_Adapter(mainActivity, arrayList);
                if(adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);

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
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
