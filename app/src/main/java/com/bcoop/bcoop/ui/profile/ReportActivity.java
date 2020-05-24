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
import com.google.firebase.firestore.CollectionReference;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
        TextView lastBan = findViewById(R.id.lastBanText);
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
                            date.add(changeTimeFormat(document.getString("data")));
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
                    Date lastBlogueig = user.getLastBloqueig();
                    if (lastBlogueig != null) {
                        lastBan.setText(R.string.lastBan);
                        lastBan.setText(lastBan.getText().toString().concat(" ").concat(changeTimeFormat(lastBlogueig.toString())));
                    }
                    else lastBan.setText(R.string.noBan);
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

    private String changeTimeFormat(String timeDate) {
        String date = timeDate.substring(8, 10);
        date = date.concat("/");
        String mes = timeDate.substring(4, 7);
        int num = convertMonth(mes);
        if (num < 10)
            date = date.concat("0");
        date = date.concat(Integer.toString(num));
        date = date.concat("/");
        date = date.concat(timeDate.substring(timeDate.length()-4));
        return date;
    }

    private int convertMonth(String mes) {
        int month = 1;
        switch (mes) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }
        return month;
    }

}
