package com.business.order_trip.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.business.order_trip.MainActivity;
import com.business.order_trip.OrderPublishActivity;
import com.business.order_trip.R;
import com.business.order_trip.WebVIewActivity;
import com.business.order_trip.models.OrderModel;

import java.util.ArrayList;

public class HomeOrderFragment extends Fragment {

    MainActivity mainActivity;
    ArrayList<OrderModel> arrayList = new ArrayList<>();
    View view;
    LinearLayout mBody;
    public HomeOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_order, container, false);
        mBody = view.findViewById(R.id.body);
//        mBody.setVisibility(View.GONE);
        //Loading progress bar
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(getContext())
//                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
//                .themeColor(Color.WHITE)
//                .text("Loding ...")
//                .fadeColor(Color.DKGRAY).build();
//        dialog.show();
//
//        long delayInMillis = 5000;
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//                new Handler(Looper.getMainLooper()).post(new Runnable(){
//                    @Override
//                    public void run() {
//                        mBody.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        }, delayInMillis);

        initUI();
        return view;
    }

    void initUI() {
        TextView tvOrderProduct= (TextView) view.findViewById(R.id.tv_order_product);
        tvOrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebVIewActivity.class);
                startActivity(intent);
            }
        });

        TextView button= (TextView) view.findViewById(R.id.tv_order_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderPublishActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

}
