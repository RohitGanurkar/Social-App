package com.example.socialapp.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Model.CommentModel;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.CommentSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{
    Context context;
    ArrayList<CommentModel> list;

    public CommentAdapter(Context context, ArrayList<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CommentModel comment = list.get(position);
        holder.binding.commTime.setText(TimeAgo.using(comment.getCommentedAt()));
        FirebaseDatabase.getInstance().getReference().child("User")
                .child(comment.getCommentedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.back_ground).into(holder.binding.profileImage);
                        holder.binding.comment.setText(Html.fromHtml("<b>"+ user.getName() +"</b>" + "  " + comment.getCommentBody()));
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
        CommentSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleBinding.bind(itemView);
        }
    }
}
