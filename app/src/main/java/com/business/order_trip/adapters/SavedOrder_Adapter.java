package com.business.order_trip.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.business.order_trip.AwaitingOffersActivity;
import com.business.order_trip.R;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.OrderModel;
import com.business.order_trip.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedOrder_Adapter extends BaseAdapter {
    ArrayList<OrderModel> arrayList;
    Context context;
    String order_id;

    SavedOrder_Adapter() {
        context = null;
        arrayList = null;
    }

    public SavedOrder_Adapter(Context _context, ArrayList<OrderModel> _arrayList) {
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
        final OrderModel order = arrayList.get(i);
        order_id = order.getId();
        if (view == null) {
            int resource = R.layout.cell_saved_order;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView product_name = (TextView)view.findViewById(R.id.product_name);
        TextView from = (TextView)view.findViewById(R.id.from);
        TextView to = (TextView)view.findViewById(R.id.to);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView status = (TextView)view.findViewById(R.id.status);
        TextView price = (TextView)view.findViewById(R.id.price);
        ImageView image = view.findViewById(R.id.image);
        TextView  weight = view.findViewById(R.id.weight);
        TextView  category = view.findViewById(R.id.category);

        product_name.setText(order.product_name);
        from.setText(order.from);
        to.setText(String.valueOf(order.to));
        date.setText(order.end_date);
        status.setText(order.status);
        price.setText("US$ " + order.price);
        weight.setText(order.weight + "kg");
        category.setText(order.category);
        Glide.with(context.getApplicationContext()).load(order.image_url).into(image);

        final LinearLayout btn = (LinearLayout)view.findViewById(R.id.ll_saved_order);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AwaitingOffersActivity.class);
                intent.putExtra("order_id", order_id);
                context.startActivity(intent);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OrderModel order = arrayList.get(i);
                if(!order.getImage_url().equals("null")){
                    Intent inf = new Intent(context, fullscreenActivity.class);
                    inf.putExtra("image_uri", order.getImage_url());
                    context.startActivity(inf);
                }
            }
        });

        return view;
    }
}
