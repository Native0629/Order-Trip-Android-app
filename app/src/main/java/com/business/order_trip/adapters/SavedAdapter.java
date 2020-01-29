package com.business.order_trip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.business.order_trip.fragments.SavedOrderFragment;
import com.business.order_trip.fragments.SavedTripFragment;

public class SavedAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SavedAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SavedOrderFragment tab1 = new SavedOrderFragment();
                return tab1;
            case 1:
                SavedTripFragment tab2 = new SavedTripFragment();
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