package com.bcoop.bcoop.ui.notification;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;


public class NotificationAdapter extends ArrayAdapter<Notification> {

    Context context;
    int resource;
    List<Notification> notificationList;
    Notification notification;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ConectFirebase conectFirebase = new ConectFirebase();

    public NotificationAdapter(Context context, int resource, List<Notification> notificationList) {
        super(context, resource, notificationList);

        this.context = context;
        this.resource = resource;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        notification = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        TextView titol = view.findViewById(R.id.Titol);
        TextView time = view.findViewById(R.id.time);
        TextView descripcio = view.findViewById(R.id.Descripcio);

        titol.setText(notification.getTitle());
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(notification.getTime().toDate());
        time.setText(date);
        if (notification.getType() == 4 && notification.getPrice() < 0) {
            notification.setContent(context.getResources().getString(R.string.you_spent)
                    +" "+ (-notification.getPrice()) +" "+ context.getResources().getString(R.string.to_acquire_gift));
        } else if (notification.getType() == 2) {
            Log.d(TAG, notification.getUserName());
            if (notification.isResponse()) {
                notification.setContent(notification.getUserName()
                                +" "+ context.getResources().getString(R.string.request_agreed));
            }
            else {
                notification.setContent(notification.getUserName()
                                +" "+ context.getResources().getString(R.string.request_rejected));
            }
        } else if (notification.getType() == 3) {
            notification.setContent(notification.getUserName() + " " + context.getResources().getString(R.string.servei_commented));
        }
        descripcio.setText(notification.getContent());

        if (notification.isRead()) {
            titol.setTextColor(Color.GRAY);
            time.setTextColor(Color.GRAY);
        }

     return view;
    }
}
