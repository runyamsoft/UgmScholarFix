package com.octopus.ugmscholar2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import layout.BookmarkedFragment;
import layout.FragmentContent;

/**
 * Created by Jaka on 22/05/2016.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter  {
    String[] title = new String[]{
            "Beasiswa", "Favorites"
    };

    Fragment fragment = null;
    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    //method ini yang akan memanipulasi penampilan Fragment dilayar
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                fragment = new FragmentContent();
                break;
            case 1:
                fragment = new BookmarkedFragment();
                break;
            default:
                fragment = null;
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }


}
