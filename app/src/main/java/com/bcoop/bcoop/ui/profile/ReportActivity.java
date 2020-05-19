package com.bcoop.bcoop.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);

        ListView reports = findViewById(R.id.messagesList);
        TextView numReps = findViewById(R.id.numberReports);
        TextView username = findViewById(R.id.usernameText);
        userImage = findViewById(R.id.userImage);
        userImage.setImageResource(R.drawable.profile);

        List<String> informe = new ArrayList<>();
        List<String> date = new ArrayList<>();

        String user = getIntent().getStringExtra("otherUser");
        //Get info from DB
        Query userReports = FirebaseFirestore.getInstance().collection("Reports").whereEqualTo("user", user);
        userReports.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            informe.add(document.getString("Informe"));
                            date.add(document.getString("data"));
                        }
                    }
                    numReps.setText(String.valueOf(informe.size()));
                    ReportAdaptar reportAdaptar = new ReportAdaptar(ReportActivity.this, informe, date);
                    reports.setAdapter(reportAdaptar);
                }
                else Toast.makeText(ReportActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });

        final DocumentReference documentReferenceUsuari = FirebaseFirestore.getInstance().collection("Usuari").document(user);
        documentReferenceUsuari.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari user = documentSnapshot.toObject(Usuari.class);
                    getImageFromStorage(user.getFoto());
                    username.setText(user.getNom());
                }
            }
        });
    }

    private void getImageFromStorage(String uriImage) {
        if (uriImage != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(uriImage);
            try {
                final File file = File.createTempFile("image", uriImage.substring(uriImage.lastIndexOf('.')));
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        userImage.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
