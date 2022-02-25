package com.example.socialapp.Fragment;

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
import android.widget.Toast;

import com.example.socialapp.Adapter.FollowersAdapter;
import com.example.socialapp.Model.FollowModel;
import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    ArrayList<FollowModel> followerList;
    ActivityResultLauncher<String> changeCover, changeProfile;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Instance of Firebase
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // getting UserInfo for setup User Profile
        database.getReference().child("User").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getCoverPhoto())
                            .placeholder(R.drawable.back_ground)
                            .into(binding.coverPhoto);
                    Picasso.get()
                            .load(user.getProfilePhoto())
                            .placeholder(R.drawable.back_ground)
                            .into(binding.profileImage);
                    binding.name.setText(user.getName());
                    binding.profession.setText(user.getProfession());
                    binding.follower.setText(user.getFollowerCount()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Arraylist for MyFollowerRecyclerView
        followerList = new ArrayList<>();

        // Setting Adapter to RecyclerView
        FollowersAdapter followersAdapter = new FollowersAdapter(followerList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.friends.setLayoutManager(linearLayoutManager);
        binding.friends.setAdapter(followersAdapter);

        // get Follower from Database for followerList
        database.getReference().child("User")
                .child(auth.getUid())
                .child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followerList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                    followerList.add(followModel);
                }
                followersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // when we click for ChangeCoverPhoto
        binding.addCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCover.launch("image/*");
            }
        });

        // when we click for ChangeProfilePhoto
        binding.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile.launch("image/*");
            }
        });
        /* StartActivityForResult :- {{ ALTERNATIVE }}
         when user select the image by clicking */
        changeCover = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.coverPhoto.setImageURI(result);
                // Store image in Firebase Storage
                StorageReference reference = storage.getReference().child("cover_photo").child(auth.getUid());
                reference.putFile(result).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(getContext(), "Successfully Change", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // copy imageUrl into userDetails
                                database.getReference().child("User").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        });
        // when user select the image by clicking CheckedBtn for ChangeProfilePhoto
        changeProfile = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.profileImage.setImageURI(result);
                // Store image in Firebase Storage
                StorageReference reference = storage.getReference().child("profile_photo").child(auth.getUid());
                reference.putFile(result).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(getContext(), "Successfully Change", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // copy imageUrl into userDetails
                                database.getReference().child("User").child(auth.getUid()).child("profilePhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        });

        return binding.getRoot();
    }
}