package com.example.android.attendance.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.attendance.Fragments.AttendanceFragment;
import com.example.android.attendance.Fragments.CalenderFragment;
import com.example.android.attendance.Fragments.HomeFragment;
import com.example.android.attendance.Fragments.ProfileFragment;

public class SmFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Home", "Attendance", "Calender", "Profile" };
    private Context context;

    public SmFragmentPagerAdapter(FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1){
            return new AttendanceFragment();
        } else if (position == 2){
            return new CalenderFragment();
        } else {
            return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
