package com.softians.poller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by HP on 26-08-2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment>fragments = new ArrayList<>();
    ArrayList<String>strings = new ArrayList<>();
    public ViewPagerAdapter (FragmentManager fm)
    {
        super(fm);
    }
    public void addFragment(Fragment fragment,String strings)
    {
        this.fragments.add(fragment);
        this.strings.add(strings);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
    return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
