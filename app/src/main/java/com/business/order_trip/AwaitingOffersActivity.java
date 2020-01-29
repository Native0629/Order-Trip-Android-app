package com.business.order_trip;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AwaitingOffersActivity extends AppCompatActivity {
    ImageButton back;
    TextView productName, endDate, Price, Tax, totalPrice, From, To, mCategory, mWeight, mCount, mStatus;
    ImageView productImage;

    DatabaseReference reference;
    String order_id, id, weight, category, country, city, destination, end_date, product_name, details, image_uri, price, tax, total_price, quantity, sender_id, delive_id,
            trip_id, from, to, timestamp, imagePath, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awaiting_offers);

        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");

        productImage = findViewById(R.id.picture);
        productName = findViewById(R.id.product_name);
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);
        endDate = findViewById(R.id.date);
        mCategory = findViewById(R.id.category);
        mWeight= findViewById(R.id.weight);
        Price = findViewById(R.id.price);
        Tax = findViewById(R.id.tax);
        totalPrice = findViewById(R.id.total_price);
        mCount =  findViewById(R.id.count);
        mStatus=  findViewById(R.id.status);

        if (order_id != null){
            reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    OrderModel order1 = dataSnapshot.getValue(OrderModel.class);
                    destination = order1.getTo();

                    image_uri = order1.getImage_url();
                    product_name = order1.getProduct_name();
                    category = order1.getCategory();
                    weight = order1.getWeight();
                    from = order1.getFrom();
                    to = order1.getTo();
                    end_date = order1.getEnd_date();
                    price = order1.getPrice();
                    tax = order1.getTax();
                    total_price = order1.getTotal_price();//Integer.parseInt(price) + Integer.parseInt(tax);
                    quantity = order1.getQuantity();
                    status = order1.getStatus();
                    if (!image_uri.equals("null")){
                        Glide.with(getApplicationContext()).load(image_uri).into(productImage);
                    }
                    productName.setText(product_name);
                    if(!weight.equals("")) mWeight.setText(weight+"kg");
                    mCategory.setText(category);
                    endDate.setText(end_date);
                    Price.setText("US$ " + price);
                    Tax.setText("US$ " + tax);
                    totalPrice.setText("US$ " + total_price + "*" + quantity);
                    From.setText(from);
                    To.setText(to);
                    mCount.setText(quantity);
                    mStatus.setText(status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        productImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!image_uri.equals("null")){
                    Intent inf=new Intent(AwaitingOffersActivity.this, fullscreenActivity.class);
                    inf.putExtra("image_uri", image_uri);
                    startActivity(inf);
                }
            }
        });

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(AwaitingOffersActivity.this, DeliveryRequestActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        RelativeLayout button3 = (RelativeLayout) findViewById(R.id.rl_request_delivery);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(AwaitingOffersActivity.this, DeliveryRequestActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
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
    }

    public void alert_open1(){

        final Dialog dialog = new Dialog(AwaitingOffersActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_awaiting_offer_more);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        LinearLayout dialog_btn1 = dialog.findViewById(R.id.ll_btn1);
        dialog_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(AwaitingOffersActivity.this, EditOrderActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });
        LinearLayout dialog_btn2 = dialog.findViewById(R.id.ll_btn2);
        dialog_btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(AwaitingOffersActivity.this, ContactUsActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        LinearLayout dialog_btn3 = dialog.findViewById(R.id.ll_btn3);
        dialog_btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(AwaitingOffersActivity.this,"Go to Help Center", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout dialog_btn4 = dialog.findViewById(R.id.ll_btn4);
        dialog_btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        dialog.show();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width()*1f), dialog.getWindow().getAttributes().height);
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
