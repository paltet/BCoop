package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class InitConfigImageActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private ImageView img;
    private Uri imgUri;
    private StorageReference imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config_image);

        Button remindLater = findViewById(R.id.remindMeLaterButton);
        remindLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InitConfigImageActivity.this, InitConfigLocationActivity.class));
            }
        });

        img = findViewById(R.id.user_image);
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

        Button accept = findViewById(R.id.acceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save img
                imagePath = FirebaseStorage.getInstance().getReference().child("ProfileImage").child("email.jpg");
                imagePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Add uri to Profile
                        /*
                        profile.setImageAddress(imagePath.toString());
                         */
                        startActivity(new Intent(InitConfigImageActivity.this, InitConfigLocationActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InitConfigImageActivity.this, R.string.failed, Toast.LENGTH_SHORT);
                    }
                });

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                Toast.makeText(InitConfigImageActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery() {
        //Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            imgUri = data.getData();
            /*String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int n = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(n);
            cursor.close();
            img.setImageBitmap(BitmapFactory.decodeFile(imgPath));*/
            img.setImageURI(imgUri);
        }
    }
}
