package com.bcoop.bcoop.ui.notification;

import com.bcoop.bcoop.Model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConectFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

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
        db.collection("Servei").document(notification.getServiceId()).update("confirmat", true);
    }


}
