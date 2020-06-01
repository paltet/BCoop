package com.bcoop.bcoop.ui.prize;

import android.content.Intent;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bcoop.bcoop.Model.CurrentUser;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.notification.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PrizeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Premi> premiList;
    ListView listView;
    int pmin = Integer.MIN_VALUE;
    int pmax = Integer.MAX_VALUE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prize, container, false);
        EditText min = root.findViewById(R.id.min);
        EditText max = root.findViewById(R.id.max);
        Button accept = root.findViewById(R.id.button);
        Button scan = root.findViewById(R.id.Scan);
        if (!CurrentUser.getInstance().getCurrentUser().isEsTienda()) scan.setVisibility(View.GONE);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(min.getText()) || TextUtils.isEmpty(max.getText())) {
                    pmin = Integer.MIN_VALUE;
                    pmax = Integer.MAX_VALUE;
                }
                else {
                    pmin = Integer.parseInt(min.getText().toString());
                    pmax = Integer.parseInt(max.getText().toString());
                }
                premiList = new ArrayList<>();
                listView = (ListView) root.findViewById(R.id.listView);
                db.collection("Premi").whereGreaterThanOrEqualTo("preu", pmin).whereLessThanOrEqualTo("preu", pmax)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Premi premi = document.toObject(Premi.class);
                                        premiList.add(premi);
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                                PremiAdapter adapter = new PremiAdapter(getContext(), R.layout.my_list_item, premiList, false);
                                listView.setAdapter(adapter);
                            }

                        });
            }
        });

        premiList = new ArrayList<>();
        listView = (ListView) root.findViewById(R.id.listView);
        db.collection("Premi").whereGreaterThanOrEqualTo("preu", pmin).whereLessThanOrEqualTo("preu", pmax)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Premi premi = document.toObject(Premi.class);
                                premiList.add(premi);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        PremiAdapter adapter = new PremiAdapter(getContext(), R.layout.my_list_item, premiList, false);
                        listView.setAdapter(adapter);
                    }

                });
        root.findViewById(R.id.VeurePremis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPremi.class);
                startActivity(intent);
            }
        });
        root.findViewById(R.id.Scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}