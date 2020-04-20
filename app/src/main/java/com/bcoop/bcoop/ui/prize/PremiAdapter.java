package com.bcoop.bcoop.ui.prize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class PremiAdapter extends ArrayAdapter<Premi> {

    Context context;
    int resource;
    List<Premi> premiList;
    Premi premi;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PremiAdapter(Context context, int resource, List<Premi> premiList) {
        super(context, resource, premiList);

        this.context = context;
        this.resource = resource;
        this.premiList = premiList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        premi = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);



        ImageView imageView = view.findViewById(R.id.imageView3);
        TextView titol = view.findViewById(R.id.Titol);
        TextView descripcio = view.findViewById(R.id.Descripció);
        TextView preu = view.findViewById(R.id.Preu);



        titol.setText(premi.getNom());
        descripcio.setText(premi.getDescripció());
        preu.setText(String.valueOf(premi.getPreu()));

        view.findViewById(R.id.Comprar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                showAlertDialog(view, premiList.get(position));

             }
        });

     return view;
    }

    public void showAlertDialog(View v, final Premi p){
        p.setTime(new Time(System.currentTimeMillis()));
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.buy);
        alert.setMessage(R.string.sure);
        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                db.collection("Usuari").document(email)
                        .update("monedes", FieldValue.increment(-p.getPreu()),
                                "premis", FieldValue.arrayUnion(p))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), p.getNom(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show();
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
}
