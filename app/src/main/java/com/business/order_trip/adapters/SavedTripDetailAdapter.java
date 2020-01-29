package com.business.order_trip.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.business.order_trip.OfferDetailsActivity;
import com.business.order_trip.R;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class SavedTripDetailAdapter extends BaseAdapter {
    ArrayList<OrderModel> arrayList;
    Context context;
    String trip_id;
    String imageUri;
    public SavedTripDetailAdapter(Context _context, ArrayList<OrderModel> _arrayList, String id) {
        context = _context;
        arrayList = _arrayList;
        trip_id = id;
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
        final OrderModel order = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_home_trip;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        ImageView ivAvatar = view.findViewById(R.id.profile_image);
        TextView  userName = view.findViewById(R.id.user_name);

        TextView  date = view.findViewById(R.id.date);
        TextView  weight = view.findViewById(R.id.weight);
        TextView  category = view.findViewById(R.id.category);

        ImageView ivProduct = view.findViewById(R.id.image);
        TextView  ProductName = view.findViewById(R.id.product_name);
        TextView  from = view.findViewById(R.id.from);
        TextView  to = view.findViewById(R.id.to);
        TextView  endDate = view.findViewById(R.id.end_date);
        TextView  tax = view.findViewById(R.id.tax);
        TextView  status = view.findViewById(R.id.status);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(order.sender_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                assert user != null;
                userName.setText(user.getUsername());
                if(!user.getImageUri().equals("null")){
                    Glide.with(context.getApplicationContext()).load(user.getImageUri()).into(ivAvatar);
                }else{
                    ivAvatar.setImageResource(R.drawable.person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        date.setText(order.timestamp);
        ProductName.setText(order.product_name);
        from.setText(String.valueOf(order.from));
        to.setText(String.valueOf(order.to));
        endDate.setText(order.end_date);
        tax.setText("US$" + order.tax);
        if(!order.weight.equals("")) weight.setText(order.weight + "kg");
        category.setText(order.category);
        status.setText(String.valueOf(order.status));
        if(!order.getImage_url().equals("null")){
            Glide.with(context.getApplicationContext()).load(order.image_url).into(ivProduct);
        }

        final Button btn = (Button)view.findViewById(R.id.btn_delivery_offer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfferDetailsActivity.class);
                intent.putExtra("order_id", order.id);
                intent.putExtra("trip_id", trip_id);

                context.startActivity(intent);
            }
        });

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OrderModel order = arrayList.get(i);
                if(!order.getImage_url().equals("null")){
                    Intent inf = new Intent(context, fullscreenActivity.class);
                    inf.putExtra("image_uri", order.getImage_url());
                    context.startActivity(inf);
                }
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(order.sender_id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        imageUri = user.imageUri;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!imageUri.equals("null")){
                    Intent inf=new Intent(context, fullscreenActivity.class);
                    inf.putExtra("image_uri", imageUri);
                    context.startActivity(inf);
                }
            }
        });

        return view;
    }
}
