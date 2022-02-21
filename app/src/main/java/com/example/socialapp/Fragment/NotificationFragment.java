package com.example.socialapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.Adapter.ViewPagerAdapter;
import com.example.socialapp.R;
import com.example.socialapp.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        // 1. firstly setting ViewpagerAdapter to viewPager
        binding.viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));

        // 2. then setting ViewPager to the Tab
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}