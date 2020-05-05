package com.bcoop.bcoop.ui.notification;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class NotificationFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Notification> notificationsList;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationsList = new ArrayList<>();
        // service request
        //notificationsList.add(new Notification("sl@gmail.com", "Mates", 200, 20, Timestamp.now(), Timestamp.now()));
        listView = (ListView) root.findViewById(R.id.listView);
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email).collection("notificacions").orderBy("time", DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Notification notification = document.toObject(Notification.class);
                        notificationsList.add(notification);
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
                        if (notification.getType().equals("Service Request"))
                            showLayoutDialog(notification);
                        else showNormalDialog(notification);
                    }
                });
            }

        });

        return root;
    }

    private void showNormalDialog(final Notification notification){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        normalDialog.setTitle(notification.getType());
        normalDialog.setMessage(notification.getContent());
        normalDialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notification.setRead(true);
                        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        db.collection("Usuari").document(email)
                                .collection("notificacions").document(notification.getTime().toString())
                                .set(notification);
                    }
                });
        normalDialog.show();
    }

    private void showLayoutDialog(final Notification notification) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout,null);
        Button dialogBtnRefuse = (Button) dialogView.findViewById(R.id.refuse);
        Button dialogBtnChat = (Button) dialogView.findViewById(R.id.chat);
        Button dialogBtnAccept = (Button) dialogView.findViewById(R.id.accept);

        TextView applicantName = dialogView.findViewById(R.id.applicantName);
        TextView habilitatName = dialogView.findViewById(R.id.habilitatName);
        TextView dateIni1 = dialogView.findViewById(R.id.dateIni1);
        TextView dateFi1 = dialogView.findViewById(R.id.dateFi1);
        TextView duration1 = dialogView.findViewById(R.id.duration1);
        TextView price1 = dialogView.findViewById(R.id.price1);

        applicantName.setText(notification.getUserEmail());
        habilitatName.setText(notification.getServiceName());
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(notification.getDataIni().toDate());
        dateIni1.setText(date);
        date = simpleDateFormat.format(notification.getDateFi().toDate());
        dateFi1.setText(date);
        duration1.setText(notification.getDuration() + " horas");
        price1.setText(notification.getPrice() + " monedes");

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(getContext());
        layoutDialog.setTitle(notification.getType());

        layoutDialog.setView(dialogView);

        notification.setRead(true);
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .collection("notificacions").document(notification.getTime().toString())
                .set(notification);
        Toast.makeText(getActivity(),notification.getTime().toDate().toString(),Toast.LENGTH_SHORT).show();

        dialogBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification1 = new Notification(FirebaseAuth.getInstance().getCurrentUser().getEmail(), true);
                db.collection("Usuari").document(notification.getUserEmail())
                        .collection("notificacions").document(notification1.getTime().toString()).set(notification1)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), R.string.abort, Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(getActivity(),R.string.success,Toast.LENGTH_SHORT).show();
            }
        });
        dialogBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialogBtnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification n = new Notification(FirebaseAuth.getInstance().getCurrentUser().getEmail(), false);
                db.collection("Usuari").document(notification.getUserEmail())
                        .collection("notificacions").document(n.getTime().toString()).set(n)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), R.string.abort, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        layoutDialog.create().show();
    }


}
