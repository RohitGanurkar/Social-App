package com.example.socialapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Model.StoryModel;
import com.example.socialapp.Model.UserStories;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.StoryRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{

    ArrayList<StoryModel> list;

    public StoryAdapter(ArrayList<StoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        StoryModel story = list.get(position);

        // if user have Stories
        if(story.getStories().size() > 0) {
            UserStories lastStory = story.getStories().get(story.getStories().size() - 1);
            Picasso.get().load(lastStory.getImageUrl()).placeholder(R.drawable.back_ground).into(holder.binding.lastStory);
            holder.binding.circularStatusView.setPortionsCount(story.getStories().size());

            // getting userInfo for setup profileImage and userName
            FirebaseDatabase.getInstance().getReference()
                    .child("User")
                    .child(story.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.back_ground).into(holder.binding.profilePhoto);
                            holder.binding.username.setText(user.getName());

                            // when Story Clicked this is open for view stories Images (like instagram)
                            holder.binding.lastStory.setOnClickListener(v -> {
                                ArrayList<MyStory> myStories = new ArrayList<>();

                                for (UserStories stories : story.getStories()) {
                                    myStories.add(new MyStory(
                                            stories.getImageUrl()
                                    ));
                                }
                                // this is a github library code
                                new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                        .setStoriesList(myStories) // Required
                                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                        .setTitleText(user.getName()) // Default is Hidden
                                        .setSubtitleText("") // Default is Hidden
                                        .setTitleLogoUrl(user.getProfilePhoto()) // Default is Hidden
                                        .setStoryClickListeners(new StoryClickListeners() {
                                            @Override
                                            public void onDescriptionClickListener(int position) {
                                                //your action
                                            }

                                            @Override
                                            public void onTitleIconClickListener(int position) {
                                                //your action
                                            }
                                        }) // Optional Listeners
                                        .build() // Must be called before calling show method
                                        .show();

                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        StoryRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StoryRvDesignBinding.bind(itemView);
        }
    }
}
