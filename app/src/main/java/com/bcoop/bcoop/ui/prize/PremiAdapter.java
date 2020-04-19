package com.bcoop.bcoop.ui.prize;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcoop.bcoop.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class PremiAdapter extends ArrayAdapter<Premi> {

    Context context;
    int resource;
    List<Premi> premiList;

    public PremiAdapter(Context context, int resource, List<Premi> premiList) {
        super(context, resource, premiList);

        this.context = context;
        this.resource = resource;
        this.premiList = premiList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resource, null);

        ImageView imageView = view.findViewById(R.id.imageView3);
        TextView titol = view.findViewById(R.id.Titol);
        TextView descripcio = view.findViewById(R.id.Descripció);
        TextView preu = view.findViewById(R.id.Preu);

        Premi premi = premiList.get(position);

        titol.setText(premi.getNom());
        descripcio.setText(premi.getDescripció());
        preu.setText(String.valueOf(premi.getPreu()));

        view.findViewById(R.id.Comprar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                showAlertDialog(view);
             }
        });

     return view;
    }

    public void showAlertDialog(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.buy);
        alert.setMessage(R.string.sure);
        alert.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
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
