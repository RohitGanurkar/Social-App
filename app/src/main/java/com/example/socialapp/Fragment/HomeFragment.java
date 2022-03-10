package com.example.socialapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.Adapter.DashBoardAdapter;
import com.example.socialapp.Adapter.StoryAdapter;
import com.example.socialapp.Model.PostModel;
import com.example.socialapp.Model.StoryModel;
import com.example.socialapp.R;
import com.example.socialapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<StoryModel> list;
    ArrayList<PostModel> dashboardList;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Arraylist for statusAdapter
        list = new ArrayList<>();
        list.add(new StoryModel(R.drawable.image_face,R.drawable.ic_baseline_live_tv_24,R.drawable.image_face, "Rahul"));
        list.add(new StoryModel(R.drawable.image_wall,R.drawable.ic_baseline_live_tv_24,R.drawable.image_face, "vishal"));
        list.add(new StoryModel(R.drawable.image_face,R.drawable.ic_baseline_live_tv_24,R.drawable.image_face, "anup"));
        list.add(new StoryModel(R.drawable.image_face,R.drawable.ic_baseline_live_tv_24,R.drawable.image_face, "shubham"));

        // Adapter for Story RecyclerView
        StoryAdapter storyAdapter = new StoryAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.stroryRecycle.setLayoutManager(linearLayoutManager);

        binding.stroryRecycle.setNestedScrollingEnabled(false);
        binding.stroryRecycle.setAdapter(storyAdapter);

        // Arraylist for DashboardAdapter
        dashboardList = new ArrayList<>();
//        dashboardList.add(new PostModel(R.drawable.image_face,R.drawable.image_wall,R.drawable.save,"Rohit","im good", "254", "65","21"));
//        dashboardList.add(new PostModel(R.drawable.image_face,R.drawable.image_wall,R.drawable.save,"vishal","im nice", "56", "24","25"));
//        dashboardList.add(new PostModel(R.drawable.image_face,R.drawable.image_wall,R.drawable.save,"anup","im better", "34", "65","98"));
//        dashboardList.add(new PostModel(R.drawable.image_face,R.drawable.image_wall,R.drawable.save,"rakesh","im no1", "254", "65","21"));

        // Adapter for DashBoard (Post) RecyclerView
        DashBoardAdapter dashBoardAdapter = new DashBoardAdapter(dashboardList,getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        binding.dashboardRv.setLayoutManager(linearLayoutManager2);

        binding.dashboardRv.setAdapter(dashBoardAdapter);

        return binding.getRoot();
    }
}