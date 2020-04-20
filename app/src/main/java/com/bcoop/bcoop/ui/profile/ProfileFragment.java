package com.bcoop.bcoop.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bcoop.bcoop.Model.Comentari;
import com.bcoop.bcoop.Model.Habilitat;
import com.bcoop.bcoop.Model.HabilitatDetall;
import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore firestore;
    private ImageView imageView;
    private Button logout;
    private String uriImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        final String perfil = getArguments().getString("email");
        final String email;
        if (perfil.equals("myPerfil"))
            email = mAuth.getCurrentUser().getEmail();
        else email = perfil;

        imageView = root.findViewById(R.id.userImage);
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
                    level.setText(R.string.nivell);
                    level.setText(level.getText().toString().concat(": ").concat(Integer.toString(usuari.getNivell())));

                    if (mAuth.getCurrentUser().getEmail().equals(email)) {
                        money.setText(R.string.money);
                        money.setText(money.getText().toString().concat(": ").concat(Double.toString(usuari.getMonedes())));
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

        logout = root.findViewById(R.id.logout);
        if (email.equals(mAuth.getCurrentUser().getEmail())) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(Objects.requireNonNull(ProfileFragment.super.getActivity()), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mAuth.signOut();
                    startActivity(intent);
                }
            });
        }
        else logout.setVisibility(View.GONE);
        return root;
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