package com.example.socialapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.CommentActivity;
import com.example.socialapp.Model.Notification;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.NotificationSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Notification notification = list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        // setup for NotificationViews
                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.back_ground).into(holder.binding.profileImage);
                        holder.binding.timeNotify.setText(TimeAgo.using(notification.getNotificationAt()));
                        switch (notification.getType()){
                            case "like":
                                holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+" liked your post"));
                                break;
                            case "comment":
                                holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+" commented on your post"));
                                break;
                            case "follow":
                                holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+" starts following you"));
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if(!notification.getType().equals("follow")){
            holder.binding.notification.setOnClickListener(v -> {

                // false means notification is not Opened by user and true means Opened
                FirebaseDatabase.getInstance().getReference()
                        .child("Notifications")
                        .child(notification.getPostedBy())
                        .child(notification.getNotificationId())
                        .child("checkOpen")
                        .setValue(true);

                holder.binding.notification.setBackgroundColor(Color.parseColor("#FFFFFF"));

                // to open CommentActivity when that notification was clicked
                Intent intent = new Intent(context, CommentActivity.class);
                // Sending some Data to CommentActivity
                intent.putExtra("postId", notification.getPostId());
                intent.putExtra("postBy",notification.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }

        // means Notification is Opened or notOpened
        if(notification.isCheckOpen()){
            holder.binding.notification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        NotificationSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationSampleBinding.bind(itemView);
        }
    }
}
