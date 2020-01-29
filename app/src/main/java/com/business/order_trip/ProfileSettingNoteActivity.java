package com.business.order_trip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.business.order_trip.adapters.ProfileSettingNote_Adapter;
import com.business.order_trip.models.TripModel;

import java.util.ArrayList;

public class ProfileSettingNoteActivity extends AppCompatActivity {
    ArrayList<TripModel> arrayList = new ArrayList<>();
    ProfileSettingNote_Adapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting_note);

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

        listView = findViewById(R.id.lv_note_setting);
        adapter = new ProfileSettingNote_Adapter(ProfileSettingNoteActivity.this, arrayList);
        listView.setAdapter(adapter);
        initArray();

        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
        listView.setAdapter(adapter);
    }

    private void initArray() {

    }

}
