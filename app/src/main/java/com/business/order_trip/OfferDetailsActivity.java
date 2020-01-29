package com.business.order_trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.Notification.APIService;
import com.business.order_trip.Notification.Client;
import com.business.order_trip.Notification.Data;
import com.business.order_trip.Notification.MyResponse;
import com.business.order_trip.Notification.Sender;
import com.business.order_trip.Notification.Token;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.OrderTripModel;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetailsActivity extends AppCompatActivity {
    ImageButton back;
    String id, order_id, trip_id, product_name, price, tax, image_uri, end_date;
    // firebase
    DatabaseReference reference;
    private FirebaseUser s_user;
    DatabaseReference chatListRef, chatRef;
    int social_type;
    TextView productName, productPrice, productDetails;
    ImageView mImageView;
    EditText mNotification;
    TextView  mTax;

    //Calender
    TextView editDate;
    DatePickerDialog.OnDateSetListener datePickerDialog;

    // send a message
    String r_userid, date, time;
    //add for notification
    APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        //add for notification
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        //end

        mImageView = findViewById(R.id.picture);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.price);
        productDetails = findViewById(R.id.detaile);
        mTax =  findViewById(R.id.tax);
        editDate = findViewById(R.id.edit_date);

        mNotification = findViewById(R.id.notification);

        Intent intent2 = getIntent();
        order_id = intent2.getStringExtra("order_id");
        trip_id = intent2.getStringExtra("trip_id");

        s_user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order1 = dataSnapshot.getValue(OrderModel.class);
//                social_type = order1.getSocial_type();
                r_userid = order1.getSender_id();

                product_name = order1.getProduct_name();
                price = order1.getPrice();
                tax = order1.getTax();
                end_date = order1.getEnd_date();

//                int price = Integer.parseInt(price.split("-")[0].trim());
                if (!price.equals("")) productPrice.setText("US$ "+price);
                productName.setText(product_name);
                if (!tax.equals(""))  mTax.setText("US$ "+ tax);
                editDate.setText(end_date);

                image_uri= order1.getImage_url();
                if(!image_uri.equals("null")){
                    Glide.with(getApplicationContext()).load(image_uri).into(mImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button = (Button) findViewById(R.id.btn_delivery_offer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add for notification
                notify = true;
                //end

                // Generate OrderTripModel
                id = UUID.randomUUID().toString();
                OrderTripModel order_trip = new OrderTripModel(id, order_id, trip_id);
                FirebaseDatabase.getInstance().getReference("OrderTrips").child(id)
                        .setValue(order_trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });

                // Send a message to a specific user
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                SimpleDateFormat df = new SimpleDateFormat("HH:mm: a");
                date = sdf.format(cal.getTime());
                time = df.format(cal.getTime());

                String msg = mNotification.getText().toString();
                if(!msg.equals("")){
                    sendMessage(s_user.getUid(), r_userid, msg, date, time);
                } else{
                    Toast.makeText(OfferDetailsActivity.this, "You can't send empty message.", Toast.LENGTH_LONG).show();
                }
                mNotification.setText("");
            }
        });

        // Calendar for date
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMM dd,yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
    }

    private void sendMessage (String sender, String receiver, String message, String date, String time){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("date", date);
        hashMap.put("time", time);
        hashMap.put("type", 1);

        reference.child("Chats").push().setValue(hashMap);

        // add user to chat fragment
        chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(r_userid)
                .child(s_user.getUid());


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(s_user.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatListRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(s_user.getUid()).child(r_userid);
        chatListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatListRef.child("id").setValue(r_userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //add for notification
        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(s_user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify){
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // add for notification
    private void sendNotification (String receiver, String username, String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(s_user.getUid(), R.mipmap.ic_launcher, username+": " + message, "New Message", r_userid);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200){

                                    if(response.body().success != 1){
                                        Toast.makeText(OfferDetailsActivity.this, "Failed", Toast.LENGTH_LONG ).show();
                                    }else{
                                        Toast.makeText(OfferDetailsActivity.this, "success", Toast.LENGTH_LONG ).show();
                                    }
                                }else{
//                                    Toast.makeText(MessageDetailsActivity.this, response.code()+"//"+response.body(), Toast.LENGTH_LONG ).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Toast.makeText(OfferDetailsActivity.this, call+"//"+t, Toast.LENGTH_LONG ).show();
                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //end

    //add for Notification
    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(s_user.getUid()).setValue(token1);
    }
}
