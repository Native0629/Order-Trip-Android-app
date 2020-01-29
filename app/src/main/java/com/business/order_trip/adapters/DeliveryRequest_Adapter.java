package com.business.order_trip.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.OrderPublishActivity;
import com.business.order_trip.ProfileanotherDetailActivity;
import com.business.order_trip.R;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.OrderTripModel;
import com.business.order_trip.models.TripModel;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.UUID;

public class DeliveryRequest_Adapter extends BaseAdapter {
    ArrayList<TripModel> arrayList;
    Context context;
    String user_id, id, order_id, trip_id;

    DeliveryRequest_Adapter() {
        context = null;
        arrayList = null;
    }

    public DeliveryRequest_Adapter(Context _context, ArrayList<TripModel> _arrayList, String orderId) {
        context = _context;
        arrayList = _arrayList;
        order_id = orderId;
    }

    @Override
    public int getCount() {
        if (arrayList == null)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TripModel trip = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_order_request;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView from = view.findViewById(R.id.from);
        TextView date = view.findViewById(R.id.date);
        TextView userName= view.findViewById(R.id.user_name);
        ImageView ivAvatar = view.findViewById(R.id.profile_image);
        TextView trip_count = view.findViewById(R.id.trip_count);
        SimpleRatingBar tripRate = view.findViewById(R.id.rating);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(trip.trip_user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user_id = user.getId();
                userName.setText(user.getUsername());
                String imageUri = user.getImageUri();
                trip_count.setText("(" + String.valueOf(user.getPre_trip_count()) + ")");
                if(!user.getImageUri().equals("null")){
                    Glide.with(context).load(imageUri).into(ivAvatar);
                }else{
                    ivAvatar.setImageResource(R.drawable.person);
                }

                int preTripCount = user.getPre_trip_count();

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

            }
        });

        from.setText("Traveling from "+trip.from);
        date.setText("Delivery date "+trip.date);

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        id = UUID.randomUUID().toString();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    trip_id = trip.id;

                    OrderTripModel order_trip = new OrderTripModel(id, order_id, trip_id);
                    FirebaseDatabase.getInstance().getReference("OrderTrips").child(id)
                            .setValue(order_trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                               Toast.makeText(OrderPublishActivity.this,  "success", Toast.LENGTH_LONG).show();
                            }else {
//                               Toast.makeText(OrderPublishActivity.this,  "Order failed, Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("OrderTrips").child(id);
                    deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // If the value to delete is not unique, there may be multiple children
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }

            }
        });

        final LinearLayout button = (LinearLayout)view.findViewById(R.id.ll_more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileanotherDetailActivity.class);
                intent.putExtra("user_id", user_id);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
