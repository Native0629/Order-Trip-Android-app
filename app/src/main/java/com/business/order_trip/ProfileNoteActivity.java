package com.business.order_trip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.business.order_trip.adapters.HomeTrip_Adapter;
import com.business.order_trip.adapters.ProfileNote_Adapter;
import com.business.order_trip.models.Chat;
import com.business.order_trip.models.MessageModel;
import com.business.order_trip.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileNoteActivity extends AppCompatActivity {
    ArrayList<Chat> arrayList = new ArrayList<>();
    ProfileNote_Adapter adapter;
    ListView listView;
    private FirebaseUser user;
    TextView  mWarning;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_note);
        user = FirebaseAuth.getInstance().getCurrentUser();
        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // ListView Open
        mWarning = findViewById(R.id.warning);
        listView = findViewById(R.id.lv_note);
        initArray();
    }

    private void initArray() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getType() == 1){
                        if (chat.getReceiver() == user.getUid()){
                            assert chat != null;
                            arrayList.add(chat);
                        }
                    }
                }

                adapter = new ProfileNote_Adapter(ProfileNoteActivity.this, arrayList);

                if(adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    mWarning.setVisibility(View.VISIBLE);
                    mWarning.setText("There are no any nofitications");

                }else{
                    listView.setVisibility(View.VISIBLE);
                    mWarning.setVisibility(View.GONE);
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
