package com.business.order_trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class OrderPublishActivity extends AppCompatActivity {
    ImageButton back;
    TextView next;
    static int ORDER_SEARCH_CATEGORY = 901;
    String receiveValue;
    EditText et_category, ev_city;
    private RadioButton ch1,ch2,ch3,ch4,ch5,ch6,ch7;
    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6, checkbox7, checkbox8, checkbox9, checkbox10 ;
    int social_type;
    //Firebase
    private FirebaseAuth auth;
    String id, weight="", category="", country, city, destination, end_date, product_name, details, image_url, price, tax, total_price, quantity, sender_id, delive_id,
            trip_id, from, to, timestamp, imagePath, status;
    String result1= "", result2= "", result3= "", result4= "", result5= "", result6= "", result7= "", result8= "", result9= "", result10= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_publish);

        //Firebase
        auth = FirebaseAuth.getInstance();

        ch1 = findViewById(R.id.ch1);
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "1"; }
            }
        });
        ch2 = findViewById(R.id.ch2);
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "3";}
            }
        });
        ch3 = findViewById(R.id.ch3);
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "5";}
            }
        });
        ch4 = findViewById(R.id.ch4);
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "7";}
            }
        });
        ch5 = findViewById(R.id.ch5);
        ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "10";}
            }
        });
        ch6 = findViewById(R.id.ch6);
        ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "15";}
            }
        });
        ch7 = findViewById(R.id.ch7);
        ch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {weight = "23";}
            }
        });

        checkbox1 = findViewById(R.id.checkbox1);
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result1 = "Electronics, "; }
            }
        });
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result2 = "Clothes, ";}
            }
        });
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result3 = "Medicine, ";}
            }
        });
        checkbox4 = findViewById(R.id.checkbox4);
        checkbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result4 = "Toys, ";}
            }
        });
        checkbox5 = findViewById(R.id.checkbox5);
        checkbox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result5 = "10, ";}
            }
        });
        checkbox6 = findViewById(R.id.checkbox6);
        checkbox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result6 = "15, ";}
            }
        });
        checkbox7 = findViewById(R.id.checkbox7);
        checkbox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result7 = "23, ";}
            }
        });

        checkbox8 = findViewById(R.id.checkbox5);
        checkbox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result8 = "10, ";}
            }
        });
        checkbox9 = findViewById(R.id.checkbox6);
        checkbox9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result9 = "15, ";}
            }
        });
        checkbox10 = findViewById(R.id.checkbox7);
        checkbox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {result10 = "23,";}
            }
        });
        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPublishActivity.this, MainActivity.class);
                intent .putExtra("openF1",2);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

        next = (TextView) findViewById(R.id.tv_order_publish);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Store();
                Intent myIntent = new Intent(OrderPublishActivity.this, ProductDetailsActivity.class);
                myIntent.putExtra("order_id", id);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void Store() {
        id = UUID.randomUUID().toString();
        category = result1 + result2 +result3+result4+result5+result6+result7+result8+result9+result10;
//        category = category.substring(0, category.length()-1);

        country = "";
        city = "";
        destination = "";
        end_date = "";
        product_name = "";
        details = "";
        image_url = "null";
        price = "";
        tax = "";
        total_price = "";
        quantity = "";
        delive_id = "";
        trip_id = "";
        from = "";
        to = "";
        sender_id = auth.getUid();
        social_type = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        timestamp = sdf.format(cal.getTime());
        imagePath = "null";
        status = "No delived offer yet";
        OrderModel order = new OrderModel(social_type ,id, weight, category, country, city, destination, end_date, product_name, details, image_url, price, tax, total_price, quantity, sender_id, delive_id, trip_id, from, to, timestamp, imagePath, status);

        FirebaseDatabase.getInstance().getReference("Orders").child(id)
                .setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                        Toast.makeText(OrderPublishActivity.this,  "success", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(OrderPublishActivity.this,  "Order failed, Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
