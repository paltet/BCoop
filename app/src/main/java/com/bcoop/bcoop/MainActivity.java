package com.bcoop.bcoop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    Button registerBtn;
    private FirebaseAuth mAuth;
    private Button loginBtn;
    private EditText mail;
    private EditText password;
    private Button signInButton;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = mail.getText().toString();
                String psw = password.getText().toString();

                if (usr.isEmpty()){
                    mail.setError(getString(R.string.unvalid_username));
                    mail.requestFocus();
                }
                else if(psw.length() < 8){
                    password.setError(getString(R.string.unvalid_passwd));
                    password.requestFocus();
                }
                else {
                    mAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        registerBtn = (Button) findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        signInButton = (Button) findViewById(R.id.loginWithGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                FirebaseGoogleAuth(acc);
            }
            catch (ApiException e){
                Log.e("", "Google sign in failed", e);
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if ("ExitApp".equals(intent.getAction())) {
            finish();
        }
    }*/

    public void onClick(View v) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        String language = "en";
         switch (v.getId()) {
            case R.id.en:
                config.locale = Locale.ENGLISH;
                language = "en";
                break;
            case R.id.es:
                config.locale = new Locale("es", "ES");
                language = "es";
                break;
            case R.id.ca:
                config.locale = new Locale("ca", "ES");
                language = "ca";
                break;
             default:
                 break;
         }
         editLanguage(language);
         resources.updateConfiguration(config, dm);
         Intent intent = new Intent(this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
     }

     public void editLanguage(String language){
        SharedPreferences preferences=getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_language", language);
        editor.apply();
    }

    public void setLanguage() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        SharedPreferences preferences=getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = preferences.getString("current_language", "");
        switch (language) {
            case "en":
                config.locale = Locale.ENGLISH;
                break;
            case "es":
                config.locale = new Locale("es", "ES");
                break;
            case "ca":
                config.locale = new Locale("ca", "ES");
                break;
        }
        resources.updateConfiguration(config, dm);
    }

}
