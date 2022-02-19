package com.example.socialapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.Adapter.FriendAdapter;
import com.example.socialapp.Model.FriendModel;
import com.example.socialapp.R;
import com.example.socialapp.databinding.FragmentPostBinding;
import com.example.socialapp.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    ArrayList<FriendModel> friendList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Arraylist for RecyclerView
        friendList = new ArrayList<>();
        friendList.add(new FriendModel(R.drawable.image_face));
        friendList.add(new FriendModel(R.drawable.image_wall));
        friendList.add(new FriendModel(R.drawable.back_ground));


        // Setting Adapter to RecyclerView
        FriendAdapter friendAdapter = new FriendAdapter(friendList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.friends.setLayoutManager(linearLayoutManager);

        binding.friends.setAdapter(friendAdapter);

        return binding.getRoot();
    }
}