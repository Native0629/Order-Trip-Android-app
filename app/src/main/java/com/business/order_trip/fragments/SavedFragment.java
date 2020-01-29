package com.business.order_trip.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.business.order_trip.R;
import com.business.order_trip.adapters.SavedAdapter;

public class SavedFragment extends Fragment {

    View view;
    public static ViewPager viewPager;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved, container, false);

        initUI();

        return view;
    }

    void initUI() {
        // Top tab
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout2);
        tabLayout.addTab(tabLayout.newTab().setText("Saved Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved Trip"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = view.findViewById(R.id.save_pager);
        final SavedAdapter adapter = new SavedAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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
