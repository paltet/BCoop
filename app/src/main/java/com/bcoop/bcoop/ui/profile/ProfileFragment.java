package com.bcoop.bcoop.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private ImageView imageView;
    private Button logout;
    private Button report;
    private Button viewReport;
    private String uriImage;
    private Button makeAdmin;
    private Button makeShop;
    private Button blockUser;
    private Button ReportSendButton;
    private EditText EditReport;
    private boolean admin = false;


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

        final DocumentReference documentReferenceAdmin = firestore.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
        documentReferenceAdmin.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    admin = documentSnapshot.getBoolean("esAdministrador");
                    if (admin) {
                        viewReport.setVisibility(View.VISIBLE);
                        makeAdmin.setVisibility(View.VISIBLE);
                        blockUser.setVisibility(View.VISIBLE);
                        makeShop.setVisibility(View.VISIBLE);
                        viewReport.setTextColor(Color.BLACK);
                        viewReport.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                        viewReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("otherUser", email);
                                intent.setClass(ProfileFragment.super.requireActivity(), ReportActivity.class);
                                startActivity(intent);
                            }
                        });

                        blockUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockUser(email);
                            }
                        });


                        makeAdmin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeadmin(email);
                            }
                        });

                        makeShop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeShop(email);
                            }
                        });
                    }
                }
            }
        });

        logout = root.findViewById(R.id.logout);
        makeAdmin = root.findViewById(R.id.makeAdmin);
        makeShop = root.findViewById(R.id.makeShop);
        blockUser = root.findViewById(R.id.blockUser);
        report = root.findViewById(R.id.Report);
        viewReport = root.findViewById(R.id.viewReportsButton);
        viewReport.setVisibility(View.GONE);
        makeAdmin.setVisibility(View.GONE);
        makeShop.setVisibility(View.GONE);
        blockUser.setVisibility(View.GONE);

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
            report.setVisibility(View.GONE);
            makeAdmin.setVisibility(View.GONE);
            makeShop.setVisibility(View.GONE);
        }
        else {
            logout.setText(R.string.chat);
            logout.setTextColor(Color.BLACK);
            logout.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("otherUserEmail", email);
                    intent.setClass(ProfileFragment.super.requireActivity(), ChatWithAnotherUserActivity.class);
                    startActivity(intent);
                }
            });

            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View viewReport = getLayoutInflater().inflate(R.layout.reportuser, null);
                    final AlertDialog.Builder alertReport = new AlertDialog.Builder(getContext());
                    alertReport.setView(viewReport);
                    final AlertDialog alertDialogReport = alertReport.create();
                    alertDialogReport.setCanceledOnTouchOutside(true);

                    alertDialogReport.show();
                    EditReport = viewReport.findViewById(R.id.editText5);
                    ReportSendButton = viewReport.findViewById(R.id.sendReport);
                    ReportSendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String item = EditReport.getText().toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("Informe", item);
                            data.put("user", email);
                            Date date = Calendar.getInstance().getTime();
                            data.put("data", date.toString());
                            firestore.collection("Reports").add(data);
                            alertDialogReport.dismiss();
                            Toast.makeText(getContext(), R.string.ReportEnviat, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
        return root;
    }

    private void makeShop(String email) {
        firestore.collection("Usuari").document(email).update("esTienda", true);
    }

    private void blockUser(String email) {
        Date date = Calendar.getInstance().getTime();
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        firestore.collection("Usuari").document(email).update("blocked", true);
        firestore.collection("Usuari").document(email).update("lastBloqueig", date);
    }

    private void makeadmin(String email) {

        firestore.collection("Usuari").document(email).update("esAdministrador", true);
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