package com.bcoop.bcoop.ui.chat.chatnotification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.ui.chat.ChatWithAnotherUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Usuari").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.update("token", token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        if (sented.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
            sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, ChatWithAnotherUserActivity.class);
        intent.putExtra("otherUserEmail", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendindIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendindIntent);
        NotificationManager notific = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notific.notify(1, builder.build());
    }
}
