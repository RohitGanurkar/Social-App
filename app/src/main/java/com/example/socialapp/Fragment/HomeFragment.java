package com.example.socialapp.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.socialapp.Model.UserStories;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ArrayList<StoryModel> storyList;
    ArrayList<PostModel> postArrayList;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    ActivityResultLauncher<String> getContent;
    ProgressDialog dialog;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // DialogBox when Story Uploading
        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait..");

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
        storyList = new ArrayList<>();

        // Adapter for Story RecyclerView
        StoryAdapter storyAdapter = new StoryAdapter(storyList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.stroryRecycle.setLayoutManager(linearLayoutManager);
        binding.stroryRecycle.setNestedScrollingEnabled(false);
        binding.stroryRecycle.setAdapter(storyAdapter);
        binding.stroryRecycle.showShimmerAdapter(); // shimmer layout

        //getting All Stories from Database
        database.getReference()
                .child("Stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            storyList.clear();
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                StoryModel story = new StoryModel();
                                story.setStoryBy(dataSnapshot.getKey());
                                story.setStoryAt(dataSnapshot.child("PostedBy").getValue(Long.class));

                                ArrayList<UserStories> userStories = new ArrayList<>();
                                for(DataSnapshot storySnapshot: dataSnapshot.child("UserStories").getChildren()){
                                    UserStories allStories = storySnapshot.getValue(UserStories.class);
                                    userStories.add(allStories);
                                }

                                story.setStories(userStories);

                                // this Arraylist user for StoryAdapter
                                storyList.add(story);
                            }
                            binding.stroryRecycle.hideShimmerAdapter(); // shimmer layout
                            storyAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Arraylist for PostAdapter
        postArrayList = new ArrayList<>();

        // Adapter for DashBoard (Posts) RecyclerView
        PostsAdapter postsAdapter = new PostsAdapter(postArrayList,getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        binding.dashboardRv.setLayoutManager(linearLayoutManager2);
        binding.dashboardRv.setAdapter(postsAdapter);
        binding.dashboardRv.showShimmerAdapter(); // shimmer layout

        // Getting All Posts from FirebaseDatabase for PostArrayList
        database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    postArrayList.add(postModel);
                }
                binding.dashboardRv.hideShimmerAdapter(); // shimmer layout
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // AddStoryLogo clicked
        binding.plusStories.setOnClickListener(v -> {
            getContent.launch("image/*");
        });
        // then Gallery open and get selected image by user
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.postImage.setImageURI(result);
                dialog.show();

                // save that Image into the FirebaseStorage
                StorageReference reference = storage.getReference()
                        .child("Stories")
                        .child(auth.getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(result).addOnSuccessListener(taskSnapshot -> {

                    reference.getDownloadUrl().addOnSuccessListener(imageUri -> {
                        StoryModel story = new StoryModel();
                        story.setStoryAt(new Date().getTime());

                        // save Story into the FirebaseDatabase
                        database.getReference()
                                .child("Stories")
                                .child(auth.getUid())
                                .child("PostedBy")
                                .setValue(story.getStoryAt())
                                .addOnSuccessListener(unused -> {
                                    UserStories userStories = new UserStories(imageUri.toString(), story.getStoryAt() );
                                    database.getReference()
                                            .child("Stories")
                                            .child(auth.getUid())
                                            .child("UserStories")
                                            .push()
                                            .setValue(userStories).addOnSuccessListener(unused1 -> {
                                                binding.postImage.setImageResource(R.drawable.image_wall);
                                                dialog.dismiss();
                                            });
                                });
                    });
                });
            }
        });

        return binding.getRoot();
    }
}