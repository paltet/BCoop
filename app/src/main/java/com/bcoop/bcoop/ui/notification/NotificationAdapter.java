package com.bcoop.bcoop.ui.notification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
        TextView descripcio = view.findViewById(R.id.Descripció);

        titol.setText(notification.getType());
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(notification.getTime().toDate());
        time.setText(date);
        descripcio.setText(notification.getContent());

        if (notification.isRead()) {
            titol.setTextColor(Color.GRAY);
            time.setTextColor(Color.GRAY);
        }

     return view;
    }
}
