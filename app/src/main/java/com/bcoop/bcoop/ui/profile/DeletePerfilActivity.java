package com.bcoop.bcoop.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcoop.bcoop.MainActivity;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeletePerfilActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private String imgUri;
    private EditText pwdUser;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_delete_user);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        pwdUser = findViewById(R.id.newUsernameForm);
        confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwdUser.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), pwd);
                mAuth.getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final DocumentReference documentReference = firestore.collection("Usuari").document(mAuth.getCurrentUser().getEmail());
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    imgUri = documentSnapshot.getString("foto");
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if (imgUri != null) {
                                                        StorageReference storageReference = storage.getReferenceFromUrl(imgUri);
                                                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(DeletePerfilActivity.this, MainActivity.class));
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(DeletePerfilActivity.this, "No borra img", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(DeletePerfilActivity.this, MainActivity.class));
                                                            }
                                                        });
                                                    }
                                                    else startActivity(new Intent(DeletePerfilActivity.this, MainActivity.class));
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(DeletePerfilActivity.this).create();
                                            alertDialog.setTitle(R.string.delete_perfil);
                                            alertDialog.setMessage((Integer.toString(R.string.perfil_cannot_delete)));
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(DeletePerfilActivity.this, ConfigProfileActivity.class));
                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startActivity(new Intent(DeletePerfilActivity.this, ConfigProfileActivity.class));
                    }
                });
            }
        });
    }
}