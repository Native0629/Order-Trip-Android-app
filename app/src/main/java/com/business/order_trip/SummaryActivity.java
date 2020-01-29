package com.business.order_trip;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.StorageReference;

public class SummaryActivity extends AppCompatActivity {
    ImageButton back;
    Button next;
    ImageView productImage;
    String order_id, product_name, image_uri, end_date, price, tax, total_price, from, to, category, weight, status;
    TextView productName, endDate, Price, Tax, totalPrice, From, To, mCategory, mWeight;

    // firebase
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        productImage = findViewById(R.id.picture);
        productName = findViewById(R.id.product_name);
        mCategory = findViewById(R.id.category);
        mWeight= findViewById(R.id.weight);
        endDate = findViewById(R.id.edit_date);
        Price = findViewById(R.id.price);
        Tax = findViewById(R.id.tax);
        totalPrice = findViewById(R.id.total_price);
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);

        Intent intent2 = getIntent();
        order_id = intent2.getStringExtra("order_id");

        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order1 = dataSnapshot.getValue(OrderModel.class);
                image_uri = order1.getImage_url();
                product_name = order1.getProduct_name();
                category = order1.getCategory();
                weight = order1.getWeight();
                from = order1.getFrom();
                to = order1.getTo();
                end_date = order1.getEnd_date();
                price = order1.getPrice();
                tax = order1.getTax();
                total_price = order1.getTotal_price();
                status = order1.getStatus();
                if (!image_uri.equals("null")){
                    Glide.with(getApplicationContext()).load(image_uri).into(productImage);
                }
                if(!weight.equals("")) {
                    mWeight.setText(weight + "kg");
                }
                productName.setText(product_name);
                mCategory.setText(category);
                endDate.setText(end_date);
                Price.setText("US$" + price);
                Tax.setText("US$" + tax);
                totalPrice.setText("US$" + total_price);
                From.setText(from);
                To.setText(to);
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
                Intent myIntent = new Intent(SummaryActivity.this, ProductDetailsActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        next = (Button) findViewById(R.id.btn_publish);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_open();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!image_uri.equals("null")){
                    Intent inf=new Intent(SummaryActivity.this, fullscreenActivity.class);
                    inf.putExtra("image_uri", image_uri);
                    startActivity(inf);
                }
            }
        });
    }

    public void alert_open(){

        final Dialog dialog = new Dialog(SummaryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_order_saved_success);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        Button dialog_btn1 = dialog.findViewById(R.id.btn_save);
        dialog_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent = new Intent(SummaryActivity.this, DeliveryRequestActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *0.85f), dialog.getWindow().getAttributes().height);
    }
}
