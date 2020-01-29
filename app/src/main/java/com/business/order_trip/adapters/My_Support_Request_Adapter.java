package com.business.order_trip.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.business.order_trip.MySupportDetailActivity;
import com.business.order_trip.R;
import com.business.order_trip.models.MessageModel;

import java.util.ArrayList;

public class My_Support_Request_Adapter extends BaseAdapter {
    ArrayList<MessageModel> arrayList;
    Context context;

    My_Support_Request_Adapter() {
        context = null;
        arrayList = null;
    }
    public My_Support_Request_Adapter(Context _context, ArrayList<MessageModel> _arrayList) {
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
        final MessageModel messageModel = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_support_request;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView support_user_id = (TextView)view.findViewById(R.id.support_user_id);
        TextView date = (TextView)view.findViewById(R.id.date);

        support_user_id.setText(messageModel.user_id);
        date.setText(messageModel.send_date);

        final LinearLayout btn_support = (LinearLayout)view.findViewById(R.id.ll_support);
        btn_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MySupportDetailActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
