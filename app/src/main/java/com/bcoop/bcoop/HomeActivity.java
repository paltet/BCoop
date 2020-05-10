package com.bcoop.bcoop;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bcoop.bcoop.Model.CurrentUser;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.ui.chat.ChatFragment;
import com.bcoop.bcoop.ui.prize.PrizeFragment;
import com.bcoop.bcoop.ui.profile.ConfigProfileActivity;
import com.bcoop.bcoop.ui.profile.HabilitatAdaptar;
import com.bcoop.bcoop.ui.profile.ProfileFragment;
import com.bcoop.bcoop.ui.search.SearchFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Usuari").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                documentReference.update("token", newToken);
                Toast.makeText(HomeActivity.this, newToken, Toast.LENGTH_SHORT).show();
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            CurrentUser.getInstance().setCurrentUser(documentSnapshot.toObject(Usuari.class));
                        }
                    }
                });

            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_prize, R.id.navigation_chat, R.id.navigation_profile, R.id.navigation_notification)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    long SystemTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()- SystemTime < 1000 && SystemTime != 0){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }else{
                SystemTime = System.currentTimeMillis();
                Toast.makeText(HomeActivity.this, "clik again to left", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_settings:
                startActivity(new Intent(HomeActivity.this, ConfigProfileActivity.class));
                return true;
            case R.id.navigation_search:
                selectedFragment = new SearchFragment();
                break;
            case R.id.navigation_prize:
                selectedFragment = new PrizeFragment();
                break;
            case R.id.navigation_chat:
                selectedFragment = new ChatFragment();
                break;
            case R.id.navigation_profile:
                selectedFragment = new ProfileFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
        return false;


    }
}
