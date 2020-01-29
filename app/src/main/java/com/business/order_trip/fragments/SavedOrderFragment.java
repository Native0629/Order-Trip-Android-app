package com.business.order_trip.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.business.order_trip.MainActivity;
import com.business.order_trip.R;
import com.business.order_trip.adapters.SavedOrder_Adapter;
import com.business.order_trip.models.OrderModel;
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


public class SavedOrderFragment extends Fragment {

    MainActivity mainActivity;
    ListView lvSavedOrder;
    ArrayList<OrderModel> arrayList = new ArrayList<>();
    SavedOrder_Adapter adapter;
    View view;
    TextView mWarning;
    private ProgressBar progressBar;
    public SavedOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved_order, container, false);
        mWarning = view.findViewById(R.id.warning);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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

        // ListView Open
        lvSavedOrder = view.findViewById(R.id.lv_saved_order);
        mainActivity = (MainActivity)getActivity();

        return  view;
    }

    private void initArray() {
        progressBar.setVisibility(View.GONE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel order = snapshot.getValue(OrderModel.class);

                    assert order != null;
                    assert currentUser != null;

                    if(order.getSender_id().equals(currentUser.getUid())){
                        if (!order.getStatus().equals("deleted"))
                            arrayList.add(order);
                    }
                }

                adapter = new SavedOrder_Adapter(mainActivity, arrayList);
                if(adapter.getCount() == 0){
                    lvSavedOrder.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                    mWarning.setText("There are no any saved orders.");
                }else{
                    lvSavedOrder.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);
                }
                lvSavedOrder.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
