package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText password, mail, username, confirm_password;
    ImageView eye, confirm_eye;
    Button buttonRegister;
    private TextView privacy;
    boolean isOpenEyeP, isOpenEyeC;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(getString(R.string.register));
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        privacy = findViewById(R.id.text_privacy);
        privacy.setText(R.string.accept_privacy);

        mail = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        eye = (ImageView) findViewById(R.id.eye_password);
        confirm_password = (EditText) findViewById(R.id.register_confirm_password);
        confirm_eye = (ImageView) findViewById(R.id.eye_confirm);
        buttonRegister = findViewById(R.id.register);
        username = (EditText) findViewById(R.id.register_username);

        isOpenEyeP = false;
        isOpenEyeC = false;

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEyeP) {
                    eye.setImageDrawable(getResources().getDrawable((R.drawable.see)));
                    isOpenEyeP = true;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    eye.setImageDrawable(getResources().getDrawable((R.drawable.hide)));
                    isOpenEyeP = false;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        confirm_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEyeC) {
                    confirm_eye.setImageDrawable(getResources().getDrawable((R.drawable.see)));
                    isOpenEyeC = true;
                    confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    confirm_eye.setImageDrawable(getResources().getDrawable((R.drawable.hide)));
                    isOpenEyeC = false;
                    confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String psw = password.getText().toString();
                String cpsw = confirm_password.getText().toString();
                String usrn = username.getText().toString();



                if (usrn.isEmpty()){
                    username.setError(getString(R.string.unvalid_username));
                    username.requestFocus();
                }
                if(email.isEmpty() || !isValid(email)) {
                    mail.setError(getString(R.string.unvalid_email));
                    mail.requestFocus();
                }
                else if(psw.length() < 8){
                    password.setError(getString(R.string.unvalid_passwd));
                    password.requestFocus();
                }
                else if(!psw.equals(cpsw)){
                    confirm_password.setError(getString(R.string.confirm_passwd));
                    confirm_password.requestFocus();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, InitConfigActivity.class));
                            }
                            else{
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(RegisterActivity.this, R.string.error_register_email_already_used, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterActivity.this, R.string.error_register, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
