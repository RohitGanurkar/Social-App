package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.socialapp.Adapter.CommentAdapter;
import com.example.socialapp.Model.CommentModel;
import com.example.socialapp.Model.Notification;
import com.example.socialapp.Model.PostModel;
import com.example.socialapp.databinding.ActivityCommentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    Intent intent;
    String postId, postBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<CommentModel> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        commentsList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // setting TopNavigationBar
        setSupportActionBar(binding.toolbarComment);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        intent = getIntent();
        // getting (postId-&-postBy) from PostAdapter
        postId = intent.getStringExtra("postId");
        postBy = intent.getStringExtra("postBy");

        // getting Posts Data from Database for Setup everyViews
        database.getReference()
                .child("Posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostModel model = snapshot.getValue(PostModel.class);
                Picasso.get()
                        .load(model.getPostImgUrl())
                        .placeholder(R.drawable.back_ground)
                        .into(binding.postedPhoto);
                binding.description.setText(model.getPostDescription());
                binding.like.setText(model.getPostLike()+"");
                binding.comment.setText(model.getCommentCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // getting User from Database for Setup everyViews
        database.getReference()
                .child("User")
                .child(postBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfilePhoto())
                        .placeholder(R.drawable.back_ground)
                        .into(binding.profileImage);
                binding.name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Comments and commentCount store into the Database when used clicked sendBtn
        binding.sendBtn.setOnClickListener(v -> {
            CommentModel comment = new CommentModel();
            comment.setCommentBody(binding.commentBox.getText().toString());
            comment.setCommentedAt(new Date().getTime());
            comment.setCommentedBy(auth.getUid());

            // store Comment into Database
            database.getReference()
                    .child("Posts")
                    .child(postId)
                    .child("comments")
                    .push()
                    .setValue(comment)
                    .addOnSuccessListener(unused -> {
                        database.getReference()
                                .child("Posts")
                                .child(postId)
                                .child("commentCount")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // if commentCount filed is not there in Database means the Post have No Like ( 0 Like)
                                        int commentCount = 0 ;
                                        if(snapshot.exists()){
                                            // if commentCount filed there in Database means the Post have some Like
                                            commentCount = snapshot.getValue(Integer.class);
                                        }
                                        // Store commentCount into Database
                                        database.getReference()
                                                .child("Posts")
                                                .child(postId)
                                                .child("commentCount")
                                                .setValue(commentCount + 1)
                                                .addOnSuccessListener(unused1 -> {
                                                    binding.commentBox.setText("");
                                                    Toast.makeText(CommentActivity.this, "Commented Successfully", Toast.LENGTH_SHORT).show();

                                                    Notification notification = new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notification.setNotificationAt(new Date().getTime());
                                                    notification.setPostId(postId);
                                                    notification.setPostedBy(postBy);
                                                    notification.setType("comment");

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Notifications")
                                                            .child(postBy)
                                                            .push()
                                                            .setValue(notification);

                                                    // Sending Notification to That User
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("User")
                                                            .child(postBy)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    User user = snapshot.getValue(User.class);
                                                                    NotificationFCM notificationFCM = new NotificationFCM();
                                                                    notificationFCM.sendNotify(CommentActivity.this, user.getName(), "commented on your post", user.getToken());
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    });
        });

        // setting Adapter to the RecyclerView
        CommentAdapter adapter = new CommentAdapter(this, commentsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(linearLayoutManager);
        binding.commentRv.setAdapter(adapter);

        // getting All Comments from database
        database.getReference().child("Posts")
                .child(postId)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentsList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            CommentModel commentModel = dataSnapshot.getValue(CommentModel.class);
                            commentsList.add(commentModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}