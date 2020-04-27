package com.bcoop.bcoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.bcoop.bcoop.ui.profile.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String mail = (String) getIntent().getSerializableExtra("email");
        Bundle bundle = new Bundle();
        bundle.putString("email", mail);
        ProfileFragment frag = new ProfileFragment();
        frag.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(android.R.id.content, frag).addToBackStack(null).commit();

    }
}
