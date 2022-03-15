package com.example.socialapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Model.FollowModel;
import com.example.socialapp.Model.Notification;
import com.example.socialapp.NotificationFCM;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.UserSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.viewHolder> {
    ArrayList<User> list;
    Context context;

    public SearchUserAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user = list.get(position);
        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.back_ground).into(holder.binding.profileImage);
        holder.binding.nameName.setText(user.getName());
        holder.binding.professionUser.setText(user.getProfession());

        // IF :- AllReady followed then FollowButton was changed
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(user.getUserId()).child("follower")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.binding.followButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_activebutton));
                    holder.binding.followButton.setText("Following");
                    holder.binding.followButton.setTextColor(context.getResources().getColor(R.color.green));
                    holder.binding.followButton.setEnabled(false);
                }
                else{
                    // OTHERWISE :- When FollowButton was clicked  [current user follow that User]
                    holder.binding.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FollowModel followModel = new FollowModel();
                            followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            followModel.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference().child("User") // Set Follower into the userInfo in DATABASE
                                    .child(user.getUserId()).child("follower")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(followModel).addOnSuccessListener(unused -> {
                                FirebaseDatabase.getInstance().getReference().child("User")  // Set FollowerCount into the userInfo in DATABASE
                                        .child(user.getUserId()).child("followerCount")
                                        .setValue(user.getFollowerCount()+1)
                                        .addOnSuccessListener(unused1 -> {
                                            // for modify Button If allReady followed
                                            holder.binding.followButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_activebutton));
                                            holder.binding.followButton.setText("Following");
                                            holder.binding.followButton.setTextColor(context.getResources().getColor(R.color.green));
                                            holder.binding.followButton.setEnabled(false);
                                            Toast.makeText(context, "You Followed "+ user.getName(), Toast.LENGTH_SHORT).show();

                                            Notification notification = new Notification();
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Notifications")
                                                    .child(user.getUserId())
                                                    .push()
                                                    .setValue(notification);

                                            // save following in Database
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("User")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("following")
                                                    .child(user.getUserId())
                                                    .setValue(1);

                                            NotificationFCM notificationFCM = new NotificationFCM();
                                            notificationFCM.sendNotify(context, user.getName(), " starts following you", user.getToken());
                                });
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        UserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserSampleBinding.bind(itemView);
        }
    }
}
