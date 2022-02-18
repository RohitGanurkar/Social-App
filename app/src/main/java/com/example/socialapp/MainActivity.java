package com.example.socialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.socialapp.Fragment.HomeFragment;
import com.example.socialapp.Fragment.NotificationFragment;
import com.example.socialapp.Fragment.PostFragment;
import com.example.socialapp.Fragment.ProfileFragment;
import com.example.socialapp.Fragment.SearchFragment;
import com.example.socialapp.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Showing Home Fragment when App in Opening
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();

        // When BottomNavigation clicked Then Fragment Will be Replaced
        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {

                // for use Fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch(i){
                    case 0:
                        transaction.replace(R.id.container, new HomeFragment()); // for replace View with Fragment
                        break;
                    case 1:
                        transaction.replace(R.id.container, new NotificationFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.container, new PostFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.container, new SearchFragment());
                        break;
                    case 4:
                        transaction.replace(R.id.container, new ProfileFragment());
                        break;
                }

                transaction.commit(); // Start Working
            }
        });
    }
}