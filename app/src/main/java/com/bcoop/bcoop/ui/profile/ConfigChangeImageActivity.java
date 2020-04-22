package com.bcoop.bcoop.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcoop.bcoop.InitConfigImageActivity;
import com.bcoop.bcoop.InitConfigLocationActivity;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ConfigChangeImageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView img;
    private Uri imgUri;
    private StorageReference imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_userimage);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        img = findViewById(R.id.userImageSelector);
        img.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightGrey)));
        Button confirm = findViewById(R.id.confirmButton);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
                    else openGallery();
                }
                else openGallery();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri == null)
                    Toast.makeText(ConfigChangeImageActivity.this, R.string.select_image, Toast.LENGTH_SHORT).show();
                else
                    saveImage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            openGallery();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            imgUri = data.getData();
            img.setImageTintList(null);
            img.setImageURI(imgUri);
        }
    }

    private void saveImage() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        email = email.substring(0, email.lastIndexOf('@'));
        String extension = imgUri.getLastPathSegment();
        extension = extension.substring(extension.lastIndexOf('.'));

        imagePath = FirebaseStorage.getInstance().getReference().child("ProfileImage").child(email.concat(extension));
        imagePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final DocumentReference documentReference = firestore.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            documentReference.update("foto", imagePath.toString());
                            startActivity(new Intent(ConfigChangeImageActivity.this, ConfigProfileActivity.class));
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfigChangeImageActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
