package com.business.order_trip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.business.order_trip.R;
import com.business.order_trip.models.TripModel;

import java.util.ArrayList;

public class CanceldTrip_Adapter extends BaseAdapter {
    ArrayList<TripModel> arrayList;
    Context context;

    CanceldTrip_Adapter() {
        context = null;
        arrayList = null;
    }

    public CanceldTrip_Adapter(Context _context, ArrayList<TripModel> _arrayList) {
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
        final TripModel TripModel = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_trips_cancel;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView from = (TextView)view.findViewById(R.id.from);
        TextView to = (TextView)view.findViewById(R.id.to);
        TextView date = (TextView)view.findViewById(R.id.date);

        from.setText(TripModel.from);
        to.setText(String.valueOf(TripModel.to));
        date.setText(TripModel.date);


//        final Button btn_delete = (Button)view.findViewById(R.id.btn_home_order);
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProductDetailsActivity.class);
//                context.startActivity(intent);
//            }
//        });
        return view;
    }
}
