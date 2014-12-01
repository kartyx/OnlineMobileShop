package com.onlinemobileshop;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.onlinemobileshop.MobileActivity;
import com.onlinemobileshop.AccessoriesActivity;
import com.onlinemobileshop.ServicesActivity;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Mobile fragment activity
            return new MobileActivity();
        case 1:
            // Services fragment activity
            return new ServicesActivity();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }


 
}