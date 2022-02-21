package com.example.socialapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.Adapter.NotificationAdapter;
import com.example.socialapp.Model.NotificationModel;
import com.example.socialapp.R;
import com.example.socialapp.databinding.FragmentNotification2Binding;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {
    FragmentNotification2Binding binding;
    ArrayList<NotificationModel> list;

    public Notification2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotification2Binding.inflate(inflater, container, false);

        //Arraylist for Notification Adapter
        list = new ArrayList<>();
        list.add(new NotificationModel(R.drawable.image_face,"<b>Rohit</b> hello im very good we are at the very good place now", "06:08pm"));
        list.add(new NotificationModel(R.drawable.image_wall,"<b>Vishal</b> hello im very better", "06:08pm"));
        list.add(new NotificationModel(R.drawable.image_face,"<b>Manish</b> hello im very good", "06:08pm"));
        list.add(new NotificationModel(R.drawable.image_wall,"<b>Anup</b> hello im very better", "06:08pm"));
        list.add(new NotificationModel(R.drawable.back_ground,"<b>Shubham</b> hello im very good", "06:08pm"));

        // setting Adapter to the Notification Recycler view
        NotificationAdapter adapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.notifiyRv.setLayoutManager(linearLayoutManager);
        binding.notifiyRv.setAdapter(adapter);

        return binding.getRoot();
    }
}