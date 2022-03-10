package com.example.socialapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Model.PostModel;
import com.example.socialapp.R;

import java.util.ArrayList;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.viewHolder>{

    ArrayList<PostModel> list;
    Context context;

    public DashBoardAdapter(ArrayList<PostModel> list, Context context) {
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
//        holder.profileImg.setImageResource(model.getProfileImg());
//        holder.postImg.setImageResource(model.getPostImg());
//        holder.save.setImageResource(model.getSaveImg());
//        holder.name.setText(model.getName());
//        holder.about.setText(model.getAbout());
//        holder.like.setText(model.getLike());
//        holder.comment.setText(model.getComment());
//        holder.share.setText(model.getShare());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profileImg, postImg, save;
        TextView name, about, like, comment, share;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.profile_image);
            postImg = itemView.findViewById(R.id.postImage);
            save = itemView.findViewById(R.id.savedLogo);
            name = itemView.findViewById(R.id.userName);
            about = itemView.findViewById(R.id.about);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
        }
    }

}
