package com.business.order_trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.business.order_trip.helpers.AdressFromActivity;
import com.business.order_trip.helpers.AdressToActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DeliveryDetailsActivity extends AppCompatActivity {
    ImageButton back;
    Button next;
    String order_id;
    EditText productFrom, productTo, mDestination, mCountry;
    static int ORDER_SEARCH_CATEGORY = 901;
    TextView editDate;
    DatePickerDialog.OnDateSetListener datePickerDialog;
    Switch dateSwitch;
    String  country, destination;
    CountryPicker picker;
    // firebase
    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent2 = getIntent();
        order_id = intent2.getStringExtra("order_id");
        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);

        mDestination = findViewById(R.id.destination);
        mCountry= findViewById(R.id.country);

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(DeliveryDetailsActivity.this, SummaryActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                try {
                    String from = productFrom.getText().toString().trim();
                    String to = productTo.getText().toString().trim();
                    String end_date = editDate.getText().toString().trim();


                    destination = mDestination.getText().toString().trim();
                    country = mCountry.getText().toString().trim();

                    reference.child("from").setValue(from);
                    reference.child("to").setValue(to);
                    reference.child("end_date").setValue(end_date);
                    reference.child("country").setValue(country);
                    reference.child("destination").setValue(destination);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // Country address search
        mCountry.setKeyListener(null);
        mCountry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                picker = CountryPicker.newInstance("Select Country");
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        String selectedItem = name;
                        mCountry.setText(selectedItem);
                        picker.dismiss();
                    }
                });
            }
        });

        // City search
        productFrom = (EditText) findViewById(R.id.from);
        productFrom.setKeyListener(null);
        productFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(DeliveryDetailsActivity.this, AdressFromActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });

        productTo = (EditText) findViewById(R.id.to);
        productTo.setKeyListener(null);
        productTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(DeliveryDetailsActivity.this, AdressToActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });

        // Calendar for date
        editDate = findViewById(R.id.edit_date);
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        dateSwitch= findViewById(R.id.switch1);
        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new DatePickerDialog(DeliveryDetailsActivity.this, datePickerDialog, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else{
                    editDate.setText("");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == ORDER_SEARCH_CATEGORY) {
            if (resultCode == RESULT_OK) {
                if(!data.getStringExtra("city_to").equals("null")) {
                    String city_to = data.getStringExtra("city_to");
                    productTo.setText(city_to);
                }
            }
            else if(resultCode == RESULT_CANCELED){
                if(!data.getStringExtra("city_from").equals("null")){

                    String city_from = data.getStringExtra("city_from");
                    productFrom.setText(city_from);
                }
            }
        }
    }
}
