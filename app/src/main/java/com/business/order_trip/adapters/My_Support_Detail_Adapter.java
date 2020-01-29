package com.business.order_trip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.business.order_trip.R;
import com.business.order_trip.models.MessageModel;

import java.util.ArrayList;

public class My_Support_Detail_Adapter extends BaseAdapter {
    ArrayList<MessageModel> arrayList;
    Context context;

    My_Support_Detail_Adapter() {
        context = null;
        arrayList = null;
    }
    public My_Support_Detail_Adapter(Context _context, ArrayList<MessageModel> _arrayList) {
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
            int resource = R.layout.cell_support_detail;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        TextView send_person = (TextView)view.findViewById(R.id.send_person);
        TextView date = (TextView)view.findViewById(R.id.date);
        TextView content = (TextView)view.findViewById(R.id.content);

        send_person.setText(messageModel.send_person);
        date.setText(messageModel.send_date);
        content.setText(messageModel.content);

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
