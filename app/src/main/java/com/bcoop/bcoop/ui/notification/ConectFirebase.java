package com.bcoop.bcoop.ui.notification;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.prize.ScanActivity;
import com.bcoop.bcoop.ui.profile.HabilitatAdaptar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static android.content.ContentValues.TAG;

public class ConectFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    String name;

    public void pushNotification(Notification notification, String toEmail) {
        db.collection("Usuari").document(toEmail)
                .collection("notificacions").document(notification.getTime().toString())
                .set(notification);
    }

    public void deleteNotification(Notification notification) {
        db.collection("Usuari").document(email)
                .collection("notificacions").document(notification.getTime().toString())
                .delete();
    }

    public void deleteService(Notification notification) {
        db.collection("Servei").document(notification.getServiceId()).delete();
    }

    public void confimatService(Notification notification) {
        db.collection("Servei").document(notification.getServiceId()).update("estat", "en curs");
        //db.collection("Servei").document(notification.getServiceId()).update("confirmat", true);
    }
}
