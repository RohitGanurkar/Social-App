package com.example.socialapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.socialapp.Fragment.Notification2Fragment;
import com.example.socialapp.Fragment.RequestFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // here we are checking (which tab is now there)
        switch (position){
            case 0 :
                return new Notification2Fragment();
            case 1 :
                return new RequestFragment();
            default:
                return new Notification2Fragment();
        }
    }

    @Override // How many Tabs we Have
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override  // for Setting Title to the Tabs
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "NOTIFICATION";
        }
        else if (position == 1){
            title = "REQUEST";
        }
        return title;
    }
}
