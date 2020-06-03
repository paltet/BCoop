package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.Habilitat;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Usuari;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InitConfigLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_FINE = 2;
    private MapView mapView;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String usrname;
    private String url_foto;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config_location);

        mAuth = FirebaseAuth.getInstance();
        usrname = getIntent().getStringExtra("username");
        url_foto = getIntent().getStringExtra("url_img");
        email = mAuth.getCurrentUser().getEmail();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapView = findViewById(R.id.setupMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button remindLater = findViewById(R.id.remindMeLaterButton);
        remindLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        Button accept = findViewById(R.id.acceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
                    @Override
                    public void onSuccess(LocationAvailability locationAvailability) {
                        if (locationAvailability.isLocationAvailable()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
                                    return;
                                }
                            }
                            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    currentLocation = location;
                                    saveUser();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(InitConfigLocationActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else Toast.makeText(InitConfigLocationActivity.this, R.string.location_no_available, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveUser() {
        Usuari usuari;
        if (url_foto.equals(""))
            url_foto = null;
        if (currentLocation != null)
            usuari = new Usuari(email, usrname, url_foto, currentLocation.getLatitude(), currentLocation.getLongitude());
        else usuari = new Usuari(email, usrname, url_foto, 0.0, 0.0);

        DocumentReference documentReference = firestore.collection("Usuari").document(email);
        documentReference.set(usuari);
        startActivity(new Intent(InitConfigLocationActivity.this, HomeActivity.class));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE);
                return;
            }
        }
        googleMap.setMyLocationEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_FINE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(InitConfigLocationActivity.this, InitConfigLocationActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("url_img", getIntent().getStringExtra("url_img"));
                startActivity(intent);
            }
            else saveUser();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
