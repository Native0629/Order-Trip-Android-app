package com.business.order_trip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.business.order_trip.adapters.My_Support_Request_Adapter;
import com.business.order_trip.models.MessageModel;

import java.util.ArrayList;

public class MySupportRequestActivity extends AppCompatActivity {
    ArrayList<MessageModel> arrayList = new ArrayList<>();
    My_Support_Request_Adapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_support_request);
        initUI();
    }

    void initUI() {
        ImageButton ibback = (ImageButton) findViewById(R.id.ib_back);
        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView support_user_id = (TextView)findViewById(R.id.tv_new_request);
        support_user_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MySupportRequestActivity.this, ContactUsActivity.class);
                startActivity(myIntent);
                finish();

            }
        });

        // ListView Open
        listView = findViewById(R.id.lv_support_request);
        adapter = new My_Support_Request_Adapter(MySupportRequestActivity.this, arrayList);
        listView.setAdapter(adapter);
        initArray();

    }

    private void initArray() {

        MessageModel model = new MessageModel(1, "Gold Star", "May 13,2017","May 13,2017", "13:30 PM","","Android Mobile Phone with 32G"," Hi, Jaqueline Falcao, I am traveling to Sao Paulo ....","Hi, Jaqueline Falcao, I am traveling to Sao Paulo ....","User_id: 12313", 1);
        arrayList.add(model);
        adapter.notifyDataSetChanged();
    }
}
