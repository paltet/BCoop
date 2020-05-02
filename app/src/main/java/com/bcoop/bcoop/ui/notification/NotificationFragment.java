package com.bcoop.bcoop.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.chat.ChatViewModel;
import com.bcoop.bcoop.ui.prize.MyPremi;
import com.bcoop.bcoop.ui.prize.PremiAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("notificacions");
                        for (Map<String, Object> hm : list) {
                            String type = (String) hm.get("type");
                            Timestamp time = (Timestamp) hm.get("time");
                            if(type.equals("Trading Information")) {
                                String content = (String) hm.get("content");
                                Notification notification = new Notification(content);
                                notification.setTime(time);
                                notificationsList.add(notification);
                            }
                        }
                    }

                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
                NotificationAdapter adapter = new NotificationAdapter(getContext(), R.layout.notification_list_item, notificationsList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Notification notification = notificationsList.get(position);
                    }
                });
            }

        });

        return root;
    }
}
