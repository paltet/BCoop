package com.bcoop.bcoop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText password;
    ImageView eye;
    EditText confirm_password;
    ImageView confirm_eye;
    boolean isOpenEyeP;
    boolean isOpenEyeC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Register");
        actionBar.setDisplayHomeAsUpEnabled(true);

        password = (EditText) findViewById(R.id.register_password);
        eye = (ImageView) findViewById(R.id.eye_password);
        confirm_password = (EditText) findViewById(R.id.register_confirm_password);
        confirm_eye = (ImageView) findViewById(R.id.eye_confirm);
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
