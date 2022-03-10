package com.example.socialapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialapp.Adapter.PostsAdapter;
import com.example.socialapp.Adapter.StoryAdapter;
import com.example.socialapp.Model.PostModel;
import com.example.socialapp.Model.StoryModel;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<StoryModel> list;
    ArrayList<PostModel> postArrayList;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        // set profileImage on top
        database.getReference().child("User").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currUser = snapshot.getValue(User.class);
                Picasso.get().load(currUser.getProfilePhoto()).placeholder(R.drawable.back_ground).into(binding.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        postArrayList = new ArrayList<>();

        // Adapter for DashBoard (Post) RecyclerView
        PostsAdapter postsAdapter = new PostsAdapter(postArrayList,getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        binding.dashboardRv.setLayoutManager(linearLayoutManager2);
        binding.dashboardRv.setAdapter(postsAdapter);

        // Getting All Posts from FirebaseDatabase for PostArrayList
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postArrayList.add(postModel);
                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}