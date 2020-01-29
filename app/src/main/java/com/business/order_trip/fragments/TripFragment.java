package com.business.order_trip.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.business.order_trip.R;
import com.business.order_trip.adapters.TripPagerAdapter;

public class TripFragment extends Fragment {

    View view;
    public static ViewPager viewPager;
    FragmentTransaction transaction;

    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trip, container, false);
        Bundle bundle = this.getArguments();

        initUI();

        if (bundle != null) {
            String strtext=getArguments().getString("message");
            Toast.makeText(getActivity(),strtext, Toast.LENGTH_SHORT).show();

            viewPager.setCurrentItem(1);
        }

        return view;
    }

    void initUI() {
        // Top tab
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout1);
        tabLayout.addTab(tabLayout.newTab().setText("Add Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Canceled"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = view.findViewById(R.id.trip_pager);
        final TripPagerAdapter adapter = new TripPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
