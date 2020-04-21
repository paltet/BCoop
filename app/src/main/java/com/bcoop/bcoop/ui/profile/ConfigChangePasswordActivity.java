package com.bcoop.bcoop.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bcoop.bcoop.Model.Usuari;
import com.bcoop.bcoop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfigChangePasswordActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private boolean isEyeOpenCurrent = false, isEyeOpenNew = false, isEyeOpenConfirmNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_password);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        final EditText currentPwd = findViewById(R.id.currentPasswordForm);
        final ImageView eyeCurrentPwd = findViewById(R.id.eye_currentPassword);
        final EditText newPwd = findViewById(R.id.newPasswordForm);
        final ImageView eyeNewPwd = findViewById(R.id.eye_newPassword);
        final EditText confirmNewPwd = findViewById(R.id.confirmationPasswordForm);
        final ImageView eyeConfirmNewPwd = findViewById(R.id.eye_confirmationPassword);
        Button confirm = findViewById(R.id.confirmButton);

        eyeCurrentPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEyeOpenCurrent) {
                    eyeCurrentPwd.setImageDrawable(getResources().getDrawable((R.drawable.see)));
                    isEyeOpenCurrent = true;
                    currentPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    eyeCurrentPwd.setImageDrawable(getResources().getDrawable((R.drawable.hide)));
                    isEyeOpenCurrent = false;
                    currentPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        eyeNewPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEyeOpenNew) {
                    eyeNewPwd.setImageDrawable(getResources().getDrawable((R.drawable.see)));
                    isEyeOpenNew = true;
                    newPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    eyeNewPwd.setImageDrawable(getResources().getDrawable((R.drawable.hide)));
                    isEyeOpenNew = false;
                    newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        eyeCurrentPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEyeOpenConfirmNew) {
                    eyeConfirmNewPwd.setImageDrawable(getResources().getDrawable((R.drawable.see)));
                    isEyeOpenConfirmNew = true;
                    confirmNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    eyeConfirmNewPwd.setImageDrawable(getResources().getDrawable((R.drawable.hide)));
                    isEyeOpenConfirmNew = false;
                    confirmNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = currentPwd.getText().toString();
                final String newPassword = newPwd.getText().toString();
                final String confirmPassword = confirmNewPwd.getText().toString();
                if(!current.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    if (current.length() > 7) {
                        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), current);
                        mAuth.getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (newPassword.length() > 7) {
                                    if (confirmPassword.equals(newPassword)){
                                        mAuth.getCurrentUser().updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mAuth.getCurrentUser().reauthenticate(EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(), newPassword));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                startActivity(new Intent(ConfigChangePasswordActivity.this, ConfigChangePasswordActivity.class));
                                            }
                                        });
                                    }
                                    else {
                                        confirmNewPwd.setError(getString(R.string.confirm_passwd));
                                        confirmNewPwd.requestFocus();
                                    }
                                }
                                else {
                                    newPwd.setError(getString(R.string.unvalid_passwd));
                                    newPwd.requestFocus();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException)
                                    currentPwd.setError(getString(R.string.correct_password));
                            }
                        });
                    }
                    else {
                        currentPwd.setError(getString(R.string.unvalid_passwd));
                        currentPwd.requestFocus();
                    }
                }
            }
        });
    }
}