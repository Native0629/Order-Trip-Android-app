package com.business.order_trip.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.business.order_trip.adapters.HomeTrip_Adapter;
import com.business.order_trip.helpers.AdressFromActivity;
import com.business.order_trip.helpers.AdressToActivity;
import com.business.order_trip.R;
import com.business.order_trip.SavedTripDetailActivity;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.TripModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TripAddFragment extends Fragment {

    View view;
    static int ORDER_SEARCH_CATEGORY = 901;
    String to_city, from_city;
    EditText et_from, et_to, editDate;
    EditText edit_date;
    DatePickerDialog.OnDateSetListener datePickerDialog;
    Button trip_app, save_btn;
    Switch dateSwitch;
    //Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    int email_status, upcoming_trip_count = 0;
    String id, from, to, date, timestamp, status, trip_user_id, order_id, order_count , reward;

    public TripAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trip_add, container, false);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        initUI();

        return view;
    }

    void initUI() {

        // City search
        et_from = (EditText) view.findViewById(R.id.ev_city_from);
        et_from.setKeyListener(null);
        et_from.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(getActivity(), AdressFromActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });

        et_to = (EditText) view.findViewById(R.id.ev_city_to);
        et_to.setKeyListener(null);
        et_to.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), AdressToActivity.class);
                startActivityForResult(intent, ORDER_SEARCH_CATEGORY);
            }
        });
        final Calendar myCalendar = Calendar.getInstance();

        // Calender
        edit_date= (EditText) view.findViewById(R.id.edit_date);
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM d, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edit_date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dateSwitch = view.findViewById(R.id.switch1);
        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new DatePickerDialog(getActivity(), datePickerDialog, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else{
                    edit_date.setText("");
                }
            }
        });

        // Add event
        trip_app = (Button) view.findViewById(R.id.btn_add);
        trip_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = et_from.getText().toString().trim();
                to = et_to.getText().toString().trim();
                date = edit_date.getText().toString().trim();
                open_alert();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == ORDER_SEARCH_CATEGORY) {
            if (resultCode == RESULT_OK) {
                to_city = data.getStringExtra("city_to");
                et_to.setText(to_city);
            }
            else if(resultCode == RESULT_CANCELED){
                from_city = data.getStringExtra("city_from");
                et_from.setText(from_city);
            }
        }
    }

    private void open_alert() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_trip_saved_success);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        TextView cell_from = dialog.findViewById(R.id.from);
        TextView cell_to = dialog.findViewById(R.id.to);
        TextView cell_date = dialog.findViewById(R.id.end_date);

        cell_from.setText(from);
        cell_to.setText(to);
        cell_date.setText(date);

        save_btn = dialog.findViewById(R.id.btn_save);
        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Store();
                Intent intent = new Intent(getActivity(), SavedTripDetailActivity.class);
                intent.putExtra("trip_id", id);
                getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *0.9f), dialog.getWindow().getAttributes().height);
    }

    private void Store() {

        id = UUID.randomUUID().toString();
        trip_user_id = user.getUid();
        order_id = "";
        order_count = "0" ;
        reward = "0";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        timestamp = sdf.format(cal.getTime());
        status = "on";

        TripModel trip = new TripModel(id, from, to, date, timestamp, status, trip_user_id, order_id, order_count , reward);

        FirebaseDatabase.getInstance().getReference("Trips").child(id)
                .setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else {
                    Toast.makeText(getActivity(),  "Order failed, Try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Trips");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TripModel trip1 = snapshot.getValue(TripModel.class);

                    if (trip1.getTrip_user_id() == trip_user_id){
                        upcoming_trip_count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Users").child(trip_user_id);

        try {
            refer.child("upcoming_trip_count").setValue(upcoming_trip_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
