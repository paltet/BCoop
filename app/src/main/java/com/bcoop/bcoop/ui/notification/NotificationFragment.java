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

import com.bcoop.bcoop.Model.Habilitat;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.profile.HabilitatAdaptar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        //notificationsList.add(new Notification("sheng.liu0516@gmail.com", "Mates", 200, 20, Timestamp.now(), Timestamp.now()));
        listView = (ListView) root.findViewById(R.id.listView);
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email).collection("notificacions").orderBy("time", DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Notification notification = document.toObject(Notification.class);
                        if (notification.getType() == 1) {
                            notification.setTitle(getString(R.string.service_request));
                        }else if (notification.getType() == 2) {
                            notification.setTitle(getString(R.string.service_response));
                        }else if (notification.getType() == 3) {
                            notification.setTitle(getString(R.string.service_valoration));
                        }else if (notification.getType() == 4) {
                            notification.setTitle(getString(R.string.trading_information));
                        }
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
                        if (notification.getType() == 1) requestDialog(notification);
                        else tradingDialog(notification);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                        deleteDialog(notificationsList.get(pos));
                        return true;
                    }
                });
            }

        });

        return root;
    }

    private void deleteDialog(final Notification notification){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(getString(R.string.delete_notification));
        dialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notification.setRead(true);
                        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        db.collection("Usuari").document(email)
                                .collection("notificacions").document(notification.getTime().toString())
                                .delete();
                        dialog.cancel();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
                    }
                });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
            }
        });
        dialog.show();
    }

    private void tradingDialog(final Notification notification){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(notification.getTime().toDate());

        normalDialog.setTitle(notification.getTitle());
        normalDialog.setMessage(date + "\n" + notification.getContent());
        notification.setRead(true);
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .collection("notificacions").document(notification.getTime().toString())
                .set(notification);
        normalDialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
                    }
                });
        if (notification.getType() == 2) {
            normalDialog.setNegativeButton(R.string.chat_with, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
                }
            });
        }
        normalDialog.show();
    }

    private void requestDialog(final Notification notification) {
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout,null);
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
        price1.setText(notification.getPrice() +" "+ getString(R.string.coins));

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(getContext());
        layoutDialog.setTitle(notification.getTitle());
        layoutDialog.setCancelable(true);
        pattern = "dd/MM/yyyy HH:mm";
        simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(notification.getTime().toDate());
        layoutDialog.setMessage(date);

        layoutDialog.setView(dialogView);

        notification.setRead(true);
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db.collection("Usuari").document(email)
                .collection("notificacions").document(notification.getTime().toString())
                .set(notification);

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationFragment()).commit();
            }
        });

        layoutDialog.create().show();
    }
}
