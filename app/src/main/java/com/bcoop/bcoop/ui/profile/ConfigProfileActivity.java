package com.bcoop.bcoop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.security.Provider;

public class ConfigProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView img;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_perfil);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        final TextView usrn = findViewById(R.id.usernameText);
        Button usrnChange = findViewById(R.id.usernameButton);
        final TextView pwd = findViewById(R.id.passwordText);
        Button pwdChange = findViewById(R.id.passwordButton);
        img = findViewById(R.id.userImage);
        Button imgChange = findViewById(R.id.imageButton);
        mapView = findViewById(R.id.mapView);
        Button locChange = findViewById(R.id.locationButton);
        Button delete = findViewById(R.id.deleteUserButton);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        usrnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigProfileActivity.this, ConfigChangeUsernameActivity.class));
            }
        });

        pwdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigProfileActivity.this, ConfigChangePasswordActivity.class));
            }
        });

        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigProfileActivity.this, ConfigChangeImageActivity.class));
            }
        });

        locChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigProfileActivity.this, ConfigChangeLocationActivity.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigProfileActivity.this, DeletePerfilActivity.class));
            }
        });

        final DocumentReference documentReference = firestore.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari usuari = new Usuari();
                    usuari = documentSnapshot.toObject(Usuari.class);

                    usrn.setText(usuari.getNom());
                    pwd.setText("********");
                    getImageFromStorage(usuari.getFoto());
                    showLocation(usuari.getLocationLatitude(), usuari.getLocationLongitude());
                }
            }
        });
    }

    private void showLocation(Double latitude, Double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.addMarker(markerOptions);
        }
    }

    private void getImageFromStorage(String uriImage) {
        if (uriImage != null) {
            StorageReference storageReference = storage.getReferenceFromUrl(uriImage);
            try {
                final File file = File.createTempFile("image", uriImage.substring(uriImage.lastIndexOf('.')));
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        img.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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

}
