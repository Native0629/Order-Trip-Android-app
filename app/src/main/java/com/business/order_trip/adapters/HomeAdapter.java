package com.business.order_trip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.business.order_trip.fragments.HomeOrderFragment;
import com.business.order_trip.fragments.HomeTripFragment;
import com.business.order_trip.fragments.TripAddFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HomeAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeOrderFragment tab1 = new HomeOrderFragment();
                return tab1;
            case 1:
                HomeTripFragment tab2 = new HomeTripFragment();
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