package com.bcoop.bcoop.ui.prize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.HomeActivity;
import com.bcoop.bcoop.Model.Notification;
import com.bcoop.bcoop.Model.Premi;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.notification.ConectFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import static android.content.ContentValues.TAG;

public class PremiAdapter extends ArrayAdapter<Premi> {

    Context context;
    int resource;
    List<Premi> premiList;
    Premi premi;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isMyPremis = false;
    ConectFirebase conectFirebase = new ConectFirebase();

    public PremiAdapter(Context context, int resource, List<Premi> premiList, boolean isMyPremis) {
        super(context, resource, premiList);

        this.context = context;
        this.resource = resource;
        this.premiList = premiList;
        this.isMyPremis = isMyPremis;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        premi = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

        ImageView imageView = view.findViewById(R.id.imageView3);
        TextView titol = view.findViewById(R.id.Titol);
        TextView descripcio = view.findViewById(R.id.Descripcio);
        TextView preu = view.findViewById(R.id.Preu);

        titol.setText(premi.getNom());
        descripcio.setText(premi.getDescripció());
        preu.append(": " + String.valueOf(premi.getPreu()) + " " + context.getResources().getString(R.string.coins));

        if (isMyPremis) {
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(premi.getTime().toDate());
            preu.append("\n" +date);
            Button btn = view.findViewById(R.id.Comprar);
            btn.setText(R.string.use);
            btn.setVisibility(View.VISIBLE);
            if (premi.isUse()) btn.setVisibility(View.GONE);
        }
        view.findViewById(R.id.Comprar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (!isMyPremis) showAlertDialogBuy(view, premiList.get(position));
                 else showAlertDialogUse(view, premiList.get(position));
             }
        });

     return view;
    }

    public void showAlertDialogBuy(View v, final Premi p){
        p.setTime(Timestamp.now());
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext());
        alert.setTitle(R.string.buy);
        alert.setMessage(R.string.sure);
        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                db.collection("Usuari").document(email)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Integer monedes = document.getDouble("monedes").intValue();
                                if (monedes < p.getPreu()) {
                                    Toast.makeText(getContext(), R.string.notEnough, Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("isUsed", false);
                                    data.put("user", email);
                                    db.collection("Premi Venut").add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            p.setId(documentReference.getId());
                                            db.collection("Usuari").document(email).collection("premis").add(p);
                                            db.collection("Usuari").document(email).update("monedes", FieldValue.increment(-p.getPreu()));
                                            Toast.makeText(getContext(), R.string.success, Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });

                                    Notification notification = new Notification(-p.getPreu());
                                    conectFirebase.pushNotification(notification, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                }
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.abort, Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    public void showAlertDialogUse(View v, final Premi p){
        p.setTime(Timestamp.now());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_code,null);
        ImageView code = dialogView.findViewById(R.id.code);

        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext());
        alert.setView(dialogView);
        alert.setTitle(R.string.use);
        alert.setMessage(context.getResources().getString(R.string.to_store) + '\n' +p.getId());
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(p.getId(), 480, 480);
        code.setImageBitmap(mBitmap);
        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), p.getNom(), Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), R.string.abort, Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }
}
