package com.business.order_trip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.business.order_trip.MainActivity;
import com.business.order_trip.R;
import com.business.order_trip.adapters.CanceldTrip_Adapter;
import com.business.order_trip.adapters.Saved_Trip_Adapter;
import com.business.order_trip.models.TripModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripCancelFragment extends Fragment {

    MainActivity mainActivity;
    ArrayList<TripModel> arrayList = new ArrayList<>();
    CanceldTrip_Adapter adapter;
    ListView lvCancelTrip;
    View view;
    TextView mWarning;
    EditText mFrom, mTo;
    public TripCancelFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trip_cancel, container, false);
        mWarning = view.findViewById(R.id.warning);
        initUI();
        return  view;
    }

    void initUI() {
        // ListView Open
        lvCancelTrip = view.findViewById(R.id.lv_cancel_trip);
        mainActivity = (MainActivity)getActivity();
        initArray();

    }

    private void initArray() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trips");
        reference.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TripModel trip = snapshot.getValue(TripModel.class);

                    assert trip != null;
                    assert currentUser != null;

                    if(trip.getTrip_user_id().equals(currentUser.getUid())){
                        if (trip.getStatus().equals("canceled")){
                            arrayList.add(trip);
                        }
                    }
                }

                adapter = new CanceldTrip_Adapter(mainActivity, arrayList);
                if(adapter.getCount() == 0){
                    lvCancelTrip.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                }else{
                    lvCancelTrip.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);
                }
                lvCancelTrip.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}