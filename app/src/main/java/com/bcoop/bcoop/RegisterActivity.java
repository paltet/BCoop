package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText password, mail;
    ImageView eye;
    EditText confirm_password;
    ImageView confirm_eye;
    Button buttonRegister;
    boolean isOpenEyeP;
    boolean isOpenEyeC;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Register");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mail = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        eye = (ImageView) findViewById(R.id.eye_password);
        confirm_password = (EditText) findViewById(R.id.register_confirm_password);
        confirm_eye = (ImageView) findViewById(R.id.eye_confirm);
        buttonRegister = findViewById(R.id.register);

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

                if(email.isEmpty()) {
                    mail.setError("Please enter a valid email");
                    mail.requestFocus();
                }
                else if(psw.isEmpty()){
                    password.setError("Please enter a valid password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && psw.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Error during register, Try Again", Toast.LENGTH_SHORT).show();

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
}
