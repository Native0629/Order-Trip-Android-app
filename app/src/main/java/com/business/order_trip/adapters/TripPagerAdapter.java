package com.business.order_trip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.business.order_trip.fragments.TripCancelFragment;
import com.business.order_trip.fragments.TripAddFragment;

public class TripPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TripPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TripAddFragment tab1 = new TripAddFragment();
                return tab1;
            case 1:
                TripCancelFragment tab2 = new TripCancelFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}