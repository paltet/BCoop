package com.bcoop.bcoop.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.bcoop.bcoop.ui.chat.ChatWithAnotherUserActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private ImageView imageView;
    private Button logout;
    private String uriImage;
    private Button proves;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        final String perfil = getArguments().getString("email");
        final String email;
        if (perfil.equals("myPerfil"))
            email = mAuth.getCurrentUser().getEmail();
        else email = perfil;

        imageView = root.findViewById(R.id.userImage);
        imageView.setImageResource(R.drawable.profile);
        final TextView username = root.findViewById(R.id.usernameText);
        final TextView level = root.findViewById(R.id.levelText);
        final TextView money = root.findViewById(R.id.moneyText);
        final ExpandableListView listHabilitats = root.findViewById(R.id.listHabilitats);

        //Obtenir dades des de FirebaseFirestore
        firestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firestore.collection("Usuari").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Usuari usuari = new Usuari();
                    usuari = documentSnapshot.toObject(Usuari.class);

                    username.setText(usuari.getNom());
                    level.setText(getString(R.string.nivell).concat(": ").concat(Integer.toString(usuari.getNivell())));

                    if (mAuth.getCurrentUser().getEmail().equals(email)) {
                        money.setText(getString(R.string.money).concat(": ").concat(Integer.toString(usuari.getMonedes())));
                    }
                    else {
                        String name = getLocality(usuari.getLocationLatitude(), usuari.getLocationLongitude());
                        money.setText(name);
                    }

                    uriImage = usuari.getFoto();
                    getImageFromStorage();

                    List<String> habilitatsUsuari = new ArrayList<>();
                    Map<String, HabilitatDetall> detallHabilitatUsuari = usuari.getHabilitats();
                    for (Map.Entry<String, HabilitatDetall> entry : detallHabilitatUsuari.entrySet())
                        habilitatsUsuari.add(entry.getKey());

                    HabilitatAdaptar habilitatAdaptar = new HabilitatAdaptar(getContext(), habilitatsUsuari, detallHabilitatUsuari, listHabilitats);
                    listHabilitats.setAdapter(habilitatAdaptar);
                }
            }
        });


        //inici codi de proves
        proves = root.findViewById(R.id.proves);
        View mView = getLayoutInflater().inflate(R.layout.popup_servei, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        proves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        //fi codi de proves

        logout = root.findViewById(R.id.logout);


        if (email.equals(mAuth.getCurrentUser().getEmail())) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(ProfileFragment.super.requireActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mAuth.signOut();
                    startActivity(intent);
                }
            });

        }
        else {
            //if not my user, then chat option
            logout.setText(R.string.chat);
            logout.setTextColor(Color.DKGRAY);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("otherUserEmail", email);
                    intent.setClass(ProfileFragment.super.requireActivity(), ChatWithAnotherUserActivity.class);
                    startActivity(intent);
                }
            });
        }
        return root;
    }

    private String getLocality(double locationLatitude, double locationLongitude) {
        String locality = "";
        if (locationLatitude == 0 && locationLongitude == 0)
            return locality;
        Geocoder myLocation = new Geocoder(ProfileFragment.super.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = myLocation.getFromLocation(locationLatitude, locationLongitude, 1);
            Address address = addresses.get(0);
            locality = address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locality;
    }

    private void getImageFromStorage() {
        if (uriImage != null) {
            StorageReference storageReference = storage.getReferenceFromUrl(uriImage);
            try {
                final File file = File.createTempFile("image", uriImage.substring(uriImage.lastIndexOf('.')));
                storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}