package com.example.socialapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.CommentActivity;
import com.example.socialapp.Model.Notification;
import com.example.socialapp.Model.PostModel;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.DashboardRvSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.viewHolder>{

    ArrayList<PostModel> list;
    Context context;

    public PostsAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel model = list.get(position);

        // Setting values in ImageBox/TextBox
        Picasso.get().load(model.getPostImgUrl()).placeholder(R.drawable.back_ground).into(holder.binding.postImage);
        FirebaseDatabase.getInstance().getReference().child("User").child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                // setting name and profession and other things
                Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.back_ground).into(holder.binding.profileImage);
                holder.binding.userName.setText(user.getName());
                holder.binding.about.setText(user.getProfession());
                holder.binding.like.setText(model.getPostLike()+"");
                holder.binding.comment.setText(model.getCommentCount()+"");

                if(model.getPostDescription().length() != 0){ // if postDescription here
                    holder.binding.postDescription.setVisibility(View.VISIBLE);
                    holder.binding.postDescription.setText(model.getPostDescription());
                } else { // if postDescription not  here
                    holder.binding.postDescription.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // if user Liked any Post so there RedHeartLike showed
        FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if user Liked any Post Already so there RedHeartLike showed
                if(snapshot.exists()){
                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heartlike,0,0,0);
                }
                // or user can click on LikeBtn then user can Like on Post
                else {
                    holder.binding.like.setOnClickListener(v -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Posts")
                                .child(model.getPostId())
                                .child("likes")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .setValue(true).addOnSuccessListener(unused -> {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Posts")
                                    .child(model.getPostId())
                                    .child("postLike")
                                    .setValue(model.getPostLike()+1)
                                    .addOnSuccessListener(unused1 -> {
                                        // normal heart change into RedHeart
                                        holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heartlike,0,0,0);

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostId(model.getPostId());
                                        notification.setPostedBy(model.getPostedBy());
                                        notification.setType("like");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Notifications")
                                                .child(model.getPostedBy())
                                                .push()
                                                .setValue(notification);
                                    });
                        });
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // when Comment btn clicked CommentActivity opened
        holder.binding.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            // Sending some Data to CommentActivity
            intent.putExtra("postId", model.getPostId());
            intent.putExtra("postBy", model.getPostedBy());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        DashboardRvSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DashboardRvSampleBinding.bind(itemView);
        }
    }

}
