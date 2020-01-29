package com.business.order_trip.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.business.order_trip.R;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.TripModel;

import java.util.ArrayList;

public class UpcomingTrips_Adapter extends BaseAdapter {
    ArrayList<TripModel> arrayList;
    Context context;
    TextView mWarning;
    String imageUri;
    UpcomingTrips_Adapter() {
        context = null;
        arrayList = null;
    }

    public UpcomingTrips_Adapter(Context _context, ArrayList<TripModel> _arrayList) {
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
            int resource = R.layout.cell_upcoming_trips;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView from = (TextView)view.findViewById(R.id.from);
        TextView to = (TextView)view.findViewById(R.id.to);
        TextView date = (TextView)view.findViewById(R.id.date);

        from.setText("Traveling from "+trip.from);
        to.setText(trip.to);
        date.setText("Delivery date "+trip.date);

        final Button button = (Button)view.findViewById(R.id.btn_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                context.startActivity(browserIntent);
            }
        });

//        ivAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!imageUri.equals("null")){
//                    Intent inf=new Intent(context, fullscreenActivity.class);
//                    inf.putExtra("image_uri", imageUri);
//                    context.startActivity(inf);
//                }
//            }
//        });

        return view;
    }
}
