package com.business.order_trip.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.business.order_trip.R;
import com.business.order_trip.SavedTripDetailActivity;
import com.business.order_trip.models.TripModel;

import java.util.ArrayList;

public class Saved_Trip_Adapter extends BaseAdapter {
    ArrayList<TripModel> arrayList;
    Context context;

    Saved_Trip_Adapter() {
        context = null;
        arrayList = null;
    }

    public Saved_Trip_Adapter(Context _context, ArrayList<TripModel> _arrayList) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TripModel trip = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_saved_trip;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView from = (TextView)view.findViewById(R.id.trip_from);
        TextView to = (TextView)view.findViewById(R.id.trip_to);
        TextView date = (TextView)view.findViewById(R.id.trip_date);
        TextView count_order = (TextView)view.findViewById(R.id.count_order);
//        TextView count_deliver = (TextView)view.findViewById(R.id.count_deliver);
        TextView count_earning = (TextView)view.findViewById(R.id.count_earning);

        from.setText(trip.from);
        to.setText(String.valueOf(trip.to));
        date.setText(trip.date);
        count_order.setText(trip.order_count);
//        count_deliver.setText(String.valueOf(trip.count_deliver));
        count_earning.setText("US$" + trip.reward);

        final String trip_id = trip.id;
        final LinearLayout btn = (LinearLayout)view.findViewById(R.id.ll_trip_detail);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SavedTripDetailActivity.class);
                intent.putExtra("trip_id", trip_id);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
