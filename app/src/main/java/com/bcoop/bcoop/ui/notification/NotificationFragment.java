package com.bcoop.bcoop.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.prize.MyPremi;
import com.bcoop.bcoop.ui.prize.PremiAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.content.ContentValues.TAG;

public class NotificationFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Notification> notificationsList;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationsList = new ArrayList<>();
        listView = (ListView) root.findViewById(R.id.listView);
        /*db.collection("Premi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String descripció = document.getString("descripció");
                                String imatge = document.getString("imatge");
                                Integer preu = document.getDouble("preu").intValue();
                                notificationsList.add(new Notification());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        NotificationAdapter adapter = new NotificationAdapter(getContext(), R.layout.notification_list_item, notificationsList);
                        listView.setAdapter(adapter);
                    }

                });*/
        NotificationAdapter adapter = new NotificationAdapter(getContext(), R.layout.notification_list_item, notificationsList);
        listView.setAdapter(adapter);

        return root;
    }
}
