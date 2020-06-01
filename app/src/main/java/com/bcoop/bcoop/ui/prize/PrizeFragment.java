package com.bcoop.bcoop.ui.prize;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.notification.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PrizeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Premi> premiList;
    ListView listView;
    private Button dbAdd;
    private Button dbAddItem;
    private EditText dbdescr;
    private EditText dbpreu;
    private EditText dbnomText;
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


        isAdmin();
        dbAdd = root.findViewById(R.id.AddPremiDB);
        dbAdd.setVisibility(View.VISIBLE);
        dbAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View v) {
                View mViewDB = getLayoutInflater().inflate(R.layout.add_prize_db, null);

                final AlertDialog.Builder alertDB = new AlertDialog.Builder(getContext());
                alertDB.setView(mViewDB);
                final AlertDialog alertDialogDB = alertDB.create();
                alertDialogDB.setCanceledOnTouchOutside(true);

                alertDialogDB.show();
                dbnomText = mViewDB.findViewById(R.id.NameDB);
                dbdescr = mViewDB.findViewById(R.id.descripcioPriceDB);
                dbpreu = mViewDB.findViewById(R.id.preu);
                dbAddItem = mViewDB.findViewById(R.id.addPriceDBItem);
                dbAddItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nom = dbnomText.getText().toString();
                        String descr = dbdescr.getText().toString();
                        int val = Integer.parseInt( dbpreu.getText().toString() );
                        Map<String, Object> data = new HashMap<>();
                        data.put("descripció", descr);
                        data.put("imatge", "");
                        data.put("nom", nom);
                        data.put("preu", val);
                        db.collection("Premi").add(data);
                        alertDialogDB.dismiss();
                    }
                });
            }
            });


            premiList =new ArrayList<>();
            listView =(ListView)root.findViewById(R.id.listView);
        db.collection("Premi")
                .

            get()
                .

            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete (@NonNull Task < QuerySnapshot > task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nom = document.getString("nom");
                            String descripció = document.getString("descripció");
                            String imatge = document.getString("imatge");
                            Integer preu = document.getDouble("preu").intValue();
                            premiList.add(new Premi(nom, descripció, imatge, preu, null, null));
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                    PremiAdapter adapter = new PremiAdapter(getContext(), R.layout.my_list_item, premiList, false);
                    listView.setAdapter(adapter);
                }
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
        root.findViewById(R.id.VeurePremis).

            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){
                    Intent intent = new Intent(getContext(), MyPremi.class);
                    startActivity(intent);
                }
            });
        root.findViewById(R.id.Scan).

            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){
                    Intent intent = new Intent(getContext(), ScanActivity.class);
                    startActivity(intent);
                }
            });


        return root;
        }


    private void isAdmin() {

            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            final boolean[] adminlocal = {true};
            final DocumentReference documentReference = db.collection("Usuari").document(email);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Usuari usuari = new Usuari();
                        usuari = documentSnapshot.toObject(Usuari.class);
                        adminlocal[0] = usuari.isEsAdministrador();
                        if (adminlocal[0]) dbAdd.setVisibility(View.VISIBLE);
                        else dbAdd.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }