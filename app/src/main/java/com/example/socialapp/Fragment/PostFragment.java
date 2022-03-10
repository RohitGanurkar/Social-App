package com.example.socialapp.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.User;
import com.example.socialapp.databinding.FragmentPostBinding;
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

public class PostFragment extends Fragment {
    FragmentPostBinding binding;
    ActivityResultLauncher<String> changePostImage;
    Uri uri;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instance of Firebase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);

        // getting UserInfo for setup User ProfileImage and Name in Post Frgament
        database.getReference().child("User").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfilePhoto())
                            .placeholder(R.drawable.back_ground)
                            .into(binding.profileImage);
                    binding.nameName.setText(user.getName());
                    binding.professionUser.setText(user.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // when user typing in PostDescription
        binding.postDescripyion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = binding.postDescripyion.getText().toString();
                if(!description.isEmpty() || uri!=null){
                    // for set up post button
                    binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_design));
                    binding.postButton.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postButton.setEnabled(true);
                }
                else{
                    // for set up post button
                    binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_activebutton));
                    binding.postButton.setTextColor(getContext().getResources().getColor(R.color.grey));
                    binding.postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // when AddImage clicked gallery opened
        binding.addImage.setOnClickListener(v -> {
            changePostImage.launch("image/*");
        });

        // get that image which can be selected by user in AddImage
        changePostImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null){
                    uri = result;
                    binding.postImagePost.setImageURI(uri);
                    binding.postImagePost.setVisibility(View.VISIBLE);
                    binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_design));
                    binding.postButton.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postButton.setEnabled(true);
                }
            }
        });
        return binding.getRoot();
    }
}