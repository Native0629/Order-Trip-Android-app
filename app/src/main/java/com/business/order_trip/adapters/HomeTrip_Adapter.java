package com.business.order_trip.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.business.order_trip.DeliveryDetailsActivity;
import com.business.order_trip.MainActivity;
import com.business.order_trip.OfferDetailsActivity;
import com.business.order_trip.ProductDetailsActivity;
import com.business.order_trip.ProfileSettingPhoneActivity;
import com.business.order_trip.R;
import com.business.order_trip.SavedTripDetailActivity;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeTrip_Adapter extends BaseAdapter {
    ArrayList<OrderModel> arrayList;
    Context context;
    private String imageUri;
    FirebaseUser user1;
    int phone_verify;

    HomeTrip_Adapter() {
        context = null;
        arrayList = null;
    }

    public HomeTrip_Adapter(Context _context, ArrayList<OrderModel> _arrayList) {
        context = _context;
        arrayList = _arrayList;
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

    @SuppressLint("RestrictedApi")
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
        TextView mPrice = view.findViewById(R.id.price);
        ImageView ivProduct = view.findViewById(R.id.image);
        TextView  ProductName = view.findViewById(R.id.product_name);
        TextView from = (TextView)view.findViewById(R.id.from);
        TextView to = (TextView)view.findViewById(R.id.to);
        TextView  endDate = view.findViewById(R.id.end_date);
        TextView  tax = view.findViewById(R.id.tax);
        TextView  status = view.findViewById(R.id.status);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(order.sender_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.username);

                if(!user.getImageUri().equals("null")){
                     Glide.with(getApplicationContext()).load(user.getImageUri()).into(ivAvatar);
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
        mPrice.setText("US$ " + order.price);
        tax.setText("US$ " + order.tax);
        if(!order.weight.equals("")) weight.setText(order.weight + "kg");
        category.setText(order.category);
        status.setText(String.valueOf(order.status));

        if(!order.getImage_url().equals("null")){
            Glide.with(getApplicationContext()).load(order.image_url).into(ivProduct);
        }

        user1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user1.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                phone_verify = user.getPhone_status();

                if(!user.getImageUri().equals("null")){
                    Glide.with(getApplicationContext()).load(user.getImageUri()).into(ivAvatar);
                }else{
                    ivAvatar.setImageResource(R.drawable.person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button btn_offer = (Button)view.findViewById(R.id.btn_delivery_offer);
        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phone_verify == 1){
                    Intent intent = new Intent(context, MainActivity.class);
                    intent .putExtra("openF3",3);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                }else{
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_home_trip_verify);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.BOTTOM;
                    window.setAttributes(wlp);

                    LinearLayout dialog_btn1 = dialog.findViewById(R.id.ll_btn1);
                    dialog_btn1.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Intent myIntent = new Intent(context, ProfileSettingPhoneActivity.class);
                            context.startActivity(myIntent);
                            dialog.dismiss();
                        }
                    });

                    LinearLayout dialog_btn2 = dialog.findViewById(R.id.ll_btn2);
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
                        if(!imageUri.equals("null")){
                            Intent inf=new Intent(context, fullscreenActivity.class);
                            inf.putExtra("image_uri", imageUri);
                            context.startActivity(inf);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        return view;
    }
}
