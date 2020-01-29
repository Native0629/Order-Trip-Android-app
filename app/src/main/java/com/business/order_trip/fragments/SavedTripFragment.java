package com.business.order_trip.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.business.order_trip.MainActivity;
import com.business.order_trip.R;
import com.business.order_trip.adapters.SavedOrder_Adapter;
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


public class SavedTripFragment extends Fragment {

    MainActivity mainActivity;
    ArrayList<TripModel> arrayList = new ArrayList<>();
    Saved_Trip_Adapter adapter;
    ListView lvSavedTrip;
    View view;
    TextView mWarning;

    public SavedTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved_trip, container, false);
        mWarning = view.findViewById(R.id.warning);
        initUI();
        return  view;
    }

    void initUI() {
        // ListView Open
        lvSavedTrip = view.findViewById(R.id.lv_saved_trip);
        mainActivity = (MainActivity)getActivity();
        adapter = new Saved_Trip_Adapter(mainActivity, arrayList);
        lvSavedTrip.setAdapter(adapter);
        initArray();
    }

    private void initArray() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trips");
        reference.orderByChild("timestamp").limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TripModel trip = snapshot.getValue(TripModel.class);

                    assert trip != null;
                    assert currentUser != null;

                    if(trip.getTrip_user_id().equals(currentUser.getUid())){
                        if (trip.getStatus().equals("on")){
                            arrayList.add(trip);
                        }
                    }
                }

                adapter = new Saved_Trip_Adapter(mainActivity, arrayList);
                if(adapter.getCount() == 0){
                    lvSavedTrip.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                    mWarning.setText("There are no any saved trips.");
                }else{
                    lvSavedTrip.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);
                }
                lvSavedTrip.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
